<template>
    <div class="dashboard">
        <el-row>
            <el-row>
                <el-col :span="8" style="padding-left: 10px; padding-right: 10px;">
                    <el-row>
                        <el-card class="box-card mgb20" shadow="hover">
                            <div class="user-info">
                                <img src="/api/avatar/avatar44.png" alt="" class="user-avator">
                                <div class="user-info-cont">
                                    <div class="user-info-name">{{user.username}}</div>
                                    <div>{{user.authorities}}</div>
                                </div>

                            </div>
                            <div class="user-info-list">Email：
                                <span>{{user.email}}</span>
                            </div>
                            <div class="user-info-list">电话：
                                <span>{{user.phone}}</span>
                            </div>
                        </el-card>


                        <el-card class="box-card" shadow="hover">
                            <div slot="header">
                                <span>WEB服务器详情</span>
                            </div>
                            <div>
                                <div class="user-info-list">os_name： {{this.system.os_name}}</div>
                                <div class="user-info-list">os_arch： {{this.system.os_arch}}</div>
                                <div class="user-info-list">freeMemory： {{this.system.freeMemory}}</div>
                                <div class="user-info-list">maxMemory： {{this.system.maxMemory}}</div>
                            </div>
                        </el-card>

                        <el-card class="box-card" shadow="hover" style="margin-top: 20px;">
                            <div slot="header">
                                <span>EMQ服务器详情</span>
                            </div>
                            <div>
                                <div class="user-info-list">版本： 2.3.5
                                </div>
                                <div class="user-info-list">信息： Erlang MQTT Broker
                                </div>
                                <div class="user-info-list">开机时间： 16 minutes, 57 seconds
                                </div>
                                <div class="user-info-list">当前状态： Running
                                </div>
                            </div>
                        </el-card>
                    </el-row>
                </el-col>
                <el-col :span="16" style="padding-left: 10px; padding-right: 10px;">
                    <el-row class="mgb20" style="margin-left: -10px; margin-right: -10px;">
                        <el-col :span="8" style="padding-left: 10px; padding-right: 10px;">
                            <el-card class="box-card" shadow="hover" body-style=" padding: 0px;">
                                <div class="grid-content grid-con-1">
                                    <i class="el-icon-setting grid-con-icon"></i>
                                    <div class="grid-cont-right">
                                        <div class="grid-num">{{this.state.total}}</div>
                                        <div>设备量</div>
                                    </div>
                                </div>
                            </el-card>
                        </el-col>
                        <el-col :span="8" style="padding-left: 10px; padding-right: 10px;">
                            <el-card class="box-card" shadow="hover" body-style=" padding: 0px;">
                                <div class="grid-content grid-con-2">
                                    <i class="el-icon-circle-check grid-con-icon"></i>
                                    <div class="grid-cont-right">
                                        <div class="grid-num">{{this.state.onLine}}</div>
                                        <div>在线数目</div>
                                    </div>
                                </div>
                            </el-card>
                        </el-col>
                        <el-col :span="8" style="padding-left: 10px; padding-right: 10px;">
                            <el-card class="box-card" shadow="hover" body-style=" padding: 0px;">
                                <div class="grid-content grid-con-3">
                                    <i class="el-icon-circle-close grid-con-icon"></i>
                                    <div class="grid-cont-right">
                                        <div class="grid-num">{{this.state.offLine}}</div>
                                        <div>离线数目</div>
                                    </div>
                                </div>
                            </el-card>
                        </el-col>
                    </el-row>
                    <el-card class="box-card" shadow="hover" style="margin-top: 20px;" body-style="height: 500px;">
                        <div slot="header">
                            <span>日志</span>
                        </div>
                        <el-table :data="tableData" border style="width: 100%;height: 480px;">
                            <el-table-column prop="date" label="日期" width="180">
                            </el-table-column>
                            <el-table-column prop="deviceId" label="设备" width="180">
                            </el-table-column>
                            <el-table-column prop="event" label="事件">
                            </el-table-column>
                        </el-table>
                        <el-pagination small @current-change="handleCurrentChange" layout="prev, pager, next" :total="this.total">
                        </el-pagination>
                    </el-card>
                </el-col>
            </el-row>

        </el-row>
    </div>
</template>

<script>
    import {
        getCurrentUserInfo,
        getCurrentState,
        updateUserInfo
    } from '@/api/user'
    import {
        getSystemInfo,
        getEmqInfo
    } from '@/api/server'
    import {
        getAllLog
    } from '@/api/device'
    export default {
        data: function() {
            return {
                user: {},
                system: {},
                emq: {},
                state: {},
                tableData: [],
                cur_page: 0,
                page_size: 10,
                total: 0,
                dialogTableVisible: false,
                dialogFormVisible: false,
            }
        },
        mounted() {
            getCurrentUserInfo().then((data) => {
                this.user = data.data


            })
            getCurrentState().then((data) => {
                this.state = data.data
            })
            getSystemInfo().then((data) => {
                this.system = data.data
            })

            this.getLog();

            // emq接口有问题 先把数据写成成默认的了
            /*
            getEmqInfo().then((data) => {
                this.emq = data.data
            })
            */
        },
        methods: {
            handleEdit(){
                updateUserInfo(this.form).then((res) => {
                    this.dialogFormVisible = false
                })
            },
            handleCurrentChange(val) {
                this.cur_page = val - 1;
                this.getLog();
            },
            getLog() {
                getAllLog(this.cur_page, this.page_size).then((res) => {
                    this.total = res.data.totalElements;
                    this.tableData = res.data.data;
                })
            }
        }
    }
</script>

<style scoped>
    .user-info {
        display: -ms-flexbox;
        display: flex;
        -ms-flex-align: center;
        align-items: center;
        padding-bottom: 20px;
        margin-bottom: 20px;
        border-bottom: 2px solid #ccc;
    }

    .user-avator {
        width: 120px;
        height: 120px;
        border-radius: 50%
    }

    .user-info-cont {
        -ms-flex: 1;
        flex: 1;
        color: #999;
        font-size: 14px
    }

    .user-info-cont div:first-child {
        color: #222;
        font-size: 30px
    }

    .user-info-list {
        color: #999;
        font-size: 14px;
        line-height: 25px
    }

    .user-info-list span {
        margin-left: 20px
    }

    .grid-content {
        display: -ms-flexbox;
        display: flex;
        -ms-flex-align: center;
        align-items: center;
        height: 100px
    }

    .grid-cont-right {
        -ms-flex: 1;
        flex: 1;
        color: #999;
        font-size: 12px;
        text-align: center
    }

    .grid-num {
        font-size: 30px;
        font-weight: 700
    }

    .grid-con-icon {
        width: 100px;
        height: 100px;
        color: #fff;
        font-size: 50px;
        line-height: 100px;
        text-align: center
    }

    .grid-con-1 .grid-con-icon {
        background: #2d8cf0
    }

    .grid-con-1 .grid-num {
        color: #2d8cf0
    }

    .grid-con-2 .grid-con-icon {
        background: #64d572
    }

    .grid-con-2 .grid-num {
        color: #2d8cf0
    }

    .grid-con-3 .grid-con-icon {
        background: #f25e43
    }

    .grid-con-3 .grid-num {
        color: #f25e43
    }

    .mgb20 {
        margin-bottom: 20px;
    }
</style>
