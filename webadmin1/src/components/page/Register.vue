<template>
    <div class="login-wrap">
        <div class="ms-title"><img :src="logo" style="height: 75px"/></div>
    <el-form ref="newUser" :model="newUser" :rules="rules" label-position="left" label-width="0px" class="demo-ruleForm login-container">
        <el-form-item prop="username">
            <el-input type="text" v-model="newUser.username" placeholder="账号"></el-input>
        </el-form-item>
        <el-form-item prop="password">
            <el-input type="password" v-model="newUser.password" placeholder="密码"></el-input>
        </el-form-item>
        <el-form-item prop="passwordRetry">
            <el-input type="password" v-model="newUser.passwordRetry" placeholder="确认密码"></el-input>
        </el-form-item>
        <el-form-item prop="email">
            <el-input type="email" v-model="newUser.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item prop="phone">
            <el-input type="number" v-model="newUser.phone" placeholder="手机"></el-input>
        </el-form-item>
        <el-form-item prop="alternatePhone">
            <el-input type="number" v-model="newUser.alternatePhone" placeholder="备用手机(非必填)"></el-input>
        </el-form-item>
        <el-form-item style="width:100%;color:ffffff;">
            <el-button type="primary" style="width:100%;" @click="submitForm('newUser')">注册</el-button>
            <router-link to="/login"><el-button type="text">登陆</el-button></router-link>
        </el-form-item>
    </el-form>
    </div>
</template>
<script>
import { Message } from 'element-ui';
import logo from '@/assets/logo.png';
    export default {
        name: 'dashboard',
        data() {
            return {
                logo:logo,
                //日志
                newUser: {
                    username: "",
                    password: "",
                    passwordRetry: "",
                    email: "",
                    phone: "",
                    alternatePhone: ""
                },
                rules: {
                    username: [{
                        required: true,
                        message: '请输入用户名',
                        trigger: 'blur'
                    }],
                    password: [{
                        required: true,
                        message: '请输入密码',
                        trigger: 'blur'
                    }],
                    passwordRetry: [{
                        required: true,
                        message: '请确认密码',
                        trigger: 'blur'
                    }],
                    email: [{
                        required: true,
                        message: '请输入邮箱(激活账户用)',
                        trigger: 'blur'
                    }],
                    phone: [{
                        required: true,
                        message: '请输入电话号码',
                        trigger: 'blur'
                    }]
                }

            }
        },
        methods: {
            submitForm(formName) {
                let _this = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.post("/api/user/register", _this.newUser).then(function (data) {
                            console.log(data.data)
                            if (data.data.state == 1) {
                                Message({
                                    showClose: true,
                                    message: data.data.message,
                                    type: 'success'
                                },3000);

                                _this.$router.push('login');
                            } else {
                                Message({
                                    showClose: true,
                                    message: data.data.message,
                                    type: 'error'
                                });
                            }
                        });

                    } else {
                        Message({
                            showClose: true,
                            message: '请填写有效的账户!',
                            type: 'error'
                        });
                        return false;
                    }
                });
            }
        }

    }

</script>

<style>
    .login-wrap{
        position: relative;
        width:100%;
        height:100%;
    }
    .ms-title{
        position: absolute;
        left:77%;
        top:55%;
        width:100%;
        margin-top: -230px;

        font-size:30px;
        color: #fff;

    }

.title {
  margin-bottom: 50px;
  text-align: center;
}

.login-container {
    position: absolute;
    left:80%;
    top:55%;
    width:360px;
    height:500px;
    margin:-150px 0 0 -190px;
    padding:40px;
    border-radius: 5px;
    box-sizing: border-box;
    box-shadow: 0 0 0 1px hsla(240,0%,100%,.3) inset,
    0 .5em 1em rgba(0, 0, 0, 0.6);
    text-shadow: 0 1px 1px hsla(240,0%,100%,.5);
    background-color: rgba(100,100,100,.2);
}

</style>
