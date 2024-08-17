<template>
    <div class="table">

        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i>车载设备</el-breadcrumb-item>
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
                    <el-button style="float: right" type="default" icon="el-icon-plus" @click="newDevice()">新建</el-button>
                </el-col>
            </el-row>
        </div>

        <!-- 显示设备 -->
        <el-table :data="tableData" border style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="id" label="设备ID" sortable width="150">
            </el-table-column>
            <el-table-column prop="name" label="设备名称" width="180">
            </el-table-column>
            <el-table-column prop="describe" label="备注" width="180">
            </el-table-column>
            <el-table-column prop="deviceType" label="设备类型" width="180">
            </el-table-column>
            <el-table-column prop="groupName" label="群组名称" width="120">
            </el-table-column>
            <el-table-column label="操作" :formatter="formatter">
                <template scope="scope" align="right">
                                        <el-button size="small" type="primary"
                                                   @click="getDeviceStatus(scope.$index, scope.row)">状态</el-button>
                                        <!-- TODO:此处应该调用方法先获取当前选择设备的信息 -->
                                        <el-button size="small"
                                                   @click="editDeviceInfo(scope.row)">编辑</el-button>
                                        <el-button size="small" type="danger"
                                                   @click="closeDelDialogVisible(scope)">删除</el-button>
</template>
            </el-table-column>
        </el-table>


        <!-- 编辑设备 -->
            <el-dialog  title="编辑" :visible.sync="dialogEditFormVisible" >
                <!-- <el-tabs>
                    <el-tab-pane label="基础信息"> -->
                        <el-form :model="EditTableData" style="width:70%">
                            <el-form-item label="设备ID" :label-width="formLabelWidth">
                                <el-input v-model="EditTableData.deviceId" auto-complete="off"></el-input>
                            </el-form-item>
                            <el-form-item label="名称" :label-width="formLabelWidth">
                                <el-input v-model="EditTableData.deviceName" auto-complete="off"></el-input>
                            </el-form-item>
                            <el-form-item label="类型" :label-width="formLabelWidth">
                                <el-input v-model="EditTableData.type" auto-complete="off"></el-input>
                            </el-form-item>
                            <el-form-item label="备注" :label-width="formLabelWidth">
                                <el-input v-model="EditTableData.deviceDescribe" auto-complete="off"></el-input>
                            </el-form-item>
                            <el-form-item label="地址" :label-width="formLabelWidth">
                                <el-input v-model="EditTableData.locationDescribe" auto-complete="off"></el-input>
                            </el-form-item>
                            <el-form-item label="分组" :label-width="formLabelWidth">
                                <el-select v-model="EditTableData.groupName" placeholder="请选择">
                                    <el-option
                                            v-for="item in this.groups"
                                            :key="item.name"
                                            :label="item.name"
                                            :value="item.name">
                                    </el-option>
                                </el-select>
                            </el-form-item>


                        </el-form>
                        <div slot="footer" class="dialog-footer">
                            <el-button @click="dialogEditFormVisible= false">取 消</el-button>
                            <el-button type="primary" @click="editDevice()">确 定</el-button>
                        </div>
                    <!-- </el-tab-pane> -->
                    <!-- <el-tab-pane label="报警设置">
                        <el-form :model="deviceScope" style="width:70%">
                            <el-form-item label="选择开启" :label-width="formLabelWidth">
                             <el-switch on-text="" off-text="" v-model="isLimitValue" @change="changeScope()"></el-switch>
                            </el-form-item>
                            <el-form-item label="下限" :label-width="formLabelWidth">
                                <el-input v-model="deviceScope.lowerValue" auto-complete="off"></el-input>
                            </el-form-item>
                            <el-form-item label="上限" :label-width="formLabelWidth">
                                <el-input v-model="deviceScope.upperValue" auto-complete="off"></el-input>
                            </el-form-item>
                        </el-form>
                    </el-tab-pane> -->
                <!-- </el-tabs> -->
            </el-dialog>

        <!-- 新建设备 -->
        <el-dialog  title="新建" :visible.sync="dialogCreateFormVisible" >
            <el-form :model="createForm" style="width:70%">
                <el-form-item label="名称" :label-width="formLabelWidth">
                    <el-input v-model="createForm.deviceName" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="备注" :label-width="formLabelWidth">
                    <el-input v-model="createForm.deviceDescribe" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="类型" :label-width="formLabelWidth">
                    <el-input v-model="createForm.deviceType" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="地址" :label-width="formLabelWidth">
                    <el-input v-model="createForm.locationDescribe" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="分组" :label-width="formLabelWidth">
                    <el-select v-model="createForm.groupId" placeholder="请选择">
                        <el-option
                            v-for="item in this.groups"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogCreateFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="createDevice()">确 定</el-button>
            </div>
        </el-dialog>

        <!-- 删除确认 -->
        <el-dialog
                title="提示"
                :visible.sync="is_delete"
                width="30%">
            <div>您确定删除  {{delete_messgae}}  吗？</div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="is_delete = false">取 消</el-button>
                <el-button type="primary" @click="deleteConfirm()" >确 定</el-button>
            </span>
        </el-dialog>
        <div class="pagination">
            <el-pagination
                    @current-change ="handleCurrentChange"
                    layout="prev, pager, next"
                    :total="total">
            </el-pagination>
        </div>

        <div id="status">
            <el-dialog
                    title="设备详细"
                    :visible.sync="dialogVisible"
                    width="50%"
                    :before-close="disConnect">
                <el-tabs v-model="activeName" >
                    <el-tab-pane label="基本信息" name="first">
                        <el-card shadow="hover" class="box-card">
                            <el-row>
                                <el-col :span="5"><div class="grid-content bg-purple">设备名</div></el-col>
                                <el-col :span="12"><div class="grid-content bg-purple-light"></div>{{deviceName}}</el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="5"><div class="grid-content bg-purple">当前状态</div></el-col>
                                <el-col :span="12"><div class="grid-content bg-purple-light"></div>{{statuss}}</el-col>
                            </el-row>
                            <el-row>
                            <el-col :span="5"><div class="grid-content bg-purple">设备ID</div></el-col>
                            <el-col :span="12"><div class="grid-content bg-purple-light">{{deviceId}}</div></el-col>
                                </el-row>
                            <el-row>
                            <el-col :span="5"><div class="grid-content bg-purple">最后上线</div></el-col>
                            <el-col :span="12"><div class="grid-content bg-purple-light"></div>{{lastOnline}}</el-col>
                            </el-row>
                                <el-row>
                                    <el-col :span="5"><div class="grid-content bg-purple">SDK-KEY</div></el-col>
                            <el-col :span="12"><div class="grid-content bg-purple-light"></div>{{sdkKey}}</el-col>
                                    </el-row>
                                    <el-row>
                            <el-col :span="5"><div class="grid-content bg-purple">备注信息</div></el-col>
                            <el-col :span="12"><div class="grid-content bg-purple-light">{{remark}}</div></el-col>
                                    </el-row>
                        </el-card>
                    </el-tab-pane>
                    <!-- TODO:分页 -->
                    <el-tab-pane label="日志查看" name="second">
                        <el-table
                                style="width: 100%"
                                :data="logdata">
                            <el-table-column
                                    prop="date"
                                    label="日期"
                                    width="250">
                            </el-table-column>
                            <el-table-column
                                    prop="event"
                                    label="事件">
                            </el-table-column>
                        </el-table>
                    </el-tab-pane>
                    <el-tab-pane label="定时任务" name="third">
                        <el-card class="box-card">
                            <div slot="header" class="clearfix">
                                <span>当前任务</span>
                            </div>
                            <div>
                                <table id="tfhover" class="tftable" border="1">
                                    <tr>
                                        <th>Cron表达式</th>
                                        <th>JobJson(定时推送的内容)</th>
                                    </tr>
                                    <tr>
                                        <td>{{this.deviceCronInfo.cron}}</td>
                                        <td>{{this.deviceCronInfo.jobJson}}</td>
                                    </tr>
                                </table>
                            </div>
                        </el-card>

                        <el-card class="box-card">
                            <div slot="header" class="clearfix">
                                <span>设置</span>
                            </div>
                            <el-row>
                                <el-col :span="14">
                                    <div class="grid-content bg-purple" style="padding-right:6px;">
                                        <el-input v-model="addJobInfo.jobJson.jobJson" placeholder="请输入定时发送的消息内容"></el-input>
                                    </div>
                                </el-col>
                                <el-col :span="6">
                                    <div class="grid-content bg-purple-light" style="padding-left:6px;">
                                        <div class="cron">
                                            <el-popover v-model="cronPopover">
                                                <cron @change="changeCron" @close="cronPopover=false" i18n="cn"></cron>
                                                <el-input slot="reference" @click="cronPopover=true" v-model="cron" placeholder="请输入定时策略"></el-input>
                                            </el-popover>
                                        </div>
                                    </div>
                                </el-col>
                                <el-col :span="4">
                                    <div class="grid-content bg-purple" style="padding-left:6px;">
                                        <el-button @click="addjob()" type="primary">提交</el-button>
                                    </div>
                                </el-col>
                            </el-row>
                        </el-card>
                    </el-tab-pane>

                    <el-tab-pane label="执行命令" name="fourth">
                        <div style="margin-top: 15px;">
                            <el-input placeholder="请输入内容" v-model="cmd" class="input-with-select">
                                <el-select v-model="cmd" slot="prepend" placeholder="请选择">
                                    <el-option label="PING" value="PING"></el-option>
                                    <el-option label="重启" value="REBOOT"></el-option>
                                    <el-option label="SSH" value="SSH"></el-option>
                                </el-select>
                                <el-button slot="append" icon="el-icon-caret-right" @click="sendCmd"></el-button>
                            </el-input>
                        </div>
                        <el-card class="box-card" style="margin-top: 20px">
                            <div slot="header" class="clearfix">
                                <h2>Web终端</h2>
                            </div>
                            <textarea readonly="readonly" v-model="consoleLog" class="terminal">
                            </textarea>
                        </el-card>
                    </el-tab-pane>
                </el-tabs>
                <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
  </span>
            </el-dialog>
        </div>
    </div>


</template>


<script>
    import ElSelectDropdown from "element-ui/packages/select/src/select-dropdown";
    import {
        getAllDevices,
        createADevice,
        delADevice,
        searchByAppUser,
        changeDeviceGroup,
        addJob
    } from "@/api/user/device"
    import {
        getAllGroups
    } from "@/api/user/group"
    import {
        getDeviceDetails,
        getDeviceLog,
        sendCmdToDevice,
        // getDeviceScope,
        // updateValueLimit
    } from "@/api/device"
    import {
        cron
    } from "vue-cron";
    export default {
        components: {
            cron,
            ElSelectDropdown,
        },
        data() {
            return {
                cron: "",
                cronPopover: false,
                EditTableData: {
                    deviceId: '',
                    deviceName: '',
                    type: '',
                    groupName: '',
                    locationDescribe: '',
                    deviceDescribe: '',
                    groupId: ''
                },
                logdata: [],
                dialogVisible: false,
                activeName: 'first',
                // input5:'{{select}}',
                statuss: '',
                deviceId: '',
                deviceName: '',
                lastOnline: '',
                sdkKey: '',
                remark: '',
                dialogTableVisible: false,
                dialogEditFormVisible: false,
                dialogCreateFormVisible: false,
                createForm: {
                    deviceDescribe: 'Device123',
                    deviceName: 'Device123',
                    // 经度纬度先确定 后期可以考虑获取方法
                    latitude: "39.54",
                    longitude: "116.23",
                    locationDescribe: 'hziee',
                    groupId: '',
                    deviceType: "TEMP"
                },
                groups: [],
                formLabelWidth: '50%',
                tableData: [],
                cur_page: 0,
                page_size: 10,
                total: 0,
                multipleSelection: [],
                select_cate: '',
                select_word: '',
                del_list: [],
                is_search: false,
                is_delete: false,
                delete_messgae: {},
                delete_id: '',
                scope: [],
                cmd: '',
                // 后期可以考虑从配置文件里读取连接地址
                client: new Paho.MQTT.Client('39.108.153.134', 8083, ''), // 第三个参数是clientID可以为空
                topicEcho: '',
                topicOut: '',
                consoleLog: 'Welcome to Web Console V1.0',
                sendCmdInfo: {
                    deviceId: '',
                    payload: {
                        cmd: ''
                    }
                },
                deviceCronInfo: {
                    cron: "* * * * * ? *",
                    jobJson: {
                        //JSON格式自定义
                        K1: "V1",
                        K2: "V2",
                        Kn: "Vn"
                    }
                },
                addJobInfo: {
                    cronExpression: '',
                    jobJson: {
                        jobJson: ""
                    },
                    deviceId: ""
                },
                // isLimitValue:false,
                // deviceScope:{}
            }
        },

        created() {
            this.getData();
            this.fetchAllGroups();
        },
        methods: {
            // changeScope(){
            //     if(this.isLimitValue){
            //         this.deviceScope.isLimitValue=1
            //     }else{
            //         this.deviceScope.isLimitValue=0
            //     }
            //     console.log(this.deviceScope)
            //     updateValueLimit(this.deviceScope)

            // },
            handleCurrentChange(val) {
                this.cur_page = val - 1;
                this.getData();
            },
            getData() {
                getAllDevices(this.cur_page, this.page_size).then((res) => {
                    this.total = res.data.totalElements;
                    this.tableData = res.data.data;
                    // console.log(res.data.data);
                })
            },
            fetchAllGroups() {
                getAllGroups().then((res) => {

                    this.groups = res.data;
                    // console.log(this.groups);
                    this.createForm.groupId = this.groups[0].id;
                })
            },
            search() {
                searchByAppUser(this.select_word).then((res) => {
                    // console.log(res.data);
                    this.tableData = res.data;
                })
            },
            formatter(row, column) {
                return row.address;
            },
            filterTag(value, row) {
                return row.tag === value;
            },
            handleEdit(index, row) {
                this.$message('编辑第' + (index + 1) + '行');
            },
            handleDelete(index, row) {
                this.$message.error('删除第' + (index + 1) + '行');
            },
            delAll() {
                const self = this;
                self.$confirm('确认删除所选设备？')
                    .then(_ => {
                        length = self.multipleSelection.length;
                        let str = '';
                        self.del_list = self.del_list.concat(self.multipleSelection);
                        for (let i = 0; i < length; i++) {
                            str += self.multipleSelection[i].name + ' ';
                            delADevice(self.multipleSelection[i].id).then((res) => {
                                self.getData();
                            });
                        }
                        self.$message.error('删除了' + str);
                        self.multipleSelection = [];
                    }).catch(_ => {});
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            closeDelDialogVisible(scope) {
                let row = scope.row;
                this.delete_messgae = row.name;
                this.is_delete = true;
                this.delete_id = row.id;
            },
            deleteConfirm() {
                delADevice(this.delete_id).then((res) => {
                    this.$message({
                        showClose: true,
                        message: '成功删除设备ID:' + this.delete_id,
                        type: 'success'
                    });
                    this.getData();
                    this.is_delete = false;
                })
            },
            createDevice() {
                this.$confirm('确认新建设备？')
                    .then(_ => {
                        createADevice(this.createForm).then((res) => {
                            this.getData();
                        })
                        this.dialogCreateFormVisible = false;
                        this.$message({
                            showClose: true,
                            message: '新建设备成功！',
                            type: 'success'
                        });
                    })
                    .catch(_ => {});
            },
            getDeviceStatus(index, row) {
                this.dialogVisible = true;
                getDeviceDetails(row.id).then((res) => {
                    console.log(res.data);
                    this.deviceId = res.data.id;
                    this.deviceName = res.data.name;
                    this.addJobInfo.deviceId = res.data.id;
                    this.statuss = res.data.isOnline;
                    this.lastOnline = res.data.lastActiveDate;
                    this.sdkKey = res.data.key;
                    this.remark = res.data.describe;
                    this.connectEmq(this.sdkKey);
                });
                getDeviceLog(row.id).then((res) => {
                    //console.log(res.data.data);
                    this.logdata = res.data.data;
                });
            },
            editDeviceInfo(row) {
                console.log(row);
                this.dialogEditFormVisible = true;
                this.EditTableData.deviceId = row.id;
                this.EditTableData.deviceName = row.name;
                this.EditTableData.deviceDescribe = row.describe;
                this.EditTableData.groupName = row.groupName;
                this.EditTableData.type = row.deviceType;
                this.createForm.groupName = row.groupName;
                this.EditTableData.locationDescribe = row.location.describe;
                this.EditTableData.groupId = row.groupId;

                // getDeviceScope(row.id).then((res) => {
                //     if(res.data.isLimitValue===0){
                //         this.isLimitValue=false
                //     }else{
                //         this.isLimitValue=true
                //     }
                //     this.deviceScope=res.data
                //     //console.log(this.deviceScope)
                // })
            },
            connectEmq(sdkKey) {
                var path = sdkKey.replace(/-/g, '/');
                this.topicEcho = 'IN/ECHO/' + path
                this.topicIn = 'IN/ECHO/' + path
                this.topicOut = 'OUT/DEVICE/' + path
                this.client.connect({
                    userName: "websocket_client",
                    password: "websocket_client",
                    onSuccess: this.onConnect
                }); // 连接服务器并注册连接成功处理事件
                this.client.onConnectionLost = this.onConnectionLost; // 注册连接断开处理事件
                this.client.onMessageArrived = this.onMessageArrived; // 注册消息接收处理事件

            },
            onConnectionLost(responseObject) {
                if (responseObject.errorCode !== 0) {
                    console.log('onConnectionLost:' + responseObject.errorMessage);
                    console.log('连接已断开');
                }
            },
            onMessageArrived(message) {
                var str;
                console.log(message)
                if (message.destinationName === this.topicEcho) {
                    str = '\nECHO_>>' + message.payloadString;
                    this.consoleLog += str;
                } else if (message.destinationName === this.topicOut) {

                    str = '\nOUT_>>' + message.payloadString;
                    this.consoleLog += str;
                }
                console.log("收到消息:" + message.payloadString);
            },
            onConnect() {
                console.log('onConnected');
                // 订阅主题
                this.client.subscribe(this.topicEcho);
                this.client.subscribe(this.topicOut);
            },
            disConnect(done) {
                this.dialogVisible=false;
                console.log('disConnect');
                this.client.disconnect();
                this.consoleLog = 'Welcome to Web Console V1.0';
                this.cmd = '';
                done();
            },
            sendCmd() {
                this.sendCmdInfo.payload.cmd = this.cmd;
                this.sendCmdInfo.deviceId = this.deviceId;
                sendCmdToDevice(this.sendCmdInfo);
            },
            newDevice() {
                this.dialogCreateFormVisible = true
                getAllGroups().then((res) => {})
            },
            editDevice() {
                changeDeviceGroup(this.EditTableData).then((res) => {
                    console.log(res);
                    this.$message({
                        showClose: true,
                        message: res.message + '成功修改设备ID:' + this.EditTableData.deviceId,
                        type: 'success'
                    });
                    this.dialogEditFormVisible = false;
                    this.getData();
                });
            },
            changeCron(val) {
                this.cron = val;
                console.log("Cron表达式:" + val);
            },
            addjob() {
                this.addJobInfo.cronExpression = this.cron;
                // console.log(this.addJobInfo);
                addJob(this.addJobInfo).then((res) => {
                    this.$message({
                        showClose: true,
                        message: res.message,
                        type: 'success'
                    });
                })
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

    .terminal {
        width: 100%;
        height: 200px;
        color: #7fff00;
        background-color: #000;
    }

    .el-row {
        margin-bottom: 25px;
    }
</style>
