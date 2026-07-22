import { http } from './client'
import type { Category, DishVO } from '../types'

// 查询分类：type=1 菜品分类，type=2 套餐分类
export const getCategories = (type: number) =>
  http.get<Category[]>('/user/category/list', { params: { type } })

// 按分类查菜品
export const getDishes = (categoryId: number) =>
  http.get<DishVO[]>('/user/dish/list', { params: { categoryId } })
