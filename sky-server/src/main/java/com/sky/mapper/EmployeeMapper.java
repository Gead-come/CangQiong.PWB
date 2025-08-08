package com.sky.mapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
     * 插入员工数据
     *
     * @param employee
     */
    @Insert("insert into employee values (null,#{name},#{username},#{password},#{phone},#{sex},#{idNumber}," +
            " #{status},#{createTime},#{updateTime},#{createUser},#{updateUser})"
    )
    void insert(Employee employee);

    /**
     * 分页查询
     * @param name
     * @return
     */
    Page<Employee> list(String name);


    /**
     * 更新员工，启用禁用员工账号
     * @param employee
     * @return
     */
    void update(Employee employee);
    /**
     * 根据id查询员工
     * @param id
     * @return
     */

    /**
     * 编辑员工信息
     * 1 设置回显
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * 编辑员工信息
     * 2 修改员工信息
     * @param
     * @return
     */

}


