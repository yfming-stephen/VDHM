<template>
    <div class="login-wrap">
        <div class="ms-title">AIncar</div>
        <div class="ms-login">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="0px" class="demo-ruleForm">
                <el-form-item prop="loginParam">
                    <el-input v-model="ruleForm.loginParam" placeholder="账号"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" placeholder="密码" v-model="ruleForm.password" @keyup.enter.native="submitForm('ruleForm')"></el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
                </div>
                <router-link to="/register" ><el-button type="text">注册</el-button></router-link>
            </el-form>
        </div>
    </div>
</template>

<script>
    import md5 from 'js-md5';
    import { login, getCurrentUserInfo } from '@/api/user'
    import logo from '@/assets/logo.png'
    export default {
        data: function(){
            return {
                logo:logo,
                ruleForm: {
                    loginParam: '',
                    password: ''
                },
                rules: {
                    loginParam: [
                        { required: true, message: '请输入用户名', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' }
                    ]
                }
            }
        },
        methods: {
            submitForm(formName) {
                const self = this;
                self.$refs[formName].validate((valid) => {
                    if (valid) {
                        self.ruleForm.password=md5(self.ruleForm.password);
                        login(self.ruleForm).then((res) => {
                            getCurrentUserInfo().then((data) => {
                                window.localStorage.setItem('username',data.data.username);
                                // console.log(JSON.stringify(data.data.username));
                                // window.localStorage.setItem('UID',data.data.UID);
                                // window.localStorage.setItem('authorities',data.data.authorities);
                                // window.localStorage.setItem('avatar',data.data.avatar);
                                // window.localStorage.setItem('email',data.data.email);
                                // window.localStorage.setItem('phone',data.data.phone);
                                self.$router.replace({ path: '/dashboard' })
                            })
                    }).catch(() => {
                    })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            }
        }
    }
</script>

<style scoped>
    .login-wrap{
        position: relative;
        width:100%;
        height:100%;
    }
    .ms-title{
        position: absolute;
        left:77.5%;
        top:55%;
        width:100%;
        margin-top: -230px;
        font-family:微软雅黑,"Microsoft YaHei",serif;
        font-weight:bold;
        font-size:40px;
        color: #fff;

    }
    .ms-login{
        position: absolute;
        left:80%;
        top:55%;
        width:360px;
        height:240px;
        margin:-150px 0 0 -190px;
        padding:40px;
        border-radius: 5px;
        box-sizing: border-box;
        box-shadow: 0 0 0 1px hsla(240,0%,100%,.3) inset,
        0 .5em 1em rgba(0, 0, 0, 0.6);
        text-shadow: 0 1px 1px hsla(240,0%,100%,.5);
        background-color: rgba(100,100,100,.2);


    }
    .login-btn{
        text-align: center;
    }
    .login-btn button{
        width:100%;
        height:36px;
    }
</style>
