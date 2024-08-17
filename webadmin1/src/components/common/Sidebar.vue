<template>
    <div class="sidebar">
        <el-menu :default-active="onRoutes" class="el-menu-vertical-demo" style="overflow: auto" theme="dark" unique-opened router>
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-submenu :index="item.index">
                        <template slot="title"><i :class="item.icon"></i>{{ item.title }}</template>
                        <el-menu-item v-for="(subItem,i) in item.subs" :key="i" :index="subItem.index">{{ subItem.title }}
                        </el-menu-item>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index">
                        <i :class="item.icon"></i>{{ item.title }}
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
// 如果需要分权限显示不同的目录再改 目前默认都显示这些
    import { getCurrentUserInfo } from '@/api/user'
    export default {
        data() {
            return {
                userItems: [
                    {
                        icon:'el-icon-tickets',
                        index: '/dashboard',
                        title: '系统首页'
                    },
                    // {
                    //     icon:'el-icon-news',
                    //     index: '/Chart',
                    //     title: '图像分析'
                    // },
                    {
                        icon:'el-icon-view',
                        index: '/realtimedata',
                        title: '实时数据'
                    },
                    {
                        icon:'el-icon-location-outline',
                        index: '/devicemap',
                        title: '车辆地图'
                    },
                    
                    // {   icon: 'el-icon-upload2',
                    //     index: '/Test',
                    //     title: '测试'
                    // },

                    // {
                    //     icon: 'el-icon-menu',
                    //     index: '2',
                    //     title: '表格',
                    //     subs: [
                    //         {
                    //             index: '/basetable',
                    //             title: '基础表格'
                    //         },
                    //         {
                    //             index: '/vuetable',
                    //             title: 'Vue表格组件'
                    //         }
                    //     ]
                    // },
                    // {
                    //     icon: 'el-icon-date',
                    //     index: '3',
                    //     title: '表单',
                    //     subs: [
                    //         {
                    //             index: '/baseform',
                    //             title: '基本表单'
                    //         },
                    //         {
                    //             index: '/vueeditor',
                    //             title: '编辑器'
                    //         },
                    //         {
                    //             index: '/markdown',
                    //             title: 'markdown'
                    //         },
                    //         {
                    //             index: '/upload',
                    //             title: '文件上传'
                    //         }
                    //     ]
                    // },
                    // {
                    //     icon: 'el-icon-star-on',
                    //     index: '/basecharts',
                    //     title: '图表'
                    // },
                    {   icon:'el-icon-date',
                        index:'/device',
                        title:'车载设备管理'

                    },
                    {   icon:'el-icon-bell',
                        index:'/trigger',
                        title:'车载触发器'

                    },
                    // {   icon: 'el-icon-upload2',
                    //     index: '/Test',
                    //     title: '测试'
                    // },
                    {
                        icon:'el-icon-star-on',
                        index: '/alljob',
                        title: '任务管理'
                    },
                    {
                        icon:'el-icon-menu',
                        index: '/group',
                        title: '车辆管理'
                    },
                    {
                        icon:'el-icon-search',
                        index: '/myInfo',
                        title: '个人信息'
                    }
                    ],
                adminItems:
                    {
                    icon: 'el-icon-setting',
                    index: '1',
                    title: '系统管理',
                        subs:[
                            {   icon:'el-icon-menu',
                                index: '/admin/device',
                                title: '设备管理'
                            },
                            {   icon:'el-icon-menu',
                                index: '/admin/group',
                                title: '群组管理'
                            },
                            {
                                icon:'el-icon-view',
                                index: '/admin/alluser',
                                title: '所有用户'
                            },
                            {
                                icon:'el-icon-news',
                                index: '/admin/alllog',
                                title: '日志记录'
                            },
                        ]
                    },
                // enterpriseItems:
                //     {
                //         icon:'el-icon-more',
                //         index: '/enterprise',
                //         title: '商家数据'
                //     },
                items:[]

            }
        },
        computed:{
            onRoutes(){
                return this.$route.path.replace('/','');
            }
        },
        created() {
            getCurrentUserInfo().then((data) => {
                if(data.data.authorities.indexOf("ROLE_ADMIN")=== -1){
                    // if(data.data.authorities.indexOf("ROLE_ENTERPRISE")!=-1){
                    //     this.items=this.enterpriseItems;
                    // }else{
                        this.items=this.userItems;
                    // }
                }else{
                    this.items=this.userItems;
                    // this.items.push(this.enterpriseItems);
                    this.items.push(this.adminItems);
                }

            })
        },
    }
</script>

<style scoped>
    .sidebar{
        display: block;
        position: absolute;
        width:255px;
        left: 0;
        top: 80px;
        bottom:0;
        background: #22d0b2;
    }
    .sidebar > ul {
        height:100%;
    }
</style>
