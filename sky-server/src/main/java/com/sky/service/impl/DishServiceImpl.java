package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新建菜品
     * 
     * @param dishDTO
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 向菜品表插入1条数据
        dishMapper.insert(dish);

        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            // 向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分页查询
     * 
     * @param dishPageQueryDTO
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 删除菜品
     * 
     * @param ids
     */
    public void delete(List<Long> ids) {

        // 先检查菜品状态，不能删除正在售卖的菜品
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 再检查菜品是否关联了套餐，关联了套餐就不能删除
        List<Long> setmealIds = setmealDishMapper.getByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 删除菜品表中的数据和对应的口味表中的数据
        for (Long id : ids) {
            dishMapper.deleteById(id);
            dishFlavorMapper.deleteByDishId(id);
        }

    }

    /**
     * 修改菜品
     * 
     * @param dishDTO
     */
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 更新菜品表中的数据
        dishMapper.update(dish);
        // 先删除原有的口味数据，再插入口味表中新的数据
        dishFlavorMapper.deleteByDishId(dish.getId());
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 根据id查询菜品
     * 
     * @param id
     * @return
     */
    public DishVO getById(Long id) {
        // 查询菜品基本信息
        Dish dish = dishMapper.getById(id);
        // 查询菜品对应的口味信息
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    /**
     * 根据分类id查询菜品列表
     * 
     * @param categoryId
     * @return
     */
    public List<DishVO> list(Long categoryId) {
        List<Dish> dishes = dishMapper.listByCategoryId(categoryId);
        List<DishVO> dishVOList = new ArrayList<>();
        for (Dish dish : dishes) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(dish, dishVO);
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(dish.getId());
            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }
        return dishVOList;
    }

    /**
     * 菜品起售、停售
     * 
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        Dish dish = dishMapper.getById(id);
        dish.setStatus(status);
        dishMapper.update(dish);
    }

    /**
     * 条件查询菜品和口味
     * 
     * @param dish
     * @return
     */
    // TODO
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.listByCategoryId(dish.getCategoryId());

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);

            // 根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
