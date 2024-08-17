<template>
    <div class="trace">

    <div class="back">

    </div>
        <mt-tab-container v-model="selected">
            <mt-tab-container-item id="产品信息">
                <mt-swipe style="height: 250px" :auto="4000">

                    <mt-swipe-item>  <center><img style="align: center;height: 260px;width:100%" :src="mint1" ></center></mt-swipe-item>
                    <mt-swipe-item><center><img style="align: center;height: 260px;width:100%" :src="mint2" ></center></mt-swipe-item>
                    <mt-swipe-item><center><img style="align: center;height: 260px;width:100%" :src="mint3" ></center></mt-swipe-item>

                </mt-swipe>
                <ve-pie
                        style="margin-top: 0px"
                        :data="chartData"
                        :settings="chartSettings">
                </ve-pie>

                <center> <mt-badge style="text-align: center;margin-top: -50px" size="small" type="success"><div ><h2 style="color: white">生产农场/{{name}}</h2></div></mt-badge></center>

            </mt-tab-container-item>
            <mt-tab-container-item id="产品数据">
                <center>
                <div v-for="(device,index) in deviceData">
                    <div v-if="device.rows.length!==0">
                        <div style="text-align: center;"><mt-badge style="margin-top: 10px" type="success">{{device.deviceType}}</mt-badge></div>

                      <div style="text-align: center;">
                          <div v-if="index%2===1">
                          <ve-line :grid="grid" :colors="colors2" :settings="chartSettings" :data="device" style="margin:0 auto; "  height="300px" width="350px"></ve-line>
                          </div>
                          <div v-if="index%2===0">
                              <ve-line :grid="grid2" :colors="colors1" :settings="chartSettings" :data="device" style="margin:0 auto;"  height="300px" width="350px"></ve-line>
                          </div>
                      </div>
                    </div>
                </div>
                </center>
            </mt-tab-container-item>
        </mt-tab-container>

        <mt-tabbar v-model="selected">
            <mt-tab-item id="产品信息">
                产品信息
            </mt-tab-item>
            <mt-tab-item id="产品数据">
                产品数据
            </mt-tab-item>
        </mt-tabbar>

    </div>
</template>

<script>
    import {
        getTraceByGroup,
    } from "@/api/trace/index"
    import mint1 from '@/assets/mint1.png';
    import mint2 from '@/assets/mint2.jpg';
    import mint3 from '@/assets/mint3.jpg';
    export default {
        name: 'Trace',
        data() {
            this.colors1 = ['#6be0c6']
            this.colors2 = ['#8ec9f2']

            this.grid = {
                show: true,
                top: 50,
                left: 10,
                borderColor: '#ffffff'

            }
                this.grid2 = {

                    top: 50,
                    left: 10,
                    borderColor: '#ffffff'

                }
            this.chartSettings = {
                area: true
            }
            return {
                chartData: {
                    columns: ['日期', '访问用户'],
                    rows: [
                        {'日期': '温度质量', '访问用户': 1393},
                        {'日期': '湿度质量', '访问用户': 3530},
                        {'日期': 'PH值质量', '访问用户': 2923},
                        {'日期': '钾质量', '访问用户': 1723},
                        {'日期': '磷质量', '访问用户': 3792},
                        {'日期': '氮质量', '访问用户': 4593}
                    ]
                },
                mint1,
                mint2,
                mint3,
                msg: 'test',
                deviceData: [],
                name: '小农场',
                selected: "产品信息",
            }
        },
        methods: {
            getData(groupId) {
                getTraceByGroup(groupId).then((res) => {
                    // console.log(res.data.data)
                    this.name = res.data.name
                    this.deviceData = res.data.data
                })
            }
        },
        mounted() {
            var groupId = this.$route.params.groupId;
            this.getData(groupId)
        },
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.page-tabbar-container{
     overflow:hidden
}
.back{
  /*background:url("/static/img/mback.jpg") no-repeat top left;*/
  background-size:100% 100%;
  opacity: 0.9;
  position: absolute;
  width:100%;
  height:100%;

}
</style>
