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
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no")String operator_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("mill_no")String mill_no, @Param("skip")int skip, @Param("take")int take);
    //public int getCount();
    public int getCountAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no")String operator_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("mill_no")String mill_no);
    //获取外涂终检记录
    public List<HashMap<String,Object>>getOdFianlInspectionRecord(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取当天外防腐合格总数
    public List<HashMap<String,Object>> getTotalQualifiedOfDay(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("result")String result);
}
