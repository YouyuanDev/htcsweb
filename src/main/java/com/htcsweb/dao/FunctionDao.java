package com.htcsweb.dao;

import com.htcsweb.entity.Function;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface FunctionDao {

    //根据权限编号和名称查询权限列表
    public List<HashMap<String,Object>>getAllByNoName(@Param("function_no")String function_no, @Param("function_name")String function_name);

    //分页模糊查询权限信息
    public List<HashMap<String,Object>> getAllByLike(@Param("function_no")String function_no, @Param("function_name")String function_name, @Param("skip")int skip, @Param("take")int take);

    //模糊查询权限信息总数
    public int getCountAllByLike(@Param("function_no")String function_no, @Param("function_name")String function_name);

    //修改权限
    public int updateFunction(Function function);

    //增加权限
    public int addFunction(Function function);

    //删除权限
    public int delFunction(String[]arrId);

    //根据权限编号获取权限信息
    public List<Function> getFunctionByFunctionNo(@Param("function_no")String function_no);
}
