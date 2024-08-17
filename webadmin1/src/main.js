import Vue from 'vue';
import AMap from 'vue-amap';
import App from './App';
import router from './router';
import store from './store';
import ElementUI from 'element-ui';
import VueVideoPlayer from 'vue-video-player';
import 'video.js/dist/video-js.css';
import 'videojs-flash';
Vue.use(VueVideoPlayer);
// import 'eblement-ui/lib/theme-default/index.css';    // 默认主题
// import '../static/css/theme-green/index.css';       // 浅绿色主题
import '../static/theme/index.css';       // 浅绿色主题
import "babel-polyfill";

import MintUI from 'mint-ui'
import VeLine from 'v-charts/lib/line'
import VePie from 'v-charts/lib/pie'
import 'v-charts/lib/style.css'
import 'mint-ui/lib/style.css'
import axios from 'axios'

Vue.use(AMap);
// 初始化vue-amap
AMap.initAMapApiLoader({
    // 高德的key
    key: 'a94edef6104776a7376a12575a21e3f8',
    // 插件集合
    plugin: ['AMap.Autocomplete', 'AMap.PlaceSearch', 'AMap.Scale', 'AMap.OverView', 'AMap.ToolBar', 'AMap.MapType', 'AMap.PolyEditor', 'AMap.CircleEditor']
  });

Vue.use(ElementUI);
Vue.prototype.$axios = axios;
Vue.use(MintUI)
Vue.component(VeLine.name, VeLine)
Vue.component(VePie.name, VePie)

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app');
