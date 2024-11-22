package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @Author: 程浩然
 * @Create: 2024/11/22 - 9:39
 * @Description: c端用户相关操作接口
 */
public interface UserService {
    /**
     * c端用户登录
     *
     * @param userLoginDTO 用户登录参数
     * @return 登录成功参数code
     */
    User login(UserLoginDTO userLoginDTO);
}
