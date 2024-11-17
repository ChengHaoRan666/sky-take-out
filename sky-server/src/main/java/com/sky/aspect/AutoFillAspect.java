package com.sky.aspect;

import com.sky.annotaion.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @Author: 程浩然
 * @Create: 2024/11/16 - 16:32
 * @Description: 自定义切面，实现公共字段自动填充逻辑
 */
@Aspect
@Slf4j
@Component
public class AutoFillAspect {
    /**
     * 切入点<br>
     * execution(* com.sky.mapper.*.*(..))：找到对应类的全部方法都进行切面<br>
     * @annotation(com.sky.annotaion.AutoFill) 找到加上了AutoFill注解的方法进行切面
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotaion.AutoFill)")
    public void autoFillPointCut() {}

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充...");
        // 1. 获取被拦截的方法上的数据库操作的类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class); // 获取方法上的注解对象
        OperationType operationType = autoFill.value(); // 获取数据库操作类型

        // 2. 获取当前被拦截方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }

        Object entity = args[0]; // 规定第一个是要填充的对象

        // 3. 为实体对象的公共属性进行赋值
        LocalDateTime time = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        // 3.1 如果操作是插入操作，需要对四个值都赋值
        if (operationType == OperationType.INSERT) {
            try {
                // 3.1.1 获取方法
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 3.1.2 通过反射为对象赋值
                setCreateTime.invoke(entity, time);
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, time);
                setUpdateUser.invoke(entity, currentId);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        // 3.2 如果操作是修改操作，需要对两个值进行赋值
        else if (operationType == OperationType.UPDATE) {
            try {
                // 3.2.1 获取方法
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 3.2.2 通过反射为对象赋值
                setUpdateTime.invoke(entity, time);
                setUpdateUser.invoke(entity, currentId);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
