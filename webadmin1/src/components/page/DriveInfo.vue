<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i>行驶</el-breadcrumb-item>
                <el-breadcrumb-item>信息</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
    
    
        <div class="handle-box">
            <!-- <el-row>
                    <el-col :span="14">
                        <el-input v-model="select_word" placeholder="todo" class="handle-input mr10"></el-input>
                        <el-button type="primary" icon="search" @click="search">搜索</el-button>
                    </el-col>
                </el-row> -->
        </div>
    
        <el-table :data="data" border style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="45"></el-table-column>
            <el-table-column prop="hash" label="hash">
            </el-table-column>
            <el-table-column label="开始时间">
                <template scope="scope">
                        {{scope.row.createTime| formatDate}}
                </template>
                        </el-table-column>
                        <el-table-column prop="distance" label="行驶距离(km)">
                        </el-table-column>
                        <el-table-column label="操作">
            <template scope="scope">
                <!-- this.$router.push('/dashboard') -->
                <el-button size="small" type="success" @click="jumpToDriveRecord(scope.row.hash)">数据记录</el-button>
            </template>
            </el-table-column>
        </el-table>




        <div class="pagination">
            <el-pagination
                    @current-change ="handleCurrentChange"
                    layout="prev, pager, next"
                    :total="total">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import {
        getDriveInfoByGroupIdAndPage
    } from "@/api/drive"
    
    
    
    export default {
        data() {
            return {
                dialogVisible: false,
                tableData: [],
                cur_page: 0,
                page_size: 10,
                total: 0,
                multipleSelection: [],
                select_cate: '',
                select_word: '',
                del_list: [],
                is_search: false,
                input: '',
                content: '',
                dialogCreateFormVisible: false,
                dialogEditFormVisible: false,
                groupId: ''
            }
        },
        // created(){
        //     this.getData();
        // },
        mounted() {
            var id = this.$route.params.groupId;
            this.groupId = this.$route.params.groupId;
            this.getData()
        },
        computed: {
            data() {
                const self = this;
                return self.tableData;
            }
        },
    
        filters: {
            formatDate: function(value) {
                var date = new Date(value); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
                    
                var Y = date.getFullYear() + '-';    
                var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';    
                var D = date.getDate() + ' ';    
                var h = date.getHours() + ':';    
                var m = date.getMinutes() + ':';    
                var s = date.getSeconds();    
                return Y + M + D + h + m + s;
            }
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
                getDriveInfoByGroupIdAndPage(this.groupId, this.cur_page, this.page_size).then((res) => {
                    console.log(res.data);
                    this.total = res.data.totalElements;
                    this.tableData = res.data.data;
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
    .handle-box {
        margin-bottom: 20px;
    }
    
    .handle-select {
        width: 120px;
    }
    
    .handle-input {
        width: 300px;
        display: inline-block;
    }
</style>
