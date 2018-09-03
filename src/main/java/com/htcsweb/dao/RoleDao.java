package com.htcsweb.dao;

import com.htcsweb.entity.Function;
import com.htcsweb.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface RoleDao {

    //根据角色编号和角色名称查询所有角色信息
    public List<HashMap<String,Object>>getAllRoleByLike(@Param("role_no")String role_no, @Param("role_name")String role_name);

    //分页模糊查询角色信息
    public List<HashMap<String,Object>> getAllByLike(@Param("role_no")String role_no, @Param("role_name")String role_name, @Param("skip")int skip, @Param("take")int take);

    //模糊查询角色信息总数
    public int getCountAllByLike(@Param("role_no")String role_no, @Param("role_name")String role_name);

    //修改角色信息
    public int updateRole(Role role);

    //增加角色信息
    public int addRole(Role role);

    //删除角色信息
    public int delRole(String[]arrId);

    //根据角色编号得到角色信息
    public List<Role> getRoleByRoleNo(@Param("role_no")String role_no);

    //根据事件返回所有角色信息
    public List<HashMap<String,Object>> getRolesByEvent(@Param("event")String event);

}
