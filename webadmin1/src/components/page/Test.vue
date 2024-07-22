<template>
    <div id="status">
<el-button type="text" @click="dialogVisible = true">点击打开 Dialog</el-button>
<el-dialog
        title="设备详细"
        :visible.sync="dialogVisible"
        width="30%"
        :before-close="handleClose">
    <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="基本信息" name="first">
            <el-card shadow="hover" class="box-card">
                <el-table
                        :data="tableData2"
                        style="width: 100%"
                        :row-class-name="tableRowClassName">
                    <el-table-column
                            prop="date"
                            label="属性"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            prop="name"
                            label="值">
                    </el-table-column>
                </el-table>
            </el-card>
        </el-tab-pane>
        <el-tab-pane label="日志查看" name="second">
        <el-table
                :data="tableData3"
                style="width: 100%"
                :row-class-name="tableRowClassName">
            <el-table-column
                    prop="date"
                    label="日期"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="name"
                    label="事件">
            </el-table-column>
        </el-table>
            </el-tab-pane>
        <el-tab-pane label="执行命令" name="third">
            <div style="margin-top: 15px;">
                <el-input placeholder="请输入内容" v-model="input5" class="input-with-select">
                    <el-select v-model="select" slot="prepend" placeholder="请选择">
                        <el-option label="餐厅名" value="1"></el-option>
                        <el-option label="订单号" value="2"></el-option>
                        <el-option label="用户电话" value="3"></el-option>
                    </el-select>
                    <el-button slot="append" icon="el-icon-search"></el-button>
                </el-input>
            </div>
            <el-card class="box-card" style="margin-top: 20px">
                <div slot="header" class="clearfix">
                    <h2>Web终端</h2>
                </div>
                <textarea readonly="readonly" class="terminal"></textarea>
            </el-card>
        </el-tab-pane>
    </el-tabs>
    <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
  </span>
</el-dialog>
</div>
</template>
<script>
    export default {
        data() {
            return {
                dialogVisible: false,
                activeName: 'first',
                input5:'{{select}}',
                tableData2: [{
                    date: '2016-05-02',
                    name: '王小虎',

                }, {
                    date: '2016-05-04',
                    name: '王小虎',

                }]
            };
        },
        methods: {
            handleClose(done) {
                this.$confirm('确认关闭？')
                    .then(_ => {
                        done();
                    })
                    .catch(_ => {});
            }
        }
    };
</script>

<style scoped>

    .terminal {
        width: 100%;
        height: 200px;
        color: #7fff00;
        background-color: #000;
    }
</style>