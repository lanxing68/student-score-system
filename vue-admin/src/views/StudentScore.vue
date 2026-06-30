<template>
  <div class="student-score-wrap">
    <h3>我的成绩</h3>
    <el-table :data="scoreList" border>
      <el-table-column label="课程名" prop="courseName"></el-table-column>
      <el-table-column label="学期" prop="term"></el-table-column>
      <el-table-column label="课堂表现" prop="classPerformance"></el-table-column>
      <el-table-column label="实验" prop="experiment"></el-table-column>
      <el-table-column label="作业" prop="homework"></el-table-column>
      <el-table-column label="大作业" prop="finalProject"></el-table-column>
      <el-table-column label="总分" prop="total"></el-table-column>
    </el-table>
    <!-- ECharts柱状图 -->
    <div ref="chart" style="width:100%;height:400px;margin-top:20px;"></div>
  </div>
</template>

<script>
import { getMyScore } from '../api/score'
import echarts from 'echarts'
export default {
  name: "StudentScore",
  data() {
    return { scoreList: [], chart: null }
  },
  mounted() {
    this.getData()
  },
  methods: {
    async getData() {
      const res = await getMyScore()
      this.scoreList = res.data
      this.renderChart()
    },
    renderChart() {
      this.chart = echarts.init(this.$refs.chart)
      const xData = this.scoreList.map(item => item.courseName)
      const yData = this.scoreList.map(item => item.total)
      this.chart.setOption({
        xAxis: { type: 'category', data: xData },
        yAxis: { type: 'value' },
        series: [{ type: 'bar', data: yData }]
      })
    }
  }
}
</script>

<style scoped>

</style>