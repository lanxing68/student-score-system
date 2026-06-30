<template>
  <!-- 唯一外层根div，满足Vue单根要求 -->
  <div class="course-wrap">
    <h3>课程管理</h3>
    <el-button type="primary" @click="openAdd">新增课程</el-button>

    <el-table :data="tableData" border style="margin-top:10px">
      <el-table-column label="课程名称" prop="courseName"></el-table-column>
      <el-table-column label="学期" prop="term"></el-table-column>
      <el-table-column label="授课教师" prop="teacherName"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button @click="openEdit(scope.row)">编辑</el-button>
          <el-button type="danger" @click="del(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
        :current-page="pageNum"
        :page-size="pageSize"
        :total="total"
    ></el-pagination>

    <!-- 弹窗 -->
    <el-dialog :visible.sync="dialogVisible" title="课程信息">
      <el-form :model="form">
        <el-form-item label="课程名称">
          <el-input v-model="form.courseName"></el-input>
        </el-form-item>
        <el-form-item label="学期">
          <el-input v-model="form.term"></el-input>
        </el-form-item>
        <el-form-item label="授课教师">
          <el-select v-model="form.teacherId">
            <el-option v-for="t in teacherList" :key="t.id" :label="t.username" :value="t.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getCourseList, addCourse, updateCourse, delCourse, getTeacherOptions } from '../api/course'
export default {
  name: "CourseManage",
  data() {
    return {
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      teacherList: [],
      dialogVisible: false,
      form: { courseName: '', term: '', teacherId: null, id: null }
    }
  },
  mounted() {
    //this.loadList()
    //this.getTeachers()
  },
  methods: {
    async loadList() {
      const res = await getCourseList({ pageNum: this.pageNum, pageSize: this.pageSize })
      this.tableData = res.data.list
      this.total = res.data.total
    },
    async getTeachers() {
      const res = await getTeacherOptions()
      this.teacherList = res.data
    },
    handlePageChange(p) { this.pageNum = p; this.loadList() },
    handleSizeChange(s) { this.pageSize = s; this.loadList() },
    openAdd() {
      this.form = { courseName: '', term: '', teacherId: null, id: null }
      this.dialogVisible = true
    },
    openEdit(row) {
      this.form = { ...row }
      this.dialogVisible = true
    },
    async submit() {
      if (this.form.id) {
        await updateCourse(this.form.id, this.form)
      } else {
        await addCourse(this.form)
      }
      this.dialogVisible = false
      this.loadList()
    },
    async del(id) {
      await delCourse(id)
      this.loadList()
    }
  }
}
</script>

<style scoped>

</style>