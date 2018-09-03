package com.htcsweb.dao;

import com.htcsweb.entity.Person;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
@Repository
public interface PersonDao {
   public Person getPersonById(int id);

   //根据姓名和工号获取人员名单(小页面使用)
   public  List<Person>getNoByName(@Param("pname")String pname,@Param("employee_no")String employee_no);

   //根据编号查找操作工信息
   public  List<Person>getPersonByEmployeeNo(@Param("employee_no")String employee_no);

   //分页模糊查询操作工信息
   public List<HashMap<String,Object>> getAllByLike(@Param("employee_no")String employee_no, @Param("pname")String pname, @Param("skip")int skip, @Param("take")int take);

   //模糊查询操作工信息总数
   public int getCountAllByLike(@Param("employee_no")String employee_no, @Param("pname")String pname);

   //验证登录密码
   public int confirmPersonByEmployeeNoPassword(@Param("employee_no")String employee_no,@Param("ppassword") String ppassword);

   //修改操作工信息
   public int updatePerson(Person person);

   //增加操作工信息
   public int addPerson(Person person);

   //删除操作工信息
   public int delPerson(String[]arrId);



}
