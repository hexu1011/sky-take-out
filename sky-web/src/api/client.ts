import axios, {
  type AxiosResponse,
  type AxiosError,
  type AxiosRequestConfig,
  type InternalAxiosRequestConfig,
} from 'axios'
import type { Result } from '../types'

const rawHttp = axios.create({ baseURL: '/api' })

// 请求拦截器：自动加 token
rawHttp.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = localStorage.getItem('token')
  if (token) config.headers.set('authentication', token)
  return config
})

// 响应拦截器：拆掉 {code,msg,data} 信封
rawHttp.interceptors.response.use(
  (response: AxiosResponse<Result<unknown>>) => {
    const body = response.data
    if (body.code === 1) return body.data as unknown as AxiosResponse
    return Promise.reject(new Error(body.msg ?? '请求失败'))
  },
  (error: AxiosError) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  },
)

// 重新声明 http 的类型：get/post 直接解析成 T（拦截器已拆信封）
interface Http {
  get<T>(url: string, config?: AxiosRequestConfig): Promise<T>
  post<T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T>
  put<T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T>
  delete<T>(url: string, config?: AxiosRequestConfig): Promise<T>
}
export const http = rawHttp as unknown as Http