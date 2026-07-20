package com.sky.controller.admin;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.redis.core.RedisTemplate;
import com.sky.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/admin/shop")
@Slf4j
@Tag(name = "商户相关接口")
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @Operation(summary = "商户营业状态设置")
    public Result setStatus(@PathVariable Integer status) {
        log.info("商户营业状态设置：{}", status);

        redisTemplate.opsForValue().set(KEY, status);

        return Result.success();
    }

    @GetMapping("/status")
    @Operation(summary = "商户营业状态查询")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        return Result.success(status);
    }
}
