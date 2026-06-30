<template>
  <div class="teacher-score-wrap">
    <h3>教师成绩录入</h3>
    <el-select v-model="courseId" placeholder="选择课程" @change="loadStudent">
      <el-option v-for="c in courseList" :key="c.id" :label="c.courseName" :value="c.id"></el-option>
    </el-select>
    <el-button style="margin-left:10px" @click="uploadExcel">批量导入成绩</el-button>
    <input ref="file" hidden type="file" @change="importScore" />

    <el-table :data="studentList" border style="margin-top:10px">
      <el-table-column label="学号" prop="studentNo"></el-table-column>
      <el-table-column label="姓名" prop="name"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button @click="openScoreDialog(scope.row)">录入成绩</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 成绩录入弹窗 -->
    <el-dialog title="录入成绩" :visible.sync="scoreDialog">
      <el-form :model="scoreForm">
        <el-form-item label="课堂表现">
          <el-input-number v-model="scoreForm.classPerformance" :min="0" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item label="实验">
          <el-input-number v-model="scoreForm.experiment" :min="0" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item label="课后作业">
          <el-input-number v-model="scoreForm.homework" :min="0" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item label="大作业">
          <el-input-number v-model="scoreForm.finalProject" :min="0" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item label="评分类型">
          <el-select v-model="scoreForm.scoreType">
            <el-option label="正常 NORMAL" value="NORMAL"></el-option>
            <el-option label="特殊 SPECIAL" value="SPECIAL"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="scoreDialog=false">取消</el-button>
        <el-button type="primary" @click="submitScore">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getCourseList } from '../api/course'
import { getStudentList } from '../api/student'
import { enterScore, batchScore } from '../api/score'
export default {
  name: "TeacherScore",
  data() {
    return {
      courseList: [],
      courseId: null,
      studentList: [],
      scoreDialog: false,
      scoreForm: {
        studentId: null,
        courseId: null,
        classPerformance: 0,
        experiment: 0,
        homework: 0,
        finalProject: 0,
        scoreType: 'NORMAL'
      }
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
    async loadStudent() {
      const res = await getStudentList({ pageNum: 1, pageSize: 999 })
      this.studentList = res.data.list
    },
    openScoreDialog(row) {
      this.scoreForm = {
        studentId: row.id,
        courseId: this.courseId,
        classPerformance: 0,
        experiment: 0,
        homework: 0,
        finalProject: 0,
        scoreType: 'NORMAL'
      }
      this.scoreDialog = true
    },
    async submitScore() {
      await enterScore(this.scoreForm)
      this.scoreDialog = false
    },
    uploadExcel() {
      this.$refs.file.click()
    },
    async importScore(e) {
      const file = e.target.files[0]
      await batchScore(file)
    }
  }
}
</script>

<style scoped>

</style>