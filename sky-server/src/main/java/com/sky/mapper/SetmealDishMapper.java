package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品ids查询关联的套餐
     */
    List<Long> getByDishIds(List<Long> dishIds);
}
