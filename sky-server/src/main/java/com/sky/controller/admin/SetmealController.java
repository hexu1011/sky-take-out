package com.sky.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import com.sky.dto.SetmealDTO;
import com.sky.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新建套餐
     * 
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新建套餐")
    public Result add(@RequestBody SetmealDTO setmealDTO) {
        log.info("新建套餐：{}", setmealDTO);

        setmealService.add(setmealDTO);

        return Result.success();
    }

    /**
     * 套餐分页查询
     * 
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "套餐分页查询")
    public Result page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询：{}", setmealPageQueryDTO);

        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 套餐批量删除
     */
    @DeleteMapping
    @ApiOperation(value = "套餐批量删除")
    public Result deleteBatch(@RequestParam List<Long> ids) {
        log.info("套餐批量删除：{}", ids);

        setmealService.deleteBatch(ids);

        return Result.success();
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改套餐")
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐：{}", setmealDTO);

        setmealService.update(setmealDTO);

        return Result.success();
    }

    /**
     * 根据id查询套餐
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询套餐")
    public Result getById(@PathVariable Long id) {
        log.info("根据id查询套餐：{}", id);

        SetmealVO setmealVO = setmealService.getById(id);

        return Result.success(setmealVO);
    }

    /**
     * 启售停售套餐
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "启售停售套餐")
    public Result startOrStop(@PathVariable Integer status, @RequestParam Long id) {
        log.info("启售停售套餐：status={}, id={}", status, id);

        setmealService.startOrStop(status, id);

        return Result.success();
    }
}
