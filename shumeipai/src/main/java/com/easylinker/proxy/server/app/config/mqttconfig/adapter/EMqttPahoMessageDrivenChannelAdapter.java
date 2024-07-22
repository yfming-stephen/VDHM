package com.easylinker.proxy.server.app.config.mqttconfig.adapter;

import com.easylinker.proxy.server.app.model.EMQInfo;
import com.easylinker.proxy.server.app.service.EMQInfoService;
import com.easylinker.proxy.server.app.utils.HttpTool;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.integration.mqtt.core.ConsumerStopAction;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.event.MqttConnectionFailedEvent;
import org.springframework.integration.mqtt.event.MqttSubscribedEvent;
import org.springframework.integration.mqtt.inbound.AbstractMqttMessageDrivenChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * EMQ定制的MQTT消息处理器
 */
public class EMqttPahoMessageDrivenChannelAdapter extends AbstractMqttMessageDrivenChannelAdapter implements MqttCallback, ApplicationEventPublisherAware {
    private static final int DEFAULT_COMPLETION_TIMEOUT = 30000;
    private static final int DEFAULT_RECOVERY_INTERVAL = 10000;
    private final MqttPahoClientFactory clientFactory;
    private volatile IMqttClient client;
    private volatile ScheduledFuture<?> reconnectFuture;
    private volatile boolean connected;
    private volatile int completionTimeout;
    private volatile int recoveryInterval;
    private volatile boolean cleanSession;
    private volatile ConsumerStopAction consumerStopAction;
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    EMQInfoService emqInfoService;
    @Autowired
    HttpTool httpTool;
    @Value("${emq.api.host}")
    String apiHost;
    @Value("${emq.node.name}")
    String emqNodeName;

    public EMqttPahoMessageDrivenChannelAdapter(String url, String clientId, MqttPahoClientFactory clientFactory, String... topic) {
        super(url, clientId, topic);
        this.completionTimeout = 30000;
        this.recoveryInterval = 10000;
        this.clientFactory = clientFactory;
    }

    public EMqttPahoMessageDrivenChannelAdapter(String clientId, MqttPahoClientFactory clientFactory, String... topic) {
        super((String) null, clientId, topic);
        this.completionTimeout = 30000;
        this.recoveryInterval = 10000;
        this.clientFactory = clientFactory;
    }

    public EMqttPahoMessageDrivenChannelAdapter(String url, String clientId, String... topic) {
        this(url, clientId, new DefaultMqttPahoClientFactory(), topic);
    }

    public void setCompletionTimeout(int completionTimeout) {
        this.completionTimeout = completionTimeout;
    }

    public void setRecoveryInterval(int recoveryInterval) {
        this.recoveryInterval = recoveryInterval;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * MQTT启动异常
     */
    @Override
    protected void doStart() {
        Assert.state(this.getTaskScheduler() != null, "A 'taskScheduler' is required");
        super.doStart();

        try {
            this.connectAndSubscribe();
        } catch (Exception var2) {
            this.logger.error(var2);
            this.logger.error("连接Emq服务器失败，请检查配置确保EMQ启动成功！");
            //this.logger.error("Exception while connecting and subscribing, retrying", var2);
            this.scheduleReconnect();
            //System.exit(1);本来这里是直接退出的，但是考虑到两边互相不影响，发布提示消息就行。
        }

    }

    @Override
    protected synchronized void doStop() {
        this.cancelReconnect();
        super.doStop();
        if (this.client != null) {
            try {
                if (this.consumerStopAction.equals(ConsumerStopAction.UNSUBSCRIBE_ALWAYS) || this.consumerStopAction.equals(ConsumerStopAction.UNSUBSCRIBE_CLEAN) && this.cleanSession) {
                    this.client.unsubscribe(this.getTopic());
                }
            } catch (MqttException var4) {
                //this.logger.error("Exception while unsubscribing", var4);
                this.logger.error("Exception while unsubscribing");
            }

            try {
                this.client.disconnectForcibly((long) this.completionTimeout);
            } catch (MqttException var3) {
                //this.logger.error("Exception while disconnecting", var3);
                this.logger.error("Exception while disconnecting");
            }

            try {
                this.client.close();
            } catch (MqttException var2) {
                //this.logger.error("Exception while closing", var2);
                this.logger.error("Exception while closing");
            }

            this.connected = false;
            this.client = null;
        }

    }

    @Override
    public void addTopic(String topic, int qos) {
        this.topicLock.lock();

        try {
            super.addTopic(topic, qos);
            if (this.client != null && this.client.isConnected()) {
                this.client.subscribe(topic, qos);
            }
        } catch (MqttException var7) {
            super.removeTopic(new String[]{topic});
            throw new MessagingException("Failed to subscribe to topic " + topic, var7);
        } finally {
            this.topicLock.unlock();
        }

    }

    @Override
    public void removeTopic(String... topic) {
        this.topicLock.lock();

        try {
            if (this.client != null && this.client.isConnected()) {
                this.client.unsubscribe(topic);
            }

            super.removeTopic(topic);
        } catch (MqttException var6) {
            throw new MessagingException("Failed to unsubscribe from topic " + Arrays.asList(topic), var6);
        } finally {
            this.topicLock.unlock();
        }

    }

    /**
     * 执行订阅
     *
     * @throws MqttException
     */
    private synchronized void connectAndSubscribe() throws MqttException {
        MqttConnectOptions connectionOptions = this.clientFactory.getConnectionOptions();
        this.cleanSession = connectionOptions.isCleanSession();
        this.consumerStopAction = this.clientFactory.getConsumerStopAction();
        if (this.consumerStopAction == null) {
            this.consumerStopAction = ConsumerStopAction.UNSUBSCRIBE_CLEAN;
        }

        Assert.state(this.getUrl() != null || connectionOptions.getServerURIs() != null, "If no 'url' provided, connectionOptions.getServerURIs() must not be null");
        this.client = this.clientFactory.getClientInstance(this.getUrl(), this.getClientId());
        this.client.setCallback(this);
        if (this.client instanceof MqttClient) {
            ((MqttClient) this.client).setTimeToWait((long) this.completionTimeout);
        }

        this.topicLock.lock();
        String[] topics = this.getTopic();

        try {
            this.client.connect(connectionOptions);
            int[] requestedQos = this.getQos();
            int[] grantedQos = Arrays.copyOf(requestedQos, requestedQos.length);
            this.client.subscribe(topics, grantedQos);

            for (int i = 0; i < requestedQos.length; ++i) {
                if (grantedQos[i] != requestedQos[i]) {
                    if (this.logger.isWarnEnabled()) {
                        this.logger.warn("Granted QOS different to Requested QOS; topics: " + Arrays.toString(topics) + " requested: " + Arrays.toString(requestedQos) + " granted: " + Arrays.toString(grantedQos));
                    }
                    break;
                }
            }
        } catch (MqttException var9) {
            if (this.applicationEventPublisher != null) {
                this.applicationEventPublisher.publishEvent(new MqttConnectionFailedEvent(this, var9));
            }
            /**
             * 当连接失败或者ACL 拒绝的时候，这里抛出异常，此时就需要检查EMQ的配置了
             */

            this.logger.error("EMQ配置错误，导致EasyLinker无法连接，检查配置！");
            //连接失败以后,吧数据库里面的状态更新一下
            EMQInfo emqInfo = new EMQInfo();
            emqInfo.setId(1L);
            emqInfo.setConnected(false);
            emqInfoService.save(emqInfo);
            this.client.disconnectForcibly((long) this.completionTimeout);
            throw var9;
        } finally {
            this.topicLock.unlock();
        }

        if (this.client.isConnected()) {
            this.connected = true;
            String message = "Connected and subscribed to " + Arrays.toString(topics);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug(message);
            }

            if (this.applicationEventPublisher != null) {
                this.applicationEventPublisher.publishEvent(new MqttSubscribedEvent(this, message));
            }
            //连接成功  management/nodes/emq@127.0.0.1
            EMQInfo emqInfo = new EMQInfo();
            emqInfo.setId(1L);
            emqInfo.setConnected(true);
            emqInfoService.save(emqInfo);

        }

    }

    private synchronized void cancelReconnect() {
        if (this.reconnectFuture != null) {
            this.reconnectFuture.cancel(false);
            this.reconnectFuture = null;
        }

    }

    /**
     * 启动一个线程，定时重新连接
     */
    private void scheduleReconnect() {
        try {
            this.reconnectFuture = this.getTaskScheduler().schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (EMqttPahoMessageDrivenChannelAdapter.this.logger.isDebugEnabled()) {
                            EMqttPahoMessageDrivenChannelAdapter.this.logger.debug("Attempting reconnect");
                        }

                        EMqttPahoMessageDrivenChannelAdapter var1 = EMqttPahoMessageDrivenChannelAdapter.this;
                        synchronized (EMqttPahoMessageDrivenChannelAdapter.this) {
                            if (!EMqttPahoMessageDrivenChannelAdapter.this.connected) {
                                EMqttPahoMessageDrivenChannelAdapter.this.connectAndSubscribe();
                                EMqttPahoMessageDrivenChannelAdapter.this.reconnectFuture = null;
                            }
                        }
                    } catch (MqttException var4) {
                        /**
                         * 尝试重新连接引发的异常
                         */
                        EMqttPahoMessageDrivenChannelAdapter.this.logger.error("EMQ配置错误，导致EasyLinker无法连接，检查配置！");
                        //EMqttPahoMessageDrivenChannelAdapter.this.logger.error("Exception while connecting and subscribing", var4);
                        EMqttPahoMessageDrivenChannelAdapter.this.scheduleReconnect();
                    }

                }
            }, new Date(System.currentTimeMillis() + (long) this.recoveryInterval));
        } catch (Exception var2) {
            this.logger.error("Failed to schedule reconnect", var2);
        }

    }

    /**
     * 连接断开
     *
     * @param cause
     */
    @Override
    public synchronized void connectionLost(Throwable cause) {
        this.logger.error("EasyLinker和EMQ的连接断开，尝试重新连接······");
        //this.logger.error("Lost connection:" + cause.getMessage() + "; retrying...");
        this.connected = false;
        this.scheduleReconnect();
        if (this.applicationEventPublisher != null) {
            this.applicationEventPublisher.publishEvent(new MqttConnectionFailedEvent(this, cause));
        }

    }

    /**
     * 消息到达
     *
     * @param topic
     * @param mqttMessage
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        Message message = this.getConverter().toMessage(topic, mqttMessage);

        try {
            this.sendMessage(message);
        } catch (RuntimeException var5) {
            this.logger.error("Unhandled exception for " + message.toString(), var5);
            throw var5;
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }


}
