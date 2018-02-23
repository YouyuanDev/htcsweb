package com.htcsweb.dao;

import com.htcsweb.entity.OdFinalInspectionProcess;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface OdFinalInspectionProcessDao {
    public OdFinalInspectionProcess getOdFinalInProcessById(int id);
    public int addOdFinalInProcess(OdFinalInspectionProcess odFinalInspectionProcess);
    public int delOdFinalInProcess(String[]arrId);
    public int updateOdFinalInProcess(OdFinalInspectionProcess odFinalInspectionProcess);
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no")String operator_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time, @Param("skip")int skip, @Param("take")int take);
    public int getCount();
}
