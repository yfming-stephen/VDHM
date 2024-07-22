<template>
    <div class="header">
        <img :src="logo" style="height: 45px;margin-top: 15px;margin-left:8px;float: left"/> <div class="logo">智能网联汽车驾驶健康伙伴设备管理系统</div>
        <div class="user-info">
            <el-dropdown trigger="click" @command="handleCommand">
                <span class="el-dropdown-link" style="margin-top:7px ">
                    <img class="user-logo" :src="this.avatar">
                    {{username}}
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="loginout">退出</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
    </div>
</template>
<script>
    import {logout} from "@/api/user/index"
    import logo from '@/assets/logo.png'
    export default {
        data() {
            return {
                logo:logo,
                name: '',
                avt: '../../../static/img/img.jpg'
            }
        },
        computed:{
            username(){
                let username = window.localStorage.getItem('username');
                return username ? username : this.name;
            },
            avatar(){
                let avatar = window.localStorage.getItem('avatar');
                return avatar ? avatar : this.avt;
            }
        },
        methods:{
            handleCommand(command) {
                if(command == 'loginout'){
                    localStorage.removeItem('username');
                    logout();
                    this.$router.push('/login');
                }
            }
        }
    }
</script>
<style scoped>
    .header {
        position: relative;
        box-sizing: border-box;
        width: 100%;
        height: 100px;
        font-size: 22px;
        line-height: 70px;
        color: #fff;

    }
    .header .logo{
        float: left;
        /* width:250px; */
        text-align: left;
        margin-left: 10px;
        margin-bottom: 50px;
        margin-top: 8px;
    }
    .user-info {
        float: right;
        padding-right: 50px;
        font-size: 16px;
        color: #fff;
    }
    .user-info .el-dropdown-link{
        position: relative;
        display: inline-block;
        padding-left: 50px;
        color: #fff;
        cursor: pointer;
        vertical-align: middle;
    }
    .user-info .user-logo{
        position: absolute;
        left:0;
        top:15px;
        width:40px;
        height:40px;
        border-radius: 50%;
    }
    .el-dropdown-menu__item{
        text-align: center;
    }
</style>
