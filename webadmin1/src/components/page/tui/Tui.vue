<template>
    <div class='tui'>
        <div class="home_1kar3jTW content_2PxQs4xq">
            <div class="stage_VwXaF1J8"><canvas class="canvas_yMNpSrIi" width="1200" height="282"></canvas>
                <div class="accent_3oDdYLdP"></div>
                <div class="accentTwo_3vg5sOKa"></div>
                <div class="grid_1hERgeuD">
                    <div class="vueGridRow_39QYvc1w">
                        <div class="vueGridItem gridItem_3U0kMntn">
                            <h1 class="title_kb1uTXtH">Aincar</h1>
                            <div class="subTitle_2kcK4ktK">
                               解放手脚的农家生活，一款基于树莓派的智能农业解决方案
                            </div>
                            <br>
                            <br>
                            <div class="subTitle_2kcK4ktK">
                               {{userName}} 正在邀请你访问
                            </div>
                            <div class="subTitle_2kcK4ktK">
                                    <a href="http://114.116.245.20">Aincar</a>
                            </div>
                        </div>
                    </div>
                </div>
                </div>
            </div>
        </div>
</template>

<script>
    import {
        getUserNameByUserId,enter
    } from '@/api/promotion';
    export default {
        data() {
            return {
                userName: ""
            }
        },

        mounted() {
            var userId = this.$route.params.userId;
            getUserNameByUserId(userId).then((res) => {
                this.userName = res.data.userName;
            })
            enter(userId)
            //TODO: 请求api
        },
    }
</script>

<style scoped>
    .content_2PxQs4xq{-webkit-box-flex:1;-ms-flex:1;flex:1
}
.home_1kar3jTW h2{text-shadow:0 5px 10px rgba(0,0,0,.33)
}
.home_1kar3jTW .vuePanel{height:100%
}
.home_1kar3jTW .vueGridItem{margin-bottom:1.6rem
}
.home_1kar3jTW .featureSection{padding:0
}
@media (min-width:1024px){
.home_1kar3jTW .featureSection{padding:12rem 0
}
}

.stage_VwXaF1J8{min-height:100vh;overflow:hidden;position:relative;text-align:center;background:#7966f3;background:linear-gradient(171deg,#7966f3,#a851e2)
}
.accent_3oDdYLdP{-webkit-transform:skewX(-40deg) translateX(100%);transform:skewX(-40deg) translateX(100%);background:#9b9bfd;background:linear-gradient(-171deg,#9b9bfd,#7966f3)
}
.accent_3oDdYLdP,.accentTwo_3vg5sOKa{width:56%;min-height:100vh;-webkit-box-shadow:0 1px 3px rgba(0,0,0,.3);box-shadow:0 1px 3px rgba(0,0,0,.3);position:absolute;-webkit-transition:-webkit-transform .25s linear;transition:-webkit-transform .25s linear;transition:transform .25s linear;transition:transform .25s linear,-webkit-transform .25s linear
}
.accentTwo_3vg5sOKa{-webkit-transform:skewX(30deg) translateX(-57%);transform:skewX(30deg) translateX(-57%);background:#17bebb;background:linear-gradient(-171deg,#17bebb,#9b9bfd)
}
.canvas_yMNpSrIi{min-height:100vh;width:100%;position:absolute;background-color:transparent;left:0;top:0
}
.title_kb1uTXtH{top:13.6rem
}
.subTitle_2kcK4ktK{top:12rem
}

.grid_1hERgeuD{max-width:425px;margin:0 auto;position:relative;padding:0 16px
}
@media (min-width:768px){
.grid_1hERgeuD{max-width:768px;padding:0 1.6rem
}
}
@media (min-width:1024px){
.grid_1hERgeuD{max-width:1024px;padding:0 1.6rem
}
}
@media (min-width:1200px){
.grid_1hERgeuD{max-width:1200px;padding:0 3.2rem
}
}
@media (min-width:1440px){
.grid_1hERgeuD{max-width:1440px;padding:0 3.2rem
}
}
.vueGridRow_39QYvc1w{display:-webkit-box;display:-ms-flexbox;display:flex;-ms-flex-wrap:wrap;flex-wrap:wrap;margin:0 -16px
}
@media (min-width:768px){
.vueGridRow_39QYvc1w{margin:0 -1.6rem
}
}
@media (min-width:1024px){
.vueGridRow_39QYvc1w{margin:0 -1.6rem
}
}
@media (min-width:1200px){
.vueGridRow_39QYvc1w{margin:0 -3.2rem
}
}
@media (min-width:1440px){
.vueGridRow_39QYvc1w{margin:0 -3.2rem
}
}

.gridItem_3U0kMntn{-ms-flex-preferred-size:100%;flex-basis:100%;padding:0 16px;min-width:288px
}
@media (min-width:768px){
.gridItem_3U0kMntn{-webkit-box-flex:1;-ms-flex:1;flex:1;padding:0 1.6rem
}
.gridItem_3U0kMntn.fill__DiR0jGJ{-webkit-box-flex:1;-ms-flex:1 1 100%;flex:1 1 100%
}
}
@media (min-width:1024px){
.gridItem_3U0kMntn{padding:0 1.6rem
}
}
@media (min-width:1200px){
.gridItem_3U0kMntn{padding:0 3.2rem
}
}
@media (min-width:1440px){
.gridItem_3U0kMntn{padding:0 3.2rem
}
}
.title_kb1uTXtH{top:13.6rem
}
.subTitle_2kcK4ktK{
    top:17.6rem;
    font-size:1.3rem
}
.subTitle_2kcK4ktK, .title_kb1uTXtH {
    text-shadow: 0 5px 10px rgba(0,0,0,.33);
    position: relative;
    color: #fff;
}
h1{
    font-size: 3.475rem;
}
    h1, h2 {
    font-weight: 400;
    letter-spacing: -.1rem;
}
</style>
