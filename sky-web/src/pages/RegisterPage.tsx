import { useState, type FormEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Anchor, Button, Paper, PasswordInput, Stack, Text, TextInput, Title } from '@mantine/core'
import { register } from '../api/auth'
import { useAuth } from '../auth/AuthContext'

export default function RegisterPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [name, setName] = useState('')
  const [error, setError] = useState('')
  const { setAuth } = useAuth()
  const nav = useNavigate()

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setError('')
    try {
      const vo = await register({ email, password, name })
      setAuth(vo) // 后端注册即返回 token（注册即登录）
      nav('/')
    } catch (err) {
      setError((err as Error).message) // 如 "邮箱已被注册"
    }
  }

  return (
    <Paper maw={380} mx="auto" mt={60} p="xl" withBorder shadow="sm">
      <Title order={2} ta="center" mb="lg">
        Register
      </Title>
      <form onSubmit={onSubmit}>
        <Stack>
          <TextInput
            label="Name"
            placeholder="Your name"
            value={name}
            onChange={(e) => setName(e.currentTarget.value)}
            required
          />
          <TextInput
            label="Email"
            placeholder="you@example.com"
            value={email}
            onChange={(e) => setEmail(e.currentTarget.value)}
            required
          />
          <PasswordInput
            label="Password"
            placeholder="Choose a password"
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
            Register
          </Button>
        </Stack>
      </form>
      <Text ta="center" size="sm" mt="md" c="dimmed">
        Already have an account?{' '}
        <Anchor component={Link} to="/login" fw={500}>
          Log in
        </Anchor>
      </Text>
    </Paper>
  )
}
