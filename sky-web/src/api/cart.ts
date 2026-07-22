import { http } from './client'
import type { CartItem } from '../types'

export const getCart = () => http.get<CartItem[]>('/user/shoppingCart/list')

export const addToCart = (body: { dishId?: number; setmealId?: number; dishFlavor?: string }) =>
  http.post<void>('/user/shoppingCart/add', body)

export const subFromCart = (body: { dishId?: number; setmealId?: number }) =>
  http.post<void>('/user/shoppingCart/sub', body)

export const cleanCart = () => http.delete<void>('/user/shoppingCart/clean')
