<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i>实时数据</el-breadcrumb-item>
                <el-breadcrumb-item>在线设备</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-tabs v-model="activeName">
            <el-tab-pane label="全部设备" name="common">
                <!-- 模拟 -->
                <!-- <div class="realtimedata">
                    <el-row :gutter="24">
                            <el-col :span="10">
                                <el-card class="box-card">
                                    <div slot="header" class="clearfix">
                                        <el-row :gutter="24">
                                            <el-col :span="12" :offset="6">
                                                <center>
                                                    <span>温度</span>
                                                </center>
                                            </el-col>
                                            <el-col :span="6">
                                                <el-tag type="success">TEMP</el-tag>
                                            </el-col>
                                        </el-row>

                                    </div>
                                    <div style="height:120px;">
                                        <center>
                                            <div class="demo-color-box bg-blue">
                                                23 °C
                                            </div>
                                        </center>
                                    </div>
                                </el-card>
                            </el-col>
                            <el-col :span="10">
                                <el-card class="box-card">
                                    <div slot="header" class="clearfix">
                                        <el-row :gutter="24">
                                            <el-col :span="12" :offset="6">
                                                <center>
                                                    <span>速度</span>
                                                </center>
                                            </el-col>
                                            <el-col :span="6">
                                                <el-tag type="success">SPEED</el-tag>
                                            </el-col>
                                        </el-row>

                                    </div>
                                    <div style="height:120px;">
                                        <center>
                                            <div class="demo-color-box bg-blue">
                                                70 mph
                                            </div>
                                        </center>
                                    </div>
                                </el-card>
                            </el-col>
                            <el-col :span="10">
                                <el-card class="box-card">
                                    <div slot="header" class="clearfix">
                                        <el-row :gutter="24">
                                            <el-col :span="12" :offset="6">
                                                <center>
                                                    <span>心率</span>
                                                </center>
                                            </el-col>
                                            <el-col :span="6">
                                                <el-tag type="success">HEART</el-tag>
                                            </el-col>
                                        </el-row>

                                    </div>
                                    <div style="height:120px;">
                                        <center>
                                            <div class="demo-color-box bg-blue">
                                                62 次/分
                                            </div>
                                        </center>
                                    </div>
                                </el-card>
                            </el-col>
                            <el-col :span="10">
                                <el-card class="box-card">
                                    <div slot="header" class="clearfix">
                                        <el-row :gutter="24">
                                            <el-col :span="12" :offset="6">
                                                <center>
                                                    <span>血压</span>
                                                </center>
                                            </el-col>
                                            <el-col :span="6">
                                                <el-tag type="success">PRESSURE</el-tag>
                                            </el-col>
                                        </el-row>

                                    </div>
                                    <div style="height:120px;">
                                        <center>
                                            <div class="demo-color-box bg-blue">
                                                118 mmHg
                                            </div>
                                        </center>
                                    </div>
                                </el-card>
                            </el-col>
                    </el-row>
                </div> -->
                <!-- 模拟 -->


                <div class="realtimedata">
                    <center v-if="deviceData.length===0"><h1>当前暂无在线设备</h1></center>
                    <el-row :gutter="24">
                        <div v-for="(item, index) in deviceData" :key="index">
                            <el-col :span="10">
                                <el-card class="box-card">
                                    <div slot="header" class="clearfix">
                                        <el-row :gutter="24">
                                            <el-col :span="12" :offset="6">
                                                <center>
                                                    <span>{{item.name}}</span>
                                                </center>
                                            </el-col>
                                            <el-col :span="6">
                                                <el-tag type="success">{{item.type}}</el-tag>
                                            </el-col>
                                        </el-row>

                                    </div>
                                    <div style="height:120px;">
                                        <center>
                                            <div class="demo-color-box bg-blue">
                                                {{item.value}}
                                            </div>
                                        </center>
                                    </div>
                                </el-card>
                            </el-col>
                        </div>
                    </el-row>
                </div>
                <div class="pagination">
                    <el-pagination @current-change="handleCurrentChange" layout="prev, pager, next" :total="total">
                    </el-pagination>
                </div>
            </el-tab-pane>
            <el-tab-pane label="LIVE" name="live">
              <videoPlayer class="vjs-custom-skin videoPlayer" ref="videoplayer"
                           :playsinline="true" width="90%" :options="playerOptions"
                           customEventName="changed" >
              </videoPlayer>
                <el-row>
                    <!-- 模拟 -->
                    <!-- <el-col :span="6">
                        <el-menu @select="handleSelect">
                                <el-menu-item :index="1">
                                    <i class="el-icon-upload"></i>
                                    <span>奥迪A6</span>
                                    <el-button class="start-push" type="primary" size="small" @click="startLive(1)">启动</el-button>
                                    <el-button class="stop-push" size="small" @click="stopPushLive(1)">停止</el-button>
                                </el-menu-item>
                                <el-menu-item :index="2">
                                    <i class="el-icon-upload"></i>
                                    <span>马自达</span>
                                    <el-button class="start-push" type="info" size="small" @click="startLive(1)">启动</el-button>
                                    <el-button class="stop-push" type="info" size="small" @click="stopPushLive(1)">停止</el-button>
                                </el-menu-item>
                        </el-menu>
                    </el-col> -->
                    <!-- 模拟 -->


                    <el-col :span="6">
                        <el-menu @select="handleSelect">
                            <center v-if="liveData.length===0"><h3>当前暂无在线设备</h3></center>
                            <div v-for="(item, index) in liveData" :key="index">
                                <el-menu-item :index="item.id.toString()">
                                    <i class="el-icon-upload"></i>
                                    <span>{{item.name}}</span>
                                    <el-button class="start-push" type="info" size="small" @click="startLive(item.id)">启动</el-button>
                                    <el-button class="stop-push" type="info" size="small" @click="stopPushLive(item.id)">停止</el-button>
                                </el-menu-item>
                            </div>
                        </el-menu>
                    </el-col>
                    <el-col :span="18">
                        <div id="J_prismPlayer" class="prism-player"></div>
                    </el-col>
                </el-row>
            </el-tab-pane>

            <!-- 调试 -->
            <!--<el-tab-pane label="test" name="aaa">-->
                <!--<div id="a_prismPlayer" class="prism-player"></div>-->
            <!--</el-tab-pane>-->
        </el-tabs>
    </div>

</template>

<script>
    import {
        getAllOnlineDevicesByAppUser,
    } from "@/api/user/device"
    import {
        getPushLiveUrlByDevice,
        getLiveUrlByDevice,
        sendCmdToDevice
    } from "@/api/device"
    export default {
        name: "RealTimeData",
        data() {
            return {
              activeName: 'common',
              liveSource: '',
              liveData: [],
              cur_page: 0,
              page_size: 20,
              total: 0,
              deviceData: [],
              sdkKey: '',
              startCmdInfo: {
                deviceId: '',
                payload: {
                  start: ''
                }
              },
              stopCmdInfo: {
                deviceId: '',
                payload: {
                  stop: ''
                }
              },
              // 后期可以考虑从配置文件里读取连接地址
              client: new Paho.MQTT.Client('114.116.245.20', 8083, ''), // 第三个参数是clientID可以为空


              // niji
              playerOptions: {
                width: "800",
                height: "400",
                language: 'zh-CN',
                techOrder: ['flash'],
                muted: true,
                autoplay: true,
                controls: false,//不显示暂停、声音、进度条组件
                loop: true,
                sources: [{
                  type: 'rtmp/mp4',
                  src: 'rtmp://mobliestream.c3tv.com:554/live/goodtv.sdp' //网络rtmp流地址
                  //src: 'rtmp://127.0.0.1:8822/live/badao' //本地rtmp流地址
                }],
              }


            }
        },
        created() {
            getAllOnlineDevicesByAppUser(this.cur_page, this.page_size).then((res) => {
                this.total = res.data.totalElements;
                this.deviceData = res.data.data;
                this.liveData = res.data.liveData
                //console.log(res.data.liveData)
                this.connectEmq();
            })
        },
        mounted() {
            let player = new Aliplayer({
                id: 'J_prismPlayer',
                width: '100%',
                height: '600px',
                autoplay: true,
                // vid : '',
                // playauth : '',
                showBarTime: 1000,
                useFlashPrism: true
            });
            let player1 = new Aliplayer({
                id: 'a_prismPlayer',
                width: '100%',
                height: '600px',
                autoplay: true,
                prismType: 2,
                // vid : '',
                // playauth : '',
                showBarTime: 1000,
                // 调试
                source: "rtmp://127.0.0.1/live/stream", //视频地址
                useFlashPrism: true
            });
        },
        destroyed() {
            this.disConnect();
        },
        methods: {
            handleSelect(key, keyPath) {
                getLiveUrlByDevice(key).then((res) => {
                    this.liveSource = res.data.liveUrl
                    console.log(this.liveSource)
                    let player = new Aliplayer({
                        id: 'J_prismPlayer',
                        width: '100%',
                        height: '600px',
                        autoplay: true,
                        prismType: 2,
                        // vid : '',
                        // playauth : '',
                        showBarTime: 1000,
                        source: this.liveSource, //视频地址
                        useFlashPrism: true
                    });
                })
            },
            handleCurrentChange(val) {
                this.cur_page = val - 1;
                this.getData();
            },
            getData() {
                getAllOnlineDevicesByAppUser(this.cur_page, this.page_size).then((res) => {
                    this.total = res.data.totalElements;
                    this.deviceData = res.data.data;
                })
            },
            startLive(deviceId) {
                getPushLiveUrlByDevice(deviceId).then((res) => {
                    this.startCmdInfo.deviceId = deviceId
                    this.startCmdInfo.payload.start = res.data.pushLiveUrl
                    sendCmdToDevice(this.startCmdInfo)
                })
            },
            stopPushLive(deviceId) {
                this.stopCmdInfo.deviceId = deviceId
                this.stopCmdInfo.payload.stop = deviceId
                sendCmdToDevice(this.stopCmdInfo)
            },
            connectEmq() {
                this.client.connect({
                    userName: "websocket_client",
                    password: "websocket_client",
                    onSuccess: this.onConnect,
                }); // 连接服务器并注册连接成功处理事件
                this.client.onConnectionLost = this.onConnectionLost; // 注册连接断开处理事件
                this.client.onMessageArrived = this.onMessageArrived; // 注册消息接收处理事件

            },
            onConnectionLost(responseObject) {
                if (responseObject.errorCode !== 0) {
                    console.log('onConnectionLost:' + responseObject.errorMessage);
                    console.log('连接已断开');
                }
            },
            onMessageArrived(message) {
                //console.log(message.destinationName)
                this.deviceData.find((value, index, arr) => {
                    console.log(value);
                    console.log(message);
                    var unit=" ";
                    if (value.topic === message.destinationName) {
                    // if (value.topic.indexOf("MIN")!=-1) {
                        //todo 不要前端判断 数据表加个单位字段直接拼

                        // if(value.type === "RAIN") {
                        //     if(parseInt(message.payloadString)>100) {
                        //         value.value  = "晴"
                        //     }
                        //     else{
                        //         value.value  = "雨"
                        //     }
                        // }else{
                            if (value.type === "BODYTEMP") {
                                unit = "°C"
                            }
                            if(value.type === "PRESSUREHIGH") {
                                unit = "mmHg"
                            }
                            if(value.type === "PRESSURELOW") {
                                unit = "mmHg"
                            }
                            if(value.type === "HEARTRATE") {
                                unit = "次/分钟"
                            }
                            if(value.type === "SPEED") {
                                unit = "km/h"
                            }
                            value.value = message.payloadString + unit
                        // }

                    }
                })
                console.log("收到消息:" + message.payloadString);
            },
            onConnect() {
                console.log('onConnected');
                // 订阅主题
                this.deviceData.forEach((item, index) => {
                    if (item.type === "LIVE") {
                        item.value = "Camera监控中"
                    } else {
                        this.client.subscribe(item.topic);
                    }
                    // console.log(item.topic)
                });
            },
            disConnect() {
                console.log('disConnect');
                this.client.disconnect();
            },
        }
    }
</script>

<style scoped>
    .demo-color-box {
        border-radius: 4px;
        padding: 20px;
        margin: 5px 5px;
        height: 120px;
        box-sizing: border-box;
        color: #fff;
        font-size: 40px;
    }

    .bg-blue {
        background-color: #409eff;
        padding-top: 32px;
    }

    /* .stop-push {} */
</style>
