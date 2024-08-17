<template>
    <div class="wrapper">
        <audio src="/static/warning.ogg" controls="controls" preload id="music" hidden></audio>
        <v-head></v-head>
        <v-sidebar></v-sidebar>
        <div class="content">
            <tabs-view></tabs-view>
            <transition name="move" mode="out-in"><router-view></router-view></transition>
        </div>
        <div id="warningModel" class="alert shake shake-rotate" style="color: white; display: none;">
                    <div style="font-size: 21px;">
                       <a href="#/device" class="J_menuItem" style="color: white">请立即处理！</a>
                    </div>
                    <div>报警设备ID: {{deviceId}}</div>
                    <div>报警数值：{{value}}</div>
        </div>
    </div>
</template>

<script>
    import vHead from './Header.vue';
    import vSidebar from './Sidebar.vue';
    import TabsView from '@/components/TabsView'
    import {warning} from 'static/js/warning.js';
    import {
        getCurrentUserInfo
    } from '@/api/user';

    export default {
        components:{
            vHead, vSidebar, TabsView
        },
        data(){
            return{
                playFlag:false,
                user: {},
                client: new Paho.MQTT.Client('114.116.245.20', 8083, ''),
                deviceId:1,
                value:100,
            }
        },
        created() {
            getCurrentUserInfo().then((data) => {
                this.user = data.data
                this.connectEmq();
            })
        },
        destroyed() {
            this.disConnect();
        },
        methods:{
            warn(){
                warning()
            },
            connectEmq() {
                this.client.connect({
                    userName: "websocket_client",
                    password: "websocket_client",
                    onSuccess: this.onConnect
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
                console.log(message.destinationName.split('/')[4])
                console.log("收到消息:" + message.payloadString);
                this.value=message.payloadString;
                this.deviceId=message.destinationName.split('/')[4];
                this.playPause();
                this.warn();
            },
            onConnect() {
                console.log('onConnected');
                // 订阅主题
                this.client.subscribe("WARNING/DEVICE/"+this.user.id+"/+/+/#");
            },
            disConnect() {
                console.log('disConnect');
                this.client.disconnect();
            },
            playPause(){
                var audio=document.getElementById("music")
                    audio.play();
            },
        }
    }
</script>
<style>
@import "../../../static/css/shake.css";
.alert {
    position: fixed;
    width: 220px;
    height: 95px;
    right: 2%;
    bottom: 1%;
    background-color: red;
    text-align: center;
    padding: 15px;
    margin-bottom: 20px;
    border: 1px solid transparent;
    border-radius: 4px;
}



</style>
