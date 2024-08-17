<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i>任务</el-breadcrumb-item>
                <el-breadcrumb-item>管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>


        <div class="handle-box">
            <el-row>
                <el-col :span="5">
                    <el-button type="primary" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>
                </el-col>
                <el-col :span="14">
                    <el-input v-model="select_word" placeholder="设备ID" class="handle-input mr10"></el-input>
                    <el-button type="primary" icon="search" @click="search">搜索</el-button>
                </el-col>
            </el-row>
        </div>

        <el-table :data="data" border style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="45"></el-table-column>
            <el-table-column prop="id" label="任务ID" sortable width="140">
            </el-table-column>
            <el-table-column prop="device" label="设备ID" width="140">
            </el-table-column>
            <el-table-column prop="jobJson" label="数据" width="200">
            </el-table-column>
            <el-table-column prop="cron" label="Cron" width="220">
            </el-table-column>
            <el-table-column prop="state" label="State" width="100">
            </el-table-column>
            <el-table-column label="操作" >
                <template scope="scope">
                    <el-button size="small"
                               @click="handleStop(scope.row)">操作</el-button>
                    <el-button size="small" type="danger"
                               @click="handleDelete(scope.$index, scope.row)">删除</el-button>
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
    import {getAllGroupsByPage,sendCmdToGroup,createAGroup,updateGroup} from "@/api/user/group"
    import {addJob,getAllJob,delAJob,StopAJob} from "@/api/user/device"
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
                editable: {
                    comment: '',
                    groupId:'',
                    groupName: '',
                },
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
                getAllJob(this.cur_page,this.page_size).then((res) => {

                    this.total = res.data.totalElements;
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
            handleEdit(scope) {
                this.dialogEditFormVisible=true;
                this.editable.groupId=scope.row.id;
                this.editable.comment=scope.row.comment;
                this.editable.groupName=scope.row.name;
            },
            handleDelete(index, row) {
                delAJob(row.device).then((res) =>{
                    this.$message.success('您已经成功删除任务号 '+row.id);
                    this.getData();
                })
            },
            handleStop(row) {
                console.log(row);
                StopAJob(row.device).then((res) =>{
                    this.$message.success(res.message+" 任务号为"+row.id);
                    this.getData();
                })
            },
            delAll(){
                const self = this,
                    length = self.multipleSelection.length;
                let str = '';
                self.del_list = self.del_list.concat(self.multipleSelection);

                for (let i = 0; i < length; i++) {
                    str += self.multipleSelection[i].device + ' ';
                    delAJob(self.multipleSelection[i].device).then((res) =>{
                        self.getData();
                    });
                }
                self.$message.error('您已经成功删除了'+str);
                self.multipleSelection = [];
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
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
