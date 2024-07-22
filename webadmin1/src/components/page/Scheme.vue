
<template>
    <section class="main">

        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <span>冬季大棚方案</span>
                <el-button style="float: right; padding: 3px 0" type="text">一键设置</el-button>
            </div>
            <div v-for="o in 4" :key="o" class="text item">
                {{'列表内容 ' + o }}
            </div>
        </el-card>



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

        }
    }
</script>

<style scoped>

    .text {
        font-size: 14px;
    }

    .item {
        margin-bottom: 18px;
    }

    .clearfix:before,
    .clearfix:after {
        display: table;
        content: "";
    }
    .clearfix:after {
        clear: both
    }

    .box-card {
        margin-top: 50px;
        width: 480px;
    }
</style>
