import { useState, type FormEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Anchor, Button, Paper, PasswordInput, Stack, Text, TextInput, Title } from '@mantine/core'
import { login } from '../api/auth'
import { useAuth } from '../auth/AuthContext'

export default function LoginPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const { setAuth } = useAuth()
  const nav = useNavigate()

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault() // 阻止表单默认刷新
    setError('')
    try {
      const vo = await login({ email, password }) // 拆信封后就是 {id,name,token}
      setAuth(vo)
      nav('/')
    } catch (err) {
      setError((err as Error).message) // 显示 "密码错误" 等
    }
  }

  return (
    <Paper maw={380} mx="auto" mt={60} p="xl" withBorder shadow="sm">
      <Title order={2} ta="center" mb="lg">
        Log in
      </Title>
      <form onSubmit={onSubmit}>
        <Stack>
          <TextInput
            label="Email"
            placeholder="you@example.com"
            value={email}
            onChange={(e) => setEmail(e.currentTarget.value)}
            required
          />
          <PasswordInput
            label="Password"
            placeholder="Your password"
            value={password}
            onChange={(e) => setPassword(e.currentTarget.value)}
            required
          />
          {error && (
            <Text c="red" size="sm">
              {error}
            </Text>
          )}
          <Button type="submit" fullWidth>
            Log in
          </Button>
        </Stack>
      </form>
      <Text ta="center" size="sm" mt="md" c="dimmed">
        Don&apos;t have an account?{' '}
        <Anchor component={Link} to="/register" fw={500}>
          Register
        </Anchor>
      </Text>
    </Paper>
  )
}
