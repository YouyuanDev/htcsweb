package com.htcsweb.dao;

import com.htcsweb.entity.OdStencilProcess;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface OdStencilProcessDao {
    public OdStencilProcess getOdStencilProcessById(int id);
    public int addOdStencilProcess(OdStencilProcess odStencilProcess);
    public int delOdStencilProcess(String[]arrId);
    public int updateOdStencilProcess(OdStencilProcess odStencilProcess);
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no")String operator_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time, @Param("skip")int skip, @Param("take")int take);
    //public int getCount();
    public int getCountAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no")String operator_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

}
