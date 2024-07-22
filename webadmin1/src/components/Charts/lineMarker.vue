<template>
  <div :class="className" :id="id" :style="{height:height,width:width}"></div>
</template>

<script>
import {getChart} from "@/api/device/analyze"
import echarts from 'echarts'



export default {

    created(){
        this.getData();
    },


  props: {
    className: {
      type: String,
      default: 'chart'
    },
    id: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '200px'
    },
    height: {
      type: String,
      default: '200px'
    }
  },
  data() {
    return {
      chart: null,
        time:[],
        chartdata:[],
        myArray:[],
    }
  },
  mounted() {
    this.initChart()
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
      getData(){
          this.$axios.all([getChart(1532616577046), getChart(1532616529499),getChart(1532616548703)])
              .then(this.$axios.spread(function (acct1,acct2,acct3) {
                  for (let i=0;i<acct1.data.length;i++){
                      this.myArray[i]=acct1.data[i].data;
                  }
                  console.log(acct1);
                  console.log(perms);
              }));

          getChart(1532616577046).then((res) => {

              for (var i=0;i<res.data.length;i++){
                  this.myArray[i]=res.data[i].data;
              }
              this.chart.setOption({
                  backgroundColor: '#394056',
                  title: {
                      top: 20,
                      text: '数据分析',
                      textStyle: {
                          fontWeight: 'normal',
                          fontSize: 16,
                          color: '#F1F1F3'
                      },
                      left: '1%'
                  },
                  tooltip: {
                      trigger: 'axis',
                      axisPointer: {
                          lineStyle: {
                              color: '#57617B'
                          }
                      }
                  },
                  legend: {
                      top: 20,
                      icon: 'rect',
                      itemWidth: 14,
                      itemHeight: 5,
                      itemGap: 13,
                      data: ['速度', '体温', '心率'],
                      right: '4%',
                      textStyle: {
                          fontSize: 12,
                          color: '#F1F1F3'
                      }
                  },
                  grid: {
                      top: 100,
                      left: '3%',
                      right: '4%',
                      bottom: '2%',
                      containLabel: true
                  },
                  xAxis: [{
                      type: 'category',
                      boundaryGap: false,
                      axisLine: {
                          lineStyle: {
                              color: '#57617B'
                          }
                      },
                      data: ['00:00', '01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00', '24:00']
                  }],
                  yAxis: [{
                      type: 'value',
                      name: '(%)',
                      axisTick: {
                          show: false
                      },
                      axisLine: {
                          lineStyle: {
                              color: '#57617B'
                          }
                      },
                      axisLabel: {
                          margin: 10,
                          textStyle: {
                              fontSize: 14
                          }
                      },
                      splitLine: {
                          lineStyle: {
                              color: '#57617B'
                          }
                      }
                  }],
                  series: [{
                      name: '速度',
                      type: 'line',
                      smooth: true,
                      symbol: 'circle',
                      symbolSize: 5,
                      showSymbol: false,
                      lineStyle: {
                          normal: {
                              width: 1
                          }
                      },
                      areaStyle: {
                          normal: {
                              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                  offset: 0,
                                  color: 'rgba(137, 189, 27, 0.3)'
                              }, {
                                  offset: 0.8,
                                  color: 'rgba(137, 189, 27, 0)'
                              }], false),
                              shadowColor: 'rgba(0, 0, 0, 0.1)',
                              shadowBlur: 10
                          }
                      },
                      itemStyle: {
                          normal: {
                              color: 'rgb(137,189,27)',
                              borderColor: 'rgba(137,189,2,0.27)',
                              borderWidth: 12

                          }
                      },
                      data: [60, 66, 70, 67, 68, 71, 62, 63, 64, 66, 70, 71, 66, 70, 71,72,63, 64, 66,]
                  }, {
                      name: '体温',
                      type: 'line',
                      smooth: true,
                      symbol: 'circle',
                      symbolSize: 5,
                      showSymbol: false,
                      lineStyle: {
                          normal: {
                              width: 1
                          }
                      },
                      areaStyle: {
                          normal: {
                              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                  offset: 0,
                                  color: 'rgba(0, 136, 212, 0.3)'
                              }, {
                                  offset: 0.8,
                                  color: 'rgba(0, 136, 212, 0)'
                              }], false),
                              shadowColor: 'rgba(0, 0, 0, 0.1)',
                              shadowBlur: 10
                          }
                      },
                      itemStyle: {
                          normal: {
                              color: 'rgb(0,136,212)',
                              borderColor: 'rgba(0,136,212,0.2)',
                              borderWidth: 12

                          }
                      },
                      data: [36, 37, 37.5, 37, 36, 36, 37, 37, 37, 36, 36.5, 37, 36, 36.5, 37,36,37.5, 37, 36]
                  }, {
                      name: '心率',
                      type: 'line',
                      smooth: true,
                      symbol: 'circle',
                      symbolSize: 5,
                      showSymbol: false,
                      lineStyle: {
                          normal: {
                              width: 1
                          }
                      },
                      areaStyle: {
                          normal: {
                              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                  offset: 0,
                                  color: 'rgba(219, 50, 51, 0.3)'
                              }, {
                                  offset: 0.8,
                                  color: 'rgba(219, 50, 51, 0)'
                              }], false),
                              shadowColor: 'rgba(0, 0, 0, 0.1)',
                              shadowBlur: 10
                          }
                      },
                      itemStyle: {
                          normal: {
                              color: 'rgb(219,50,51)',
                              borderColor: 'rgba(219,50,51,0.2)',
                              borderWidth: 12
                          }
                      },
                      data: [70, 65, 64, 66, 70, 72, 73, 75, 70, 71, 72, 69, 71, 72, 69,66,70, 72, 73, 75]
                  }]
              })

          });
      },
    initChart() {

      this.chart = echarts.init(document.getElementById(this.id))


    }
  }
}
</script>
