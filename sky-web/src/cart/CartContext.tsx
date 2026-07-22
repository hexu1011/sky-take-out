import { createContext, useContext, useState, useCallback, type ReactNode } from 'react'
import { getCart } from '../api/cart'
import type { CartItem } from '../types'

interface CartState {
  items: CartItem[]
  count: number // 所有菜品数量之和
  refresh: () => Promise<void>
}

const CartContext = createContext<CartState>(null!)

export function CartProvider({ children }: { children: ReactNode }) {
  const [items, setItems] = useState<CartItem[]>([])

  // 拉最新购物车（未登录时后端会 401，拦截器已处理，这里静默失败即可）
  const refresh = useCallback(async () => {
    try {
      const list = await getCart()
      setItems(list)
    } catch {
      setItems([])
    }
  }, [])

  const count = items.reduce((sum, i) => sum + i.number, 0)

  return (
    <CartContext.Provider value={{ items, count, refresh }}>
      {children}
    </CartContext.Provider>
  )
}

// eslint-disable-next-line react-refresh/only-export-components
export const useCart = () => useContext(CartContext)
