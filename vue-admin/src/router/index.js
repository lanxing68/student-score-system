import Vue from 'vue'
import VueRouter from 'vue-router'

import Login from '@/views/Login.vue'
import StudentManage from '@/views/StudentManage.vue'
import CourseManage from '@/views/CourseManage.vue'
import TeacherScore from '@/views/TeacherScore.vue'
import StudentScore from '@/views/StudentScore.vue'
import Rank from '@/views/Rank.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/studentManage',
        name: 'StudentManage',
        component: StudentManage
    },
    {
        path: '/courseManage',
        name: 'CourseManage',
        component: CourseManage
    },
    {
        path: '/teacherScore',
        name: 'TeacherScore',
        component: TeacherScore
    },
    {
        path: '/studentScore',
        name: 'StudentScore',
        component: StudentScore
    },
    {
        path: '/rank',
        name: 'Rank',
        component: Rank
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router