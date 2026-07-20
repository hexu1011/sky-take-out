package com.sky.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Tag(name = "商户相关接口")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    public void getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("商户营业状态查询：{}", status);
    }

}
