import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Badge, Button, Card, Group, Image, SimpleGrid, Stack, Tabs, Text, Title } from '@mantine/core'
import type { Category, DishVO } from '../types'
import { getCategories, getDishes } from '../api/dish'
import { addToCart } from '../api/cart'
import { useAuth } from '../auth/AuthContext'
import { useCart } from '../cart/CartContext'

export default function MenuPage() {
  const [categories, setCategories] = useState<Category[]>([])
  const [active, setActive] = useState<string | null>(null) // Tabs 的 value 是字符串
  const [dishes, setDishes] = useState<DishVO[]>([])
  const { user } = useAuth()
  const { refresh } = useCart()
  const nav = useNavigate()

  // 进页面拉分类（type=1 菜品分类），默认选中第一个
  useEffect(() => {
    getCategories(1).then((list) => {
      setCategories(list)
      if (list.length > 0) setActive(String(list[0].id))
    })
  }, [])

  // 选中分类变化时拉菜品
  useEffect(() => {
    if (active) getDishes(Number(active)).then(setDishes)
  }, [active])

  const onAdd = async (dishId: number) => {
    if (!user) {
      nav('/login') // 未登录先去登录
      return
    }
    await addToCart({ dishId })
    await refresh() // 更新导航栏购物车徽标
  }

  return (
    <Stack gap="lg">
      <Title order={2}>Menu</Title>

      <Tabs value={active} onChange={setActive} variant="pills" color="orange">
        <Tabs.List>
          {categories.map((c) => (
            <Tabs.Tab key={c.id} value={String(c.id)}>
              {c.name}
            </Tabs.Tab>
          ))}
        </Tabs.List>
      </Tabs>

      <SimpleGrid cols={{ base: 1, sm: 2, md: 3 }}>
        {dishes.map((d) => (
          <Card key={d.id} withBorder shadow="sm" padding="lg">
            <Card.Section>
              <Image
                src={d.image}
                h={160}
                alt={d.name}
                fallbackSrc="https://placehold.co/400x240?text=Dish"
              />
            </Card.Section>

            <Group justify="space-between" mt="md">
              <Text fw={600}>{d.name}</Text>
              <Badge color="orange" variant="light" size="lg">
                €{d.price}
              </Badge>
            </Group>

            <Text size="sm" c="dimmed" lineClamp={2} mt={4} mih={40}>
              {d.description}
            </Text>

            <Button fullWidth mt="md" onClick={() => onAdd(d.id)}>
              Add to cart
            </Button>
          </Card>
        ))}
      </SimpleGrid>
    </Stack>
  )
}
