package com.htcsweb.dao;


import com.htcsweb.entity.OdCoatingProcess;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface OdCoatingProcessDao {
    public OdCoatingProcess getOdCoatingProcessById(int id);
    public int addOdCoatingProcess(OdCoatingProcess odCoatingProcess);
    public int delOdCoatingProcess(String[]arrId);
    public List<OdCoatingProcess> getOdCoatingProcess();
    public int updateOdCoatingProcess(OdCoatingProcess odCoatingProcess);
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no")String operator_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("mill_no")String mill_no, @Param("skip")int skip, @Param("take")int take);
    //public int getCount();

    public int getCountAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no")String operator_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("mill_no")String mill_no);
    //获取2fbe外涂记录
    public List<HashMap<String,Object>>getOd2FBECoatRecord(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取当天外防腐2FBE总数
    //public int getODCoating2FBECount(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
}
