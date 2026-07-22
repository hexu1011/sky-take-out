import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import { MantineProvider } from '@mantine/core'
import '@mantine/core/styles.css'
import './index.css'
import { theme } from './theme'
import { AuthProvider } from './auth/AuthContext'
import { CartProvider } from './cart/CartContext'
import App from './App.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <MantineProvider theme={theme}>   {/* 组件库主题 */}
      <BrowserRouter>                  {/* 路由能力 */}
        <AuthProvider>                 {/* 全局登录态 */}
          <CartProvider>               {/* 全局购物车计数 */}
            <App />
          </CartProvider>
        </AuthProvider>
      </BrowserRouter>
    </MantineProvider>
  </StrictMode>,
)
