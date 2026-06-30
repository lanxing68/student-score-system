import request from '../utils/request'
// 单条录入成绩
export function enterScore(data) {
    return request({
        url: '/api/score/enter',
        method: 'post',
        data
    })
}
// Excel批量导入成绩
export function batchScore(file) {
    const formData = new FormData()
    formData.append('file', file)
    return request({
        url: '/api/score/batch',
        method: 'post',
        data: formData
    })
}
// 学生查询自己成绩
export function getMyScore() {
    return request({
        url: '/api/score/my',
        method: 'get'
    })
}
// 修改成绩
export function editScore(id, data) {
    return request({
        url: `/api/score/${id}`,
        method: 'put',
        data
    })
}
// 查询课程排名
export function getCourseRank(courseId) {
    return request({
        url: `/api/rank/course/${courseId}`,
        method: 'get'
    })
}