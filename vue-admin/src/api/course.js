import request from '../utils/request'
// 课程分页
export function getCourseList(params) {
    return request({
        url: '/api/course/list',
        method: 'get',
        params
    })
}
// 新增课程
export function addCourse(data) {
    return request({
        url: '/api/course',
        method: 'post',
        data
    })
}
// 修改课程
export function updateCourse(id, data) {
    return request({
        url: `/api/course/${id}`,
        method: 'put',
        data
    })
}
// 删除课程
export function delCourse(id) {
    return request({
        url: `/api/course/${id}`,
        method: 'delete'
    })
}
// 获取教师下拉
export function getTeacherOptions() {
    return request({
        url: '/api/course/teachers',
        method: 'get'
    })
}