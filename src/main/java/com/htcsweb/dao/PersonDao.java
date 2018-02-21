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
   public void addPerson(Person person);
   public List<Person>getPerson();
   public void  updatePerson(Person person);

   //小页面显示人员名单
   public  List<Person>getNoByName(@Param("pname")String pname,@Param("employee_no")String employee_no);

   //模糊搜索带分页
   public List<HashMap<String,Object>> getAllByLike(@Param("employee_no")String employee_no, @Param("pname")String pname, @Param("skip")int skip, @Param("take")int take);
   public int getCount();
   //验证登录密码
   public int confirmPersonByEmployeeNoPassword(@Param("employee_no")String employee_no,@Param("ppassword") String ppassword);





}
