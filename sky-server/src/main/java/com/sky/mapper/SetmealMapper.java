package com.sky.mapper;

/**
 * @Author: 程浩然
 * @Create: 2024/11/16 - 14:58
 * @Description: 套餐Mapper接口
 */

import com.github.pagehelper.Page;
import com.sky.annotaion.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     *
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 添加套餐
     *
     * @param setmeal 套餐
     */
    @AutoFill(OperationType.INSERT)
    void addSetmeal(Setmeal setmeal);

    /**
     * 根据id查询套餐
     *
     * @param id 套餐id
     * @return 套餐VO
     */
    @Select("select * from setmeal where id = #{id};")
    Setmeal getById(Long id);

    /**
     * 分页查询
     *
     * @param setmealPageQueryDTO 分页查询参数
     * @return 页面page
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 修改套餐状态
     *
     * @param id     套餐id
     * @param status 要修改的状态
     * @param time   修改时间
     * @param userId 修改人id
     */
    @Update("update setmeal set status = #{status}, update_time = #{time}, update_user = #{id} where id = #{id}")
    void status(Long id, Long status, LocalDateTime time, Long userId);

    /**
     * 根据id删除套餐
     * @param ids 套餐id集合
     */
    void deleteById(List<Long> ids);
}

