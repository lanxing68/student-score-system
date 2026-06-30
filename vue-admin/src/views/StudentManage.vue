<template>
  <div class="student-wrap">
    <h3>学生管理</h3>
    <el-button type="primary" @click="openAdd">新增学生</el-button>
    <el-button @click="uploadFile">批量导入Excel</el-button>
    <input type="file" ref="fileInput" hidden @change="handleImport" />

    <el-table :data="tableData" border style="margin-top:10px">
      <el-table-column label="学号" prop="studentNo"></el-table-column>
      <el-table-column label="姓名" prop="name"></el-table-column>
      <el-table-column label="班级" prop="className"></el-table-column>
      <el-table-column label="创建时间" prop="createTime"></el-table-column>
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog :visible.sync="dialogVisible" title="学生信息">
      <el-form :model="form">
        <el-form-item label="姓名">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="班级">
          <el-input v-model="form.className"></el-input>
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
import { getStudentList, addStudent, updateStudent, delStudent, importStudent } from '../api/student'
export default {
  name: "StudentManage",
  data() {
    return {
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      dialogVisible: false,
      form: { name: '', className: '', id: null }
    }
  },
  mounted() {
    this.loadList()
  },
  methods: {
    async loadList() {
      const res = await getStudentList({ pageNum: this.pageNum, pageSize: this.pageSize })
      this.tableData = res.data.list
      this.total = res.data.total
    },
    handlePageChange(p) { this.pageNum = p; this.loadList() },
    handleSizeChange(s) { this.pageSize = s; this.loadList() },
    openAdd() {
      this.form = { name: '', className: '', id: null }
      this.dialogVisible = true
    },
    openEdit(row) {
      this.form = { ...row }
      this.dialogVisible = true
    },
    async submit() {
      if (this.form.id) {
        await updateStudent(this.form.id, this.form)
      } else {
        await addStudent(this.form)
      }
      this.dialogVisible = false
      this.loadList()
    },
    async del(id) {
      await delStudent(id)
      this.loadList()
    },
    uploadFile() {
      this.$refs.fileInput.click()
    },
    async handleImport(e) {
      const file = e.target.files[0]
      await importStudent(file)
      this.loadList()
    }
  }
}
</script>

<style scoped>

</style>