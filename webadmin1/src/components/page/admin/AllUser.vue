<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i>用户</el-breadcrumb-item>
                <el-breadcrumb-item>管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>


        <div class="handle-box">
            <el-row>
                <el-col :span="14">
                    <el-input v-model="select_word" placeholder="用户信息" class="handle-input mr10"></el-input>
                    <el-button type="primary" icon="search" @click="search">搜索</el-button>
                </el-col>
            </el-row>
        </div>

        <el-table :data="data" border style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="45"></el-table-column>
            <el-table-column prop="id" label="用户ID" sortable width="140">
            </el-table-column>
            <el-table-column prop="username" label="用户名称" width="130">
            </el-table-column>
            <el-table-column prop="role" label="角色" width="160">
            </el-table-column>
            <el-table-column prop="email" label="邮箱" width="170">
            </el-table-column>
            <el-table-column prop="phone" label="手机" width="140">
            </el-table-column>
            <el-table-column :formatter="stringFormat" prop="isEnable" label="激活状态" >
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
    import {getAllUsers} from "@/api/admin/user"



    export default {
        data() {
            return {
                url: './static/vuetable.json',
                dialogVisible: false,
                tableData: [],
                cur_page: 0,
                page_size:10,
                total:0,
                multipleSelection: [],
                select_cate: '',
                select_word: '',
                del_list: [],
                is_search: false,
                sendCmdToGroupInfo:{
                    groupId:'',
                    payload:{
                        message:''
                    },
                },
                input: '',
                numberValidateForm: {
                    age: ''
                },
                temp_scope:[],
                content:'',
                dialogCreateFormVisible:false,
                dialogEditFormVisible:false,
            }
        },
        created(){
            this.getData();
        },
        computed: {
            data(){
                const self = this;
                return self.tableData;
            }
        },
        methods: {
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            handleCurrentChange(val){
                this.cur_page = val - 1;
                this.getData();
            },
            getData(){
                getAllUsers(this.cur_page,this.page_size).then((res) => {
                    // console.log(res.data);
                    this.tableData = res.data.data;
                })
            },
            search(){
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
            stringFormat(row, column){
                var data = row[column.property];

                if(data){
                    return "激活"
                }else{
                    return "未激活"
                }
            },

        }
    }
</script>

<style scoped>
    .handle-box{
        margin-bottom: 20px;
    }
    .handle-select{
        width: 120px;
    }
    .handle-input{
        width: 300px;
        display: inline-block;
    }
</style>
