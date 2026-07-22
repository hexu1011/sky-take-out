import { http } from './client'
import type { UserLoginVO, LoginDTO, RegisterDTO } from '../types'

export const login = (dto: LoginDTO) =>
  http.post<UserLoginVO>('/user/user/login', dto)

export const register = (dto: RegisterDTO) =>
  http.post<UserLoginVO>('/user/user/register', dto)
