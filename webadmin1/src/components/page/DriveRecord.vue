<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i>行驶数据</el-breadcrumb-item>
                <el-breadcrumb-item>记录</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <span>行驶相关</span>
            </div>
            <el-row>
                <el-col :span="10" :offset="1">
                    <ve-line :data="speedData" :mark-line="markLine" :mark-point="markPoint"></ve-line>
                </el-col>
                <el-col :span="10" :offset="1">
                    <ve-line :data="tempData" :grid="grid" :visual-map="tempMap"></ve-line>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10" :offset="1">
                    <ve-line :data="heartData" :colors="colors2"></ve-line>
                </el-col>
                <el-col :span="10" :offset="1">
                    <ve-line :data="pressureData" :settings="chartSettings" :colors="colors"></ve-line>
                </el-col>
            </el-row>
        </el-card>
        <br>
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <span>行驶轨迹</span>
            </div>
            <el-row>
                <div class="amap-page-container">
                <el-amap vid="amap" :zoom="zoom" :center="center" class="amap-demo">
                    <el-amap-polyline :path="polyline.path"></el-amap-polyline>
                </el-amap>
                </div>
    
            </el-row>
        </el-card>
    </div>
</template>

<script>
    import {
        getRecordByHash
    } from "@/api/drive"
    import 'echarts/lib/component/visualMap'
    import 'echarts/lib/component/markLine'
    import 'echarts/lib/component/markPoint'
    
    export default {
        data() {
    
            this.tempMap = [{
                type: 'piecewise',
                splitNumber: 5,
                min: 0,
                max: 50,
                right: 0,
                top: '50%'
            }]
            this.markLine = {
                data: [{
                    name: '平均线',
                    type: 'average'
                }]
            }
            this.markPoint = {
                data: [{
                    name: '最大值',
                    type: 'max'
                }]
            }
            this.chartSettings = {
                area: true
            }
            this.grid = {
                right: 60
            }
            this.colors = ['#8ec9f2', '#c4ccd3']
            this.colors2 = ['#c23531']
    
            return {
                zoom: 12,
                center:[121.5389385, 31.21515044],
                polyline: {
                    path: [
                        // [121.5389385, 31.21515044], [121.5389385, 31.29615044], [121.5273285, 31.21515044]
                        ],
                    editable: false
                },
                speedData: {
                    columns: ['时间', '速度'],
                    rows: []
                },
                tempData: {
                    columns: ['时间', '体温'],
                    rows: []
                },
                heartData: {
                    columns: ['时间', '心率'],
                    rows: []
                },
                pressureData: {
                    columns: ['时间', '高压', '低压'],
                    rows: []
                },
                dialogVisible: false,
                record: {
                    drivingHealth: [],
                    drivingRecord: []
                },
                hash: '',
    
            }
        },
        mounted() {
            var hash = this.$route.params.hash;
            this.hash = this.$route.params.hash;
            this.getData()
        },
        methods: {
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            handleCurrentChange(val) {
                this.cur_page = val - 1;
                this.getData();
            },
            getData() {
                getRecordByHash(this.hash, this.cur_page, this.page_size).then((res) => {
                    // console.log(res.data);
                    this.record = res.data;
                    this.record.drivingRecord.forEach((item, index) => {
                        this.speedData.rows.push({
                            '时间': index,
                            '速度': item.speed
                        })
                        this.tempData.rows.push({
                            '时间': index,
                            '体温': item.bodyTemp
                        })
                        // polyline
                        this.polyline.path.push([item.gps.longitude,item.gps.latitude])
                        if (index==0){
                            this.center=[Number(item.gps.longitude),Number(item.gps.latitude)]
                        }
                    });
                    // console.log(this.center)
                    this.record.drivingHealth.forEach((item, index) => {
                        this.heartData.rows.push({
                            '时间': index,
                            '心率': item.heartRate
                        })
                        this.pressureData.rows.push({
                            '时间': index,
                            '高压': item.pressure.high,
                            '低压': item.pressure.low
                        })
                    });
                })
            },
            search() {
                this.is_search = true;
            },
            formatter(row, column) {
                return row.address;
            },
            filterTag(value, row) {
                return row.tag === value;
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            jumpToDriveRecord(hash) {
                this.$router.push('/drive/record/' + hash);
            }
    
        }
    }
</script>

<style scoped>
    .page-tabbar-container {
        overflow: hidden
    }
    
    .back {
        /*background:url("/static/img/mback.jpg") no-repeat top left;*/
        background-size: 100% 100%;
        opacity: 0.9;
        position: absolute;
        width: 100%;
        height: 100%;
    }
    
    .amap-demo {
      height: 500px;
    }
</style>
