<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i> 日志</el-breadcrumb-item>
                <el-breadcrumb-item>日志记录</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-tabs>
            <el-tab-pane label="用户操作">
                <el-table :data="dailyData" border style="width: 100%">
                    <el-table-column prop="date" label="日期" width="300">
                    </el-table-column>
                    <el-table-column prop="who" label="用户">
                    </el-table-column>
                    <el-table-column prop="event" label="事件">
                    </el-table-column>
                    <el-table-column prop="operate" label="结果">
                    </el-table-column>
                </el-table>
                <div class="pagination">
                    <el-pagination @current-change="handleCurrentChangeDaily" layout="prev, pager, next" :total="this.daily_total">
                    </el-pagination>
                </div>
            </el-tab-pane>
            <el-tab-pane label="设备连接">
                <el-table :data="onOffData" border style="width: 100%">
                    <el-table-column prop="date" label="日期">
                    </el-table-column>
                    <el-table-column prop="deviceId" label="设备">
                    </el-table-column>
                    <el-table-column prop="event" label="事件">
                    </el-table-column>
                </el-table>
                <div class="pagination">
                    <el-pagination @current-change="handleCurrentChangeOnOff" layout="prev, pager, next" :total="this.onoff_total">
                    </el-pagination>
                </div>
            </el-tab-pane>
            <el-tab-pane label="设备通讯">
                <el-table :data="deviceData" border style="width: 100%">
                    <el-table-column prop="createTime" label="日期" width="300">
                    </el-table-column>
                    <el-table-column prop="deviceId" label="设备ID">
                    </el-table-column>
                    <el-table-column prop="data" label="消息" width="300">
                    </el-table-column>
                    <el-table-column prop="type" label="类型">
                    </el-table-column>
                </el-table>
                <div class="pagination">
                    <el-pagination @current-change="handleCurrentChangeDevice" layout="prev, pager, next" :total="this.device_total">
                    </el-pagination>
                </div>
            </el-tab-pane>
        </el-tabs>
    
    </div>
</template>

<script>
    import {
        getAllLog
    } from '@/api/device'
    import {
        getAllDailyLogByPage, getAllDeviceDataByPage
    } from '@/api/admin/log'
    export default {
        name: "AllLog",
        data() {
            return {
                onOffData: [],
                dailyData: [],
                deviceData: [],
                onoff_cur_page: 0,
                daily_cur_page: 0,
                device_cur_page: 0,
                page_size: 10,
                onoff_total: 0,
                daily_total: 0,
                device_total: 0
            }
        },
        mounted() {
            this.getDailyLog();
            this.getOnOffLog();
            this.getDeviceLog();
        },
        methods: {
            handleCurrentChangeOnOff(val) {
                this.onoff_cur_page = val - 1;
                this.getOnOffLog();
            },
            handleCurrentChangeDaily(val) {
                this.daily_cur_page = val - 1;
                this.getDailyLog();
            },
            handleCurrentChangeDevice(val) {
                this.device_cur_page = val - 1;
                this.getDeviceLog();
            },
            getOnOffLog() {
                getAllLog(this.onoff_cur_page, this.page_size).then((res) => {
                    this.onoff_total = res.data.totalElements;
                    this.onOffData = res.data.data;
                })
            },
            getDailyLog() {
                getAllDailyLogByPage(this.daily_cur_page, this.page_size).then((res) => {
                    this.daily_total = res.data.totalElements;
                    this.dailyData = res.data.data;
                    console.log(res.data.data[0].date)
                })
            },
            getDeviceLog() {
                getAllDeviceDataByPage(this.device_cur_page, this.page_size).then((res) => {
                    this.device_total = res.data.totalElements;
                    this.deviceData = res.data.data;
                })
            }
        }
    }
</script>

<style scoped>
    
</style>