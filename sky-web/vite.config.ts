import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server:{
    proxy:{
      // 将 /api 开头的请求代理到 http://localhost:8080
      '/api':{
        target:'http://localhost:8080',
        changeOrigin:true,
        // 重写路径，将 /api 替换为空字符串
        rewrite:(path)=>path.replace(/^\/api/,'')
      }
    }
  }
})
