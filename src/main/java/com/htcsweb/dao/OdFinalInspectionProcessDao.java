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
    public List<HashMap<String,Object>>getOdFianlInspectionRecord(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取当天外防腐合格总数与合格长度
    public List<HashMap<String,Object>> getODCoatingAcceptedInfo(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("result")String result);
    //获取外防腐支数(4.11)
    public int getODCoatingCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取外防腐合格支数(4.11)
    public int getODCoatingAcceptedCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //根据钢管编号查询最新一条记录
    public OdFinalInspectionProcess getRecentRecordByPipeNo(@Param("pipe_no")String pipe_no);
}
