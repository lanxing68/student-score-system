import request from '../utils/request'
// 学生分页列表
export function getStudentList(params) {
    return request({
        url: '/api/student/list',
        method: 'get',
        params
    })
}
// 新增学生
export function addStudent(data) {
    return request({
        url: '/api/student',
        method: 'post',
        data
    })
}
// 修改学生
export function updateStudent(id, data) {
    return request({
        url: `/api/student/${id}`,
        method: 'put',
        data
    })
}
// 删除学生
export function delStudent(id) {
    return request({
        url: `/api/student/${id}`,
        method: 'delete'
    })
}
// Excel导入学生
export function importStudent(file) {
    const formData = new FormData()
    formData.append('file', file)
    return request({
        url: '/api/student/import',
        method: 'post',
        data: formData
    })
}