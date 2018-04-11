package com.htcsweb.dao;


import com.htcsweb.entity.PipeSamplingRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface PipeSamplingRecordDao {

    public int addPipeSamplingRecord(PipeSamplingRecord pipeSamplingRecord);
    public int delPipeSamplingRecord(String[] arrId);
    public int updatePipeSamplingRecord(PipeSamplingRecord pipeSamplingRecord);
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time,@Param(value = "mill_no")String mill_no, @Param("skip") int skip, @Param("take") int take);
    public int getCountAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time,@Param(value = "mill_no")String mill_no);
    public List<PipeSamplingRecord>getPipeSamplingInfo(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取封面上的试验管信息
    public List<HashMap<String,Object>>getCoverPipeSamplingInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取样管需切斜个数
    public int getTotalCutOffOfSample(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
}
