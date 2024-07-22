<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i>个人信息</el-breadcrumb-item>
                <el-breadcrumb-item>修改</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <el-form :model="form" ref="form" label-width="100px">
            <el-form-item label="用户名" :label-width="formLabelWidth">
                <el-input v-model="form.username" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" :label-width="formLabelWidth">
                <el-input v-model="form.email" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="手机" :label-width="formLabelWidth">
                <el-input v-model="form.phone" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="备用手机" :label-width="formLabelWidth">
                <el-input v-model="form.alternatePhone" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="旧密码" :label-width="formLabelWidth">
                <el-input type="password" v-model="form.oldPassword" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="新密码" :label-width="formLabelWidth">
                <el-input type="password" v-model="form.newPassword" autocomplete="off"></el-input>
            </el-form-item>


        <el-form-item style="float: right">
            <el-button type="primary" @click="submitForm()">提交</el-button>
        </el-form-item>
        </el-form>






    </div>
</template>

<script>
    import { Message } from 'element-ui';
    import {
        getCurrentUserInfo,
        getCurrentState,
        updateUserInfo
    } from '@/api/user'



    export default {
        data() {
            return {
                form: {
                    username: '',
                    oldPassword: '',
                    newPassword:'',
                    email: '',
                    phone: '',
                    alternatePhone:'',
                },
                formLabelWidth: '120px',
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
            getCurrentUserInfo().then((data) => {
                this.user = data.data
                this.form.username=data.data.username
                this.form.email=data.data.email
                this.form.phone=data.data.phone
                this.form.alternatePhone=data.data.alternatePhone

            })
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
            submitForm(){
                updateUserInfo(this.form).then((data) => {
                console.log(data);
                    if (data.state == 1) {
                        Message({
                            showClose: true,
                            message: data.message,
                            type: 'success'
                        },3000);

                    }



                })
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
