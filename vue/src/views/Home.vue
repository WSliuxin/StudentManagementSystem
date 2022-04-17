<template>
  <div style="padding: 0 20px;margin-top: 20px">
    <el-row :gutter='20' style="margin-bottom: 60px">
      <el-col :span="6">
        <el-card style="color:#409eff;">
          <div >用户总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold"> 100 </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color:#409eff;">
          <div >系统管理员人数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold"> 100 </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color:#409eff;">
          <div >宿舍管理员人数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold"> 100 </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color:#409eff;">
          <div >用户人数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold"> 100 </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 500px; height: 400px"></div>
      </el-col>
      <el-col :span="12">
        <div id="pie" style="width: 500px; height: 400px"></div>
      </el-col>
    </el-row>
  </div>

</template>

<script>
import * as echarts from 'echarts'
import request from "@/utils/request";



export default {
  name: "Home",
  data() {
    return {

    }
  },
  mounted() { //页面元素渲染后在触发

    console.log("111111111111111111111")
    //饼图
    let pieoption = {
      title: {
        text: '用户分布统计',
        subtext: '比例图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          type: 'pie',
          radius: '60%',
          label: {
            normal: {
              show: true,
              position: 'inner',
              textStyle: {
                fontsize: 12,
                color: "#fff"
              },
              formatter: '{d}%'
            }
          },
          data: [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    }
    let pieDom = document.getElementById('pie')
    let pieChart = echarts.init(pieDom)

    //条形折线图
    let option = {
      title: {
        text: "用户分布统计",
        subtext: "趋势图",
        left: 'center'
      },
      xAxis: {
        type: 'category',
        data: ["系统管理员","宿舍管理员","用户","未知人员"]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [],
          type: 'line'
        },
        {
          data: [],
          type: 'bar'
        }
      ]
    }
    let chartDom = document.getElementById('main')
    let myChart = echarts.init(chartDom)

    request.get("/echarts/members").then(res => {

      //填空
      option.series[0].data = res.data
      option.series[1].data = res.data

      pieoption.series[0].data = [
        {name: "系统管理员", value: res.data[0]},
        {name: "宿舍管理员", value: res.data[1]},
        {name: "用户", value: res.data[2]},
        {name: "未知人员", value: res.data[3]},
      ]
      //数据传输完毕在set
      myChart.setOption(option)
      pieChart.setOption(pieoption)
    })




    pieChart.setOption(pieoption)
  }
}
</script>

<style scoped>

</style>