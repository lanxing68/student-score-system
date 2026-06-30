<template>
  <!-- 唯一外层根容器，满足单根节点规则 -->
  <div class="login-box">
    <el-card title="系统登录" style="width:400px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
// 替换@为相对路径，解决找不到模块报错
import { login } from '../api/auth'
export default {
  // 修改为多单词组件名，消除eslint警告
  name: "LoginPage",
  data() {
    return {
      form: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    async handleLogin() {
      const res = await login(this.form)
      if (res.code === 200) {
        localStorage.setItem('token', res.data.token)
        // 根据角色跳转
        if (res.data.role === 'STUDENT') {
          this.$router.push('/studentScore')
        } else {
          this.$router.push('/teacherScore')
        }
      }
    }
  }
}
</script>

<style scoped>
.login-box {
  display: flex;
  justify-content: center;
  margin-top: 100px;
}
</style>