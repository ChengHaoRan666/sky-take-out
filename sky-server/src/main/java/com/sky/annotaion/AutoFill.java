package com.sky.annotaion;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 程浩然
 * @Create: 2024/11/16 - 16:27
 * @Description: 自定义注解，用于表示某个方法需要进行功能字段自动填充处理
 */
// 表示可以加在方法上
@Target(ElementType.METHOD)
// 标记注解在运行时保留
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    // 数据库操作类型 UPDATE INSERT
    OperationType value();
}
