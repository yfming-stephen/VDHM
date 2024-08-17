<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-date"></i> 车辆</el-breadcrumb-item>
                <el-breadcrumb-item>地图</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div>
            <div ref="map" class="map"></div>
        </div>
    </div>
</template>

<script>
    import Echarts from "echarts";

    import {
        getAllDeviceLocationByUser,
        getAdeviceLocation
    } from '@/api/device/location';
    import {
        getCurrentUserInfo
    } from '@/api/user';

    var countsum;
    export default {

        data() {
            return {

                i:0,
                user: {},
                deviceInfo:[{
                    deviceId:1,
                    disable:false
                }],
                client: new Paho.MQTT.Client('114.116.245.20', 8083, ''),
                messageCount:1,
                countsum:0,
                chart: null,
                option: {
                    tooltip: {},
                    geo: {
                        map: 'china',
                        roam: true,
                        label: {
                            normal: {
                                show: true,
                                textStyle: {
                                    color: 'rgba(0,0,0,0.4)'
                                }
                            }
                        },
                        itemStyle: {
                            normal: {
                                borderColor: 'rgba(0, 0, 0, 0.2)'
                            },
                            emphasis: {
                                areaColor: null,
                                shadowOffsetX: 0,
                                shadowOffsetY: 0,
                                shadowBlur: 20,
                                borderWidth: 0,
                                shadowColor: 'rgba(100, 0, 100, 0.5)'
                            }
                        }
                    },
                    series: [{
                        type: 'scatter',
                        coordinateSystem: 'geo',
                        // type: 'effectScatter',
                        data:[],
                        symbolSize: 10,
                        symbolRotate: 55,
                        label: {
                            normal: {
                                formatter: '{b}',
                                position: 'right',
                                show: false
                            },
                            emphasis: {
                                show: true
                            }
                        },
                        itemStyle: {
                            normal: {
                                color: '#F06C00'
                            }
                        }
                    },
                        {
                            // type: 'scatter',
                            coordinateSystem: 'geo',
                            type: 'effectScatter',
                            data:[],
                            symbolSize: 15,
                            symbolRotate: 55,
                            label: {
                                normal: {
                                    formatter: '{b}',
                                    position: 'right',
                                    show: false
                                },
                                emphasis: {
                                    show: true
                                }
                            },
                            itemStyle: {
                                normal: {
                                    color: '#F06C00'
                                }
                            }
                        }

                    ]
                },

            };
        },
        created() {
            getCurrentUserInfo().then((data) => {
                this.user = data.data;
                this.connectEmq();
            })
        },
        destroyed() {
            this.disConnect();
        },
        mounted() {
            getAllDeviceLocationByUser().then((res) => {
                this.option.series[0].data=res.data;
                // console.log(this.option.series[0].data[0].name);
                // console.log(res.data);
                this.chart = Echarts.init(this.$refs.map);
                this.chart.showLoading();
                let mapJson = require('@/map/china.json');
                Echarts.registerMap("china", mapJson);
                this.chart.hideLoading();
                this.chart.setOption(this.option);
            });





        },
          methods: {

              warn(meCount) {
                let self=this;

                // console.log(this.deviceInfo);
                console.log("a");
                getAllDeviceLocationByUser().then((res) => {

                    this.option.series[0].data = res.data;
                    // console.log(this.option.series[0].data[0].name);
                    // console.log(res.data);
                    this.chart = Echarts.init(this.$refs.map);
                    this.chart.showLoading();
                    let mapJson = require('@/map/china.json');
                    Echarts.registerMap("china", mapJson);
                    this.chart.hideLoading();
                    console.log(meCount);
                    getAdeviceLocation(this.deviceInfo[meCount].deviceId).then((res) => {
                        // console.log(this.countsum);
                        // console.log(this.countsum);
                        this.option.series[1].data[this.countsum] = res.data[0];
                        console.log(this.option.series[1].data);
                        // console.log(res.data);
                        // this.chart = Echarts.init(this.$refs.map);
                        this.chart.showLoading();
                        Echarts.registerMap("china", mapJson);
                        this.chart.setOption(this.option);
                        this.chart.hideLoading();
                        this.countsum++;
                    })
                })
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
                //console.log(message.destinationName.split('/')[4])
                //console.log("收到消息:" + message.payloadString);

                // console.log(this.messageCount);
                // console.log(this.deviceInfo[this.messageCount].disable);

                    var flag=0;
                    var collection={
                      deviceId:message.destinationName.split('/')[4],
                      disable:false
                    };
                    for (var i=0;i<this.deviceInfo.length;i++){
                        if (this.deviceInfo[i].deviceId===message.destinationName.split('/')[4]){
                        flag++;
                        }

                    }
                    if (flag===0) {
                        this.deviceInfo.push(collection);
                        // console.log(this.messageCount);
                        this.warn(this.messageCount);
                        this.messageCount++;
                        // console.log(this.deviceInfo);
                }


                    // console.log(this.deviceInfo[this.messageCount].disable);
                    // this.deviceInfo[messageCount].disable=true;

                    // this.messageCount++;







            },
            onConnect() {
                console.log('onConnected');
                // 订阅主题
                this.client.subscribe("WARNING/DEVICE/" + this.user.id + "/+/+/#");
            },
            disConnect() {
                console.log('disConnect');
                this.client.disconnect();
            }
        }
    }
</script>

<style scoped>
    .map {
        width: 100%;
        height: 800px;
    }
</style>
