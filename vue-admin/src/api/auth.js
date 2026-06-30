import request from '../utils/request'
// 登录接口（队友A）
export function login(data) {
    return request({
        url: '/auth/login',
        method: 'post',
        data
    })
}