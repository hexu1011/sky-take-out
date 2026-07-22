import { createContext, useContext, useState, type ReactNode } from 'react'
import type { UserLoginVO } from '../types'

// 这个"全局登录态"对外提供什么
interface AuthState {
  user: { id: number; name: string } | null // 当前用户，没登录是 null
  setAuth: (vo: UserLoginVO) => void         // 登录/注册成功后调它
  logout: () => void
}

const AuthContext = createContext<AuthState>(null!)

// Provider：把登录态"供给"包在它里面的所有组件
export function AuthProvider({ children }: { children: ReactNode }) {
  // 初始值从 localStorage 恢复 → 刷新页面不掉登录
  const [user, setUser] = useState<AuthState['user']>(() => {
    const raw = localStorage.getItem('user')
    return raw ? JSON.parse(raw) : null
  })

  const setAuth = (vo: UserLoginVO) => {
    localStorage.setItem('token', vo.token) // 给 API 拦截器读
    localStorage.setItem('user', JSON.stringify({ id: vo.id, name: vo.name }))
    setUser({ id: vo.id, name: vo.name })
  }

  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, setAuth, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

// 快捷方式：任何组件里 useAuth() 就能拿到登录态
// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => useContext(AuthContext)
