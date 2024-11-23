package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: 程浩然
 * @Create: 2024/11/23 - 8:43
 * @Description: 购物车Mapper层
 */
@Mapper
public interface shoppingCartMapper {

    /**
     * 根据Cart查找购物车表中是否有
     *
     * @param shoppingCart 购物车信息
     * @return
     */
    public List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 更新购物车数量
     *
     * @param shoppingCart 购物车信息
     */
    @Update("update shopping_cart set number = #{number} where id = #{id};")
    void updateNumber(ShoppingCart shoppingCart);

    /**
     * 根据购物车信息插入
     *
     * @param shoppingCart 购物车信息
     */
    @Insert("insert into shopping_cart (name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time) " +
            " values (#{name},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);


    /**
     * 根据userId删除购物车信息
     * @param userId 用户id
     */
    @Delete("delete from shopping_cart where user_id = #{userId};")
    void clear(Long userId);
}
