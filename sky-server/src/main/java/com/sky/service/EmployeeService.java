package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);


    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    void addEmployee(EmployeeDTO employeeDTO);

    /**
     * 分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 修改员工账号状态
     */
    void status(Long id, int status);

    /**
     * 修改员工信息
     *
     * @param employeeDTO
     */
    void EditInformation(EmployeeDTO employeeDTO);

    /**
     * 根据id查询员工信息
     *
     * @param id
     * @return
     */
    Employee getEmployeeById(String id);

    /**
     * 修改密码
     *
     * @param passwordEditDTO
     */
    void changePassword(PasswordEditDTO passwordEditDTO);
}
