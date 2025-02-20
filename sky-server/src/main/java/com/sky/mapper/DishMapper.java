package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotaion.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Author: 程浩然
 * @Create: 2024/11/16 - 14:57
 * @Description: 菜品Mapper接口
 */

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);


    /**
     * 通过id查询菜品表
     *
     * @param id 菜品id
     * @return 菜品
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);


    /**
     * 添加菜品
     *
     * @param dish 菜品
     */
    @AutoFill(OperationType.INSERT)
    void addDish(Dish dish);

    /**
     * 通过分类id查找菜品
     *
     * @param categoryId 分类id
     * @return 菜品集合
     */
    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> getByCategoryId(Long categoryId);

    /**
     * 根据条件分页查询
     *
     * @param dishPageQueryDTO 查询条件
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id修改菜品
     *
     * @param dish 菜品信息
     */
    @AutoFill(OperationType.UPDATE)
    void changeDish(Dish dish);

    /**
     * 根据id删除菜品
     *
     * @param id 菜品id
     */
    @Delete("delete from dish where id = #{id};")
    void deleteByDishId(Long id);

    /**
     * 修改菜品状态
     *
     * @param id
     * @param status
     */
    @Update("update dish set status = #{status},update_time = #{updateTime}, update_user = #{updateUser} where  id = #{id};")
    void status(Long id, Long status, LocalDateTime updateTime, Long updateUser);

    /**
     * 根据id删除菜品
     *
     * @param ids 菜品id
     */
    void deleteByDishIds(List<Long> ids);

    /**
     * 通过套餐id查询所有菜品
     * @param id 套餐id
     * @return 菜品集合
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long id);


    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
