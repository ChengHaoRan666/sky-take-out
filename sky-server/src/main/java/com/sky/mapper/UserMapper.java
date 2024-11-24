package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: 程浩然
 * @Create: 2024/11/22 - 10:09
 * @Description: 用户mapper接口
 */
@Mapper
public interface UserMapper {

    /**
     * 通过openId获取用户信息
     *
     * @param openId 唯一凭证
     * @return 用户
     */
    @Select("select * from user where openid = #{openId};")
    User getByOpenId(String openId);

    /**
     * 插入
     *
     * @param user 用户信息
     */
    void insert(User user);

    /**
     * 通过id查询user
     * @param userId 用户Id
     * @return
     */
    @Select("select * from user where id = #{userId};")
    User getById(Long userId);
}
