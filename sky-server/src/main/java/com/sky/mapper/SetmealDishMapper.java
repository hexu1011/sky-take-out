package com.sky.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;
import com.sky.entity.SetmealDish;
import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品ids查询关联的套餐
     */
    List<Long> getByDishIds(List<Long> dishIds);

    /**
     * 新增套餐菜品关系
     * 
     * @param setmealDish
     */
    void insert(SetmealDish setmealDish);

    /**
     * 根据套餐id删除套餐菜品关系
     * 
     * @param id
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deleteBySetmealId(Long id);

    /**
     * 根据套餐id查询套餐菜品关系
     * 
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getBySetmealId(Long id);
}
