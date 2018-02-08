package com.htcsweb.dao;

import com.htcsweb.entity.Person;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
@Repository
public interface PersonDao {
   public Person getPersonById(int id);
   public void addPerson(Person person);
   public List<Person>getPerson();
   public void  updatePerson(Person person);
   public  List<Person>getNoByName(@Param("pname")String pname);
   public List<Person>getAllByLike(@Param("pname")String pname,@Param("skip")int skip, @Param("take")int take);
   public int getCount();
   public int confirmPersonByEmployeeNoPassword(@Param("employee_no")String employee_no,@Param("ppassword") String ppassword);
}
