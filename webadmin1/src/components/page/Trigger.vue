<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i>车载触发器</el-breadcrumb-item>
                <el-breadcrumb-item>管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>


        <div class="handle-box">
            <el-row>
                <el-col :span="5">
                    <el-button type="primary" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>
                </el-col>
                <el-col :span="14">
                    <el-input v-model="select_word" placeholder="触发器信息" class="handle-input mr10"></el-input>
                    <el-button type="primary" icon="search" @click="search">搜索</el-button>
                </el-col>
                <el-col :span="5">
                    <el-button style="float: right" type="default" icon="el-icon-plus" @click="dialogCreateFormVisible = true">新建</el-button>
                </el-col>
            </el-row>
        </div>

        <el-table :data="data" border style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="45"></el-table-column>
            <el-table-column prop="deviceScopeId" label="ID" sortable width="140">
            </el-table-column>
            <el-table-column prop="deviceId" label="设备ID" sortable width="140">
            </el-table-column>
            <el-table-column prop="name" label="名称" width="120">
            </el-table-column>
            <el-table-column prop="info" label="信息">
            </el-table-column>
            <el-table-column label="操作">
                <template scope="scope">
                        <el-switch on-text="" off-text="" v-model="scope.row.isStart" @change="changeScope(scope.row)"></el-switch>
                        <el-button size="small"
                                   @click="handleEdit(scope)">编辑</el-button>
                        <el-button size="small" type="danger"
                                   @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>


        <!-- 新建 -->
        <el-dialog  title="新建触发器" :visible.sync="dialogCreateFormVisible">
            <el-form :model="createForm">
                <el-form-item label="设备">
                    <br>
                    <el-select v-model="createForm.deviceId" placeholder="请选择" size="medium">
                        <el-option
                            v-for="item in this.devices"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="名称">
                    <el-input v-model="createForm.name" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="信息">
                    <el-input v-model="createForm.info" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="触发下限">
                    <el-input v-model="createForm.lowerValue" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="触发上限">
                    <el-input v-model="createForm.upperValue" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="触发下限2">
                  <el-input v-model="createForm.lowerValue1" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="触发上限2">
                  <el-input v-model="createForm.upperValue1" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="payload">
                    <el-input v-model="createForm.payload" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogCreateFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="createTrigger()">确 定</el-button>
            </div>
        </el-dialog>

        <!-- 编辑 -->
        <el-dialog  title="编辑触发器" :visible.sync="dialogEditFormVisible" >
            <el-form :model="editable">
                <el-form-item label="ID">
                    <el-input v-model="editable.deviceScopeId" :disabled="true" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="设备ID">
                    <el-input v-model="editable.deviceId" :disabled="true" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="名称">
                    <el-input v-model="editable.name" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="信息">
                    <el-input v-model="editable.info" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="触发下限">
                    <el-input v-model="editable.lowerValue" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="触发上限">
                    <el-input v-model="editable.upperValue" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="触发下限2">
                  <el-input v-model="createForm.lowerValue1" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="触发上限2">
                  <el-input v-model="createForm.upperValue1" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="payload">
                    <el-input v-model="editable.payload" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogEditFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editTrigger()">确 定</el-button>
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
    import {
        getAllGroupsByPage,
        sendCmdToGroup,
        createAGroup,
        updateGroup
    } from "@/api/user/group"
    import {
        delAGroup
    } from "@/api/admin/group"
    import {getAllDevices} from '@/api/user/device';
    import {
        getTriggers,
        delTrigger,
        updateTriggerSetting,
        updateTriggerStatus,
        addTrigger
    } from "@/api/device"
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
                sendCmdToGroupInfo: {
                    groupId: '',
                    payload: {
                        message: ''
                    },
                },
                input: '',
                numberValidateForm: {
                    age: ''
                },
                temp_scope: [],
                content: '',
                createForm: {
                    deviceId:'',
                    name: 'test',
                    info: 'test',
                    payload: 'warning',
                    upperValue:'50',
                    lowerValue:'1',
                    upperValue1:'20',
                    lowerValue1:'10'
                },
                dialogCreateFormVisible: false,
                dialogEditFormVisible: false,
                editable: {
                    deviceScopeId:'',
                    deviceId:'',
                    name: '',
                    info: '',
                    payload: '',
                    upperValue:'',
                    lowerValue:'',
                  upperValue1:'',
                  lowerValue1:''
                },
                devices:{}
            }
        },
        created() {
            this.getData();
            this.getDevices();
        },
        computed: {
            data() {
                const self = this;
                return self.tableData;
            },
        },

        methods: {
            getDevices() {
                getAllDevices(0, 10000).then((res) => {
                    this.devices=res.data.data;
                    // console.log(res.data.data);
                })
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            handleCurrentChange(val) {
                this.cur_page = val - 1;
                this.getData();
            },
            getData() {
                getTriggers(this.cur_page, this.page_size).then((res) => {
                    this.total = res.data.totalElements;
                    this.tableData = res.data.data;
                    console.log(this.tableData)
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
            handleEdit(scope) {
                this.dialogEditFormVisible = true;
                this.editable=scope.row

            },
            handleDelete(index, row) {
                delTrigger(row.deviceScopeId).then((res) => {
                    this.$message.success('您已经成功删除' + row.name);
                    this.getData();
                })
            },
            delAll() {
                const self = this,
                    length = self.multipleSelection.length;
                let str = '';
                self.del_list = self.del_list.concat(self.multipleSelection);

                for (let i = 0; i < length; i++) {
                    str += self.multipleSelection[i].name + ' ';
                    delTrigger(self.multipleSelection[i].deviceScopeId).then((res) => {
                        self.getData();
                    });
                }
                self.$message.error('您已经成功删除了' + str);
                self.multipleSelection = [];
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {

                        this.sendCmdToGroupInfo.groupId = this.temp_scope.row.id;
                        this.sendCmdToGroupInfo.payload.message = this.numberValidateForm.age;


                        sendCmdToGroup(this.sendCmdToGroupInfo).then((res) => {
                            alert('提交成功');
                            this.dialogVisible = false;
                        })
                    } else {
                        console.log('内容不能为空!');
                        return false;
                    }
                });
            },
            changeScope(row){
                updateTriggerStatus(row.deviceScopeId).then((res) => {
                    this.getData();
                });
            },
            broadCast(scope) {
                this.dialogVisible = true;
                this.temp_scope = scope;
            },
            createTrigger() {
                addTrigger(this.createForm).then((res) => {
                    this.getData();
                    this.dialogCreateFormVisible = false;
                });
            },
            editTrigger() {
                updateTriggerSetting(this.editable).then((res) => {
                    this.getData();
                    this.dialogEditFormVisible = false;
                    this.$message.success("成功编辑触发器" + this.editable.deviceScopeId);
                });
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
