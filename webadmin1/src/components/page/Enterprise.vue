<template>
    <div class="enterprise">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i>商家</el-breadcrumb-item>
                <el-breadcrumb-item>数据</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-collapse v-for="groupData in this.data">
            <el-collapse-item :title="groupData.name+'  |  '+groupData.comment">
                <div>商家用户名:{{groupData.userName}}</div>
                <div>商家手机号:{{groupData.userPhone}}</div>
                <el-tabs>
                    <el-tab-pane v-for="group in groupData.deviceRecordData" :label="group.deviceType">
                        <el-table :data="group.monthData" border style="width: 100%">
                            <el-table-column  prop="month" label="月份"></el-table-column>
                            <el-table-column  prop="data" label="平均值"></el-table-column>
                        </el-table>
                    </el-tab-pane>
                </el-tabs>
                <el-row>
                </el-row>
            </el-collapse-item>
        </el-collapse>
        <div class="pagination">
            <el-pagination @current-change="handleCurrentChange" layout="prev, pager, next" :total="total">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import {
        getAllDeviceGroupeRecordDataByPage
    } from "@/api/enterprise"
    export default {
        data() {
            return {
                cur_page: 0,
                page_size: 10,
                total: 0,
                data: []
            }
        },
        created() {
            this.getData();
        },
        methods: {
            handleCurrentChange(val) {
                this.cur_page = val - 1;
                this.getData();
            },
            getData() {
                getAllDeviceGroupeRecordDataByPage(this.cur_page, this.page_size).then((res) => {
                    this.total = res.data.totalElements;
                    this.data = res.data;
                })
            },
        }
    }
</script>
