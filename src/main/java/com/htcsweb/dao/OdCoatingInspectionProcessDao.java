package com.htcsweb.dao;

import com.htcsweb.entity.OdCoatingInspectionProcess;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface OdCoatingInspectionProcessDao {
    public OdCoatingInspectionProcess getOdCoatingInProcessById(int id);
    public int addOdCoatingInProcess(OdCoatingInspectionProcess odCoatingInspectionProcess);
    public int delOdCoatingInProcess(String[]arrId);
    public int updateOdCoatingInProcess(OdCoatingInspectionProcess odCoatingInspectionProcess);
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no")String operator_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("mill_no")String mill_no, @Param("skip")int skip, @Param("take")int take);
    //public int getCount();
    public int getCountAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no")String operator_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("mill_no")String mill_no);
    //获取2fbe外涂检验记录
    public List<HashMap<String,Object>>getOd2FBECoatInspectionRecord(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取当天2FBE外防待扒皮数量
    public int getTotalOdWastePipe(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取2FBE固化度试验管号
    public List<HashMap<String,Object>>getCover2FBEDscTestingInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //根据钢管编号查询最新一条记录
    public OdCoatingInspectionProcess getRecentRecordByPipeNo(@Param("pipe_no")String pipe_no);
}

