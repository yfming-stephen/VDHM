import Vue from 'vue';
import {
    Message
  } from 'element-ui'
import Router from 'vue-router';

Vue.use(Router);

const router = new Router({
    routes: [

        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/register',
            component: resolve => require(['../components/page/Register.vue'], resolve),
        },
        {
            path: '/tui/:userId',
            component: resolve => require(['../components/page/tui/Tui.vue'], resolve),
        },
        {
            path: '/trace/:groupId',
            name: 'Trace',
            component: resolve => require(['../components/page/trace/Trace'], resolve)
        },
        {
            path: '/dashboard',
            name: '系统首页',
            meta: {
                requireAuth: true,
            },
            component: resolve => require(['../components/common/Home.vue'], resolve),
            children:[
                {
                    path: '/',
                    name: '系统首页',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/Dashboard.vue'], resolve)
                },
                {
                    path: '/basetable',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/BaseTable.vue'], resolve)
                },
                {
                    path: '/myInfo',
                    name: '个人信息',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/myInfo.vue'], resolve)
                },
                {
                    path: '/vuetable',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/VueTable.vue'], resolve)     // vue-datasource组件
                },
                {
                    path: '/baseform',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/BaseForm.vue'], resolve)
                },
                {
                    path: '/vueeditor',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/VueEditor.vue'], resolve)    // Vue-Quill-Editor组件
                },
                {
                    path: '/markdown',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/Markdown.vue'], resolve)     // Vue-Quill-Editor组件
                },
                {
                    path: '/upload',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/Upload.vue'], resolve)       // Vue-Core-Image-Upload组件
                },
                {
                    path: '/basecharts',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/BaseCharts.vue'], resolve)   // vue-schart组件
                },
                {
                    path: '/device',
                    name: '车载设备管理',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/DeviceManage.vue'], resolve) //设备管理
                },
                {
                    path: '/trigger',
                    name: '车载触发器',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/Trigger.vue'], resolve) //设备管理
                },
                {
                    path: '/Test',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/Test.vue'], resolve) //测试页面
                },
                {
                    path: '/group',
                    name: '车辆管理',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/GroupManage.vue'], resolve) //测试页面

                },
                {
                    path: '/drive/:groupId',
                    name: '行驶信息',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/DriveInfo.vue'], resolve) 

                },
                {
                    path: '/drive/record/:hash',
                    name: '行驶记录',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/DriveRecord.vue'], resolve)

                },
                {
                    path: '/Chart',
                    name: '图像分析',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/Chart.vue'], resolve) //测试页面

                },
                {
                    path: '/devicemap',
                    name: '车辆地图',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/DeviceMap.vue'], resolve) //测试页面

                },
                {
                    path: '/realtimedata',
                    name: '实时数据',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/RealTimeData.vue'], resolve) //测试页面

                },
                {
                    path: '/admin/device',
                    name: '[系统]设备管理',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/admin/DeviceManage.vue'], resolve) //测试页面

                },
                {
                    path: '/admin/group',
                    name: '[系统]群组管理',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/admin/GroupManage.vue'], resolve) //测试页面

                },
                {
                    path: '/admin/alluser',
                    name: '[系统]所有用户',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/admin/AllUser.vue'], resolve) //全部用户

                },
                {
                    path: '/admin/alllog',
                    name: '[系统]日志记录',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/admin/AllLog.vue'], resolve) //全部用户

                },
                {
                    path: '/alljob',
                    name: '任务管理',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/UserJob.vue'], resolve) //全部用户

                },
                {
                    path: '/enterprise',
                    name: '商家数据',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/Enterprise.vue'], resolve) //全部用户

                },
                {
                    path: '/Guide',
                    meta: {
                        requireAuth: true,
                    },
                    component: resolve => require(['../components/page/Guide.vue'], resolve),
                    children: [
                        {
                            path: '/NewProject',
                            meta: {
                                requireAuth: true,
                            },
                            component: resolve => require(['../components/page/NewProject.vue'], resolve)
                        },
                        {
                            path: '/Scheme',
                            meta: {
                                requireAuth: true,
                            },
                            component: resolve => require(['../components/page/Scheme.vue'], resolve)
                        },
                        ]
                }
            ]
        },
        {
            path: '/login',
            component: resolve => require(['../components/page/Login.vue'], resolve)
        },
    ]
})
//检查是否登陆
router.beforeEach((to, from, next) => {
    if (to.meta.requireAuth) {  // 判断该路由是否需要登录权限
        if (window.localStorage.getItem('username')===null) {
            Message.error({
                message: '请登陆!'
                })
                next({
                    path: '/login',
                })
        }
        else {
            next();
        }
    }
    else {
        next();
    }
})
export default router
