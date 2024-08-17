<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i>群组</el-breadcrumb-item>
                <el-breadcrumb-item>管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>


        <div class="handle-box">
            <el-row>
                <el-col :span="5">
                    <el-button type="primary" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>
                </el-col>
                <el-col :span="14">
                    <el-input v-model="select_word" placeholder="设备信息" class="handle-input mr10"></el-input>
                    <el-button type="primary" icon="search" @click="search">搜索</el-button>
                </el-col>
                <el-col :span="5">
                    <el-button style="float: right" type="default" icon="el-icon-plus" @click="dialogCreateFormVisible = true">新建</el-button>
                </el-col>
            </el-row>
        </div>

        <el-table :data="data" border style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="45"></el-table-column>
            <el-table-column prop="id" label="组ID" sortable width="140">
            </el-table-column>
            <el-table-column prop="name" label="名称" width="140">
            </el-table-column>
            <el-table-column prop="user" label="归属用户" width="160">
            </el-table-column>
            <el-table-column prop="comment" label="备注" width="170">
            </el-table-column>
            <el-table-column label="操作">
                <template scope="scope">
                    <el-button size="small"
                               @click="broadCast(scope)">分组广播</el-button>
                    <el-button size="small"
                               @click="handleEdit(scope)">编辑</el-button>
                    <el-button size="small" type="danger"
                               @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog
                title="提示"
                :visible.sync="dialogVisible"
                width="30%">
            <el-form :model="numberValidateForm" ref="numberValidateForm" label-width="100px" class="demo-ruleForm">
                <el-form-item
                        label="广播"
                        :rules="[
      { required: true, message: '内容不能为空'},
    ]"
                >
                    <el-input type="age" v-model.number="numberValidateForm.age" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('numberValidateForm')">提交</el-button>
                    <el-button @click="resetForm('numberValidateForm')">重置</el-button>
                </el-form-item>
            </el-form>

        </el-dialog>


        <!-- 新建群组 -->
        <el-dialog  title="新建群组" :visible.sync="dialogCreateFormVisible">
            <el-form :model="createForm">
                <el-form-item label="分组名">
                    <el-input v-model="createForm.groupName" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="createForm.comment" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogCreateFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="createGroup()">确 定</el-button>
            </div>
        </el-dialog>

        <!-- 编辑群组 -->
        <el-dialog  title="编辑群组" :visible.sync="dialogEditFormVisible" >
            <el-form :model="editable">
                <el-form-item label="群组ID">
                    <el-input v-model="editable.groupId" :disabled="true" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="分组名">
                    <el-input v-model="editable.groupName" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="editable.comment" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogEditFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editGroup()">确 定</el-button>
            </div>
        </el-dialog>

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
    import {sendCmdToGroup,createAGroup,updateGroup} from "@/api/user/group"
    import {delAGroup} from "@/api/admin/group"
    import {getAllGroupsByPage} from "@/api/admin/group"


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
                createForm: {
                    groupName: 'GROUP0001',
                    comment: '备注信息',
                },
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
                getAllGroupsByPage(this.cur_page,this.page_size).then((res) => {
                    console.log(res);
                    this.total = res.data.length;
                    this.tableData = res.data;

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
                // console.log(row.id);
                delAGroup(row.id).then((res) =>{
                    this.$message.success('您已经成功删除'+row.name);
                    this.getData();
                })
            },
            delAll(){
                const self = this,
                    length = self.multipleSelection.length;
                let str = '';
                self.del_list = self.del_list.concat(self.multipleSelection);

                for (let i = 0; i < length; i++) {
                    str += self.multipleSelection[i].name + ' ';
                    delAGroup(self.multipleSelection[i].id).then((res) =>{
                        self.getData();
                    });
                }
                self.$message.error('您已经成功删除了'+str);
                self.multipleSelection = [];
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {

                        this.sendCmdToGroupInfo.groupId=this.temp_scope.row.id;
                        this.sendCmdToGroupInfo.payload.message=this.numberValidateForm.age;


                        sendCmdToGroup(this.sendCmdToGroupInfo).then((res) => {
                            alert('提交成功');
                            this.dialogVisible=false;
                        })
                    } else {
                        console.log('内容不能为空!');
                        return false;
                    }
                });
            },
            broadCast(scope){
                this.dialogVisible=true;
                this.temp_scope=scope;
            },
            createGroup(){
                createAGroup(this.createForm).then((res) => {
                    this.getData();
                    this.dialogCreateFormVisible=false;
                });
            },
            editGroup(){
                updateGroup(this.editable).then((res) => {
                    this.getData();
                    this.dialogEditFormVisible=false;
                    this.$message.success("成功编辑群组"+this.editable.groupId);
                });
            }

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
