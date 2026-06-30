import axios from 'axios'
import router from '@/router'

const service = axios.create({
    // 后端端口是8081，原8080改成8081，末尾加上/api
    baseURL: 'http://localhost:8081',
    timeout: 10000
})

// 请求拦截器：自动携带Token
service.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})

// 响应拦截器：统一处理错误码
service.interceptors.response.use(
    response => response.data,
    error => {
        if (!error.response) {
            alert('网络异常，请检查后端服务')
            return Promise.reject(error)
        }
        const status = error.response.status
        if (status === 401) {
            alert('登录过期，请重新登录')
            localStorage.removeItem('token')
            router.push('/login')
        } else if (status === 403) {
            alert('暂无权限操作')
        } else if (status === 400) {
            alert(error.response.data.msg)
        }
        return Promise.reject(error)
    }
)

export default service