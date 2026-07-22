import { useEffect } from 'react'
import { Link } from 'react-router-dom'
import { ActionIcon, Button, Card, Divider, Group, Image, Stack, Text, Title } from '@mantine/core'
import { addToCart, cleanCart, subFromCart } from '../api/cart'
import { useCart } from '../cart/CartContext'
import type { CartItem } from '../types'

export default function CartPage() {
  const { items, refresh } = useCart()

  // 进页面拉最新购物车
  useEffect(() => {
    refresh()
  }, [refresh])

  // 一个购物车项要么是菜品要么是套餐，取对应 id 作为增减参数
  const keyOf = (i: CartItem) => (i.dishId ? { dishId: i.dishId } : { setmealId: i.setmealId })

  const inc = async (i: CartItem) => {
    await addToCart(keyOf(i))
    await refresh()
  }
  const dec = async (i: CartItem) => {
    await subFromCart(keyOf(i))
    await refresh()
  }
  const clear = async () => {
    await cleanCart()
    await refresh()
  }

  const total = items.reduce((sum, i) => sum + Number(i.amount) * i.number, 0)

  if (items.length === 0) {
    return (
      <Stack align="center" mt={80} gap="sm">
        <Text fz={48}>🛒</Text>
        <Text c="dimmed">Your cart is empty</Text>
        <Button component={Link} to="/">
          Browse menu
        </Button>
      </Stack>
    )
  }

  return (
    <Stack maw={640} mx="auto" gap="sm">
      <Title order={2}>Shopping Cart</Title>

      {items.map((i) => (
        <Card key={i.id} withBorder padding="sm">
          <Group justify="space-between" wrap="nowrap">
            <Group wrap="nowrap">
              <Image src={i.image} w={56} h={56} radius="sm" fallbackSrc="https://placehold.co/56?text=%20" />
              <div>
                <Text fw={500}>{i.name}</Text>
                <Text size="sm" c="dimmed">
                  €{i.amount}
                </Text>
              </div>
            </Group>

            <Group gap="xs" wrap="nowrap">
              <ActionIcon variant="default" onClick={() => dec(i)} aria-label="decrease">
                −
              </ActionIcon>
              <Text w={24} ta="center">
                {i.number}
              </Text>
              <ActionIcon variant="default" onClick={() => inc(i)} aria-label="increase">
                +
              </ActionIcon>
            </Group>
          </Group>
        </Card>
      ))}

      <Divider my="xs" />

      <Group justify="space-between">
        <Text fw={700} size="lg">
          Total: €{total}
        </Text>
        <Button variant="light" color="red" onClick={clear}>
          Clear
        </Button>
      </Group>
    </Stack>
  )
}
