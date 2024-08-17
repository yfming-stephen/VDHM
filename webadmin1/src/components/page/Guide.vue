
<template>
    <section class="main">

<div class="center">
        <el-steps :active="active" align-center finish-status="success" >
            <el-step title="步骤1" description="创建绑定关系"></el-step>
            <el-step title="步骤2" description="传感器和输出设备绑定"></el-step>
            <el-step title="已完成" description="选择智能方案"></el-step>
        </el-steps>
</div>

        <router-view></router-view>

        <el-button style="margin-top: 30px; float: right" @click="next">下一步</el-button>
    </section>
</template>

<script>

    export default {
        data() {
            return {
                active: 0,
            }
        },
        methods: {
            next() {
                if (this.active++ > 2) this.$router.push('DashBoard');
                console.log(this.active);
                if (this.active===0) this.$router.push('NewProject');
                if (this.active===1) this.$router.push('ChooseDevice');
                if (this.active===2) this.$router.push('Scheme');
                if (this.active===3) this.$router.push('Finish');

            }
        }
    }
</script>

<style scoped>

    .center{

        text-align:center;
        margin-left: 150px;
    }
</style>
