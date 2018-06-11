package com.htcsweb.dao;

import com.htcsweb.entity.CoatingRepair;
import org.apache.ibatis.annotations.Param;


import java.util.Date;
import java.util.HashMap;
import java.util.List;


public interface CoatingRepairDao {


    public List<HashMap<String,Object>> getCoatingRepairInfoByLike(@Param("pipe_no")String pipe_no, @Param("operator_no")String operator_no,@Param("project_no") String project_no,  @Param("contract_no")String contract_no, @Param("mill_no")String mill_no,@Param("status")String status, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("skip")int skip, @Param("take")int take);
    public int getCountCoatingRepairInfoByLike(@Param("pipe_no")String pipe_no, @Param("operator_no")String operator_no,@Param("project_no") String project_no,  @Param("contract_no")String contract_no, @Param("mill_no")String mill_no,@Param("status")String status, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time);

    public int addCoatingRepair(CoatingRepair coatingRepair);
    public int delCoatingRepair(String[]arrId);
    public int updateCoatingRepair(CoatingRepair coatingRepair);
    //获取涂层修补数
    public int getCoatingRepairCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取修补管信息（4.11）
    public List<CoatingRepair>getCoatingRepairInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //根据钢管编号查询修补记录
    public List<CoatingRepair> getRecentRecordByPipeNo(@Param("pipe_no")String pipe_no);



}
