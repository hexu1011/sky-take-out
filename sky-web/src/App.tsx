import { useEffect } from 'react'
import { Link, Route, Routes } from 'react-router-dom'
import { Anchor, Box, Button, Container, Group, Indicator, Text } from '@mantine/core'
import { useAuth } from './auth/AuthContext'
import { useCart } from './cart/CartContext'
import MenuPage from './pages/MenuPage'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import CartPage from './pages/CartPage'

export default function App() {
  const { user, logout } = useAuth()
  const { count, refresh } = useCart()

  // 登录态变化（含刷新页面后从 localStorage 恢复）时，拉一次购物车计数
  useEffect(() => {
    if (user) refresh()
  }, [user, refresh])

  return (
    <>
      <Box
        component="header"
        bg="white"
        style={{
          borderBottom: '1px solid var(--mantine-color-gray-2)',
          position: 'sticky',
          top: 0,
          zIndex: 100,
        }}
      >
        <Container size="lg">
          <Group h={60} justify="space-between">
            <Group gap="lg">
              <Text component={Link} to="/" fw={800} size="lg" c="orange" style={{ textDecoration: 'none' }}>
                🍜 Sky Eats
              </Text>
              <Anchor component={Link} to="/" c="dark" fw={500}>
                Menu
              </Anchor>
              <Indicator label={count} size={18} color="red" offset={4} disabled={count === 0}>
                <Anchor component={Link} to="/cart" c="dark" fw={500}>
                  Cart
                </Anchor>
              </Indicator>
            </Group>

            <Group gap="sm">
              {user ? (
                <>
                  <Text size="sm" c="dimmed">
                    Hi, {user.name}
                  </Text>
                  <Button variant="light" size="xs" onClick={logout}>
                    Log out
                  </Button>
                </>
              ) : (
                <Button component={Link} to="/login" size="xs">
                  Log in
                </Button>
              )}
            </Group>
          </Group>
        </Container>
      </Box>

      <Container size="lg" py="xl">
        <Routes>
          <Route path="/" element={<MenuPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/cart" element={<CartPage />} />
        </Routes>
      </Container>
    </>
  )
}
