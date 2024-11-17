package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotaion.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 添加员工
     *
     * @param employee 待添加员工信息
     * @return 1表示成功 0或其他表示失败
     */
    @Insert("insert into employee " +
            "(name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user)" +
            " values (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser});")
    @AutoFill(value = OperationType.INSERT)
    Integer addEmployee(Employee employee);


    /**
     * 分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 修改员工属性
     */
    @AutoFill(value = OperationType.UPDATE)
    Integer status(Employee employee);


    /**
     * 通过id查员工信息
     *
     * @param id
     * @return
     */
    @Select("select * from employee where id = #{id}")
    Employee getById(String id);
}