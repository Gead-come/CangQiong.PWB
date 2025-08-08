package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
       password =  DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param dst
     */

    @Override
    public void addEmp(EmployeeDTO dst) {

        Employee employee = new Employee();
        // 对象属性拷贝
        BeanUtils.copyProperties(dst,employee);
        //1,补充：设置账号的状态，默认正常状态 1表示正常，0表示锁定，时间等

        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD .getBytes()));
        employee.setStatus(StatusConstant.ENABLE); // 默认正常状态
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //TODO:设置当前登录用户的ID
        // 从当前登录用户中(ThreadLocal)获取登陆人ID
        employee.setCreateUser(BaseContext.getCurrentId());  // 默认创建人
        employee.setUpdateUser(BaseContext.getCurrentId());   // 默认修改人
        //调用mapper的新增方法，将员工对象存入employee表中
        employeeMapper.insert(employee);

    }

    /**
     * 分页查询 -- 用分页插件
     * @param dto
     * @return
     */
    @Override
    public PageResult page(EmployeePageQueryDTO dto) {
        // 1 设置分页参数
        PageHelper.startPage(dto.getPage(),dto.getPageSize());

        // 2 调用mapper
     Page<Employee>page =  employeeMapper.list(dto.getName());

        // 3 封装PageResult并返回
        return  new PageResult(page.getTotal(),page.getResult());
    }

}
