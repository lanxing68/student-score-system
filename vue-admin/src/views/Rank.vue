<template>
  <div class="rank-wrap">
    <h3>成绩排名</h3>
    <el-select v-model="courseId" placeholder="选择课程" @change="loadRank">
      <el-option v-for="c in courseList" :key="c.id" :label="c.courseName" :value="c.id"></el-option>
    </el-select>
    <el-table :data="rankList" border style="margin-top:10px">
      <el-table-column label="排名" prop="rank"></el-table-column>
      <el-table-column label="学号" prop="studentNo"></el-table-column>
      <el-table-column label="姓名" prop="studentName"></el-table-column>
      <el-table-column label="总分" prop="total"></el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getCourseList } from '../api/course'
import { getCourseRank } from '../api/score'
export default {
  name: "ScoreRank",
  data() {
    return {
      courseList: [],
      courseId: null,
      rankList: []
    }
  },
  mounted() {
    this.loadCourse()
  },
  methods: {
    async loadCourse() {
      const res = await getCourseList({ pageNum: 1, pageSize: 999 })
      this.courseList = res.data.list
    },
    async loadRank() {
      const res = await getCourseRank(this.courseId)
      this.rankList = res.data
    }
  }
}
</script>

<style scoped>

</style>