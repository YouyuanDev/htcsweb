package com.htcsweb.dao;

import com.htcsweb.entity.OdCoating3LpeInspectionProcess;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface OdCoating3LpeInspectionProcessDao {
    public OdCoating3LpeInspectionProcess getOdCoatingInProcessById(int id);
    public int addOdCoating3LpeInProcess(OdCoating3LpeInspectionProcess odCoating3LpeInspectionProcess);
    public int delOdCoating3LpeInProcess(String[] arrId);
    public int updateOdCoating3LpeInProcess(OdCoating3LpeInspectionProcess odCoating3LpeInspectionProcess);
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no,@Param("skip") int skip, @Param("take") int take);
    public int getCountAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time,@Param("mill_no") String mill_no);
    //获取3lpe外涂检验记录
    public List<HashMap<String,Object>>getOd3LPECoatInspectionRecord(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取当天3lpe外防待扒皮数量
    public int getTotalOdWastePipe(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
}

