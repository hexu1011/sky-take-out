export interface Result<T> {
  code: number    
  msg: string | null
  data: T
}

export interface UserLoginVO { id: number; name: string; token: string }
export interface LoginDTO { email: string; password: string }
export interface RegisterDTO { email: string; password: string; name: string }

export interface Category { id: number; name: string; type: number }

export interface DishFlavor { id: number; name: string; value: string }
export interface DishVO {
  id: number; name: string; price: number
  image: string; description: string
  flavors?: DishFlavor[]
}

export interface CartItem {
  id: number; name: string; image: string
  dishId?: number; setmealId?: number
  number: number; amount: number
}