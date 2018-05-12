package com.htcsweb.dao;

import com.htcsweb.entity.BarePipeGrindingCutoffRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface BarePipeGrindingCutoffRecordDao {


    public int addBarePipeGrindingCutoffRecord(BarePipeGrindingCutoffRecord barePipeGrindingCutoffRecord);
    public int delBarePipeGrindingCutoffRecord(String[] arrId);
    //public List<BarePipeGrindingCutoffRecord> getBarePipeGrindingCutoffRecord();
    public int updateBarePipeGrindingCutoffRecord(BarePipeGrindingCutoffRecord barePipeGrindingCutoffRecord);
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no, @Param("skip") int skip, @Param("take") int take);
    //public int getCount();
    public int getCountAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no);
    //获取光管隔离数量
    public int getBarePipeOnholdCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取光管隔离修磨数量
    public int getBarePipeGrindingCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取光管隔离切割数量
    public int getBarePipeCutoffCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取光管隔离切割及修磨信息
    public List<BarePipeGrindingCutoffRecord>getBarePipeGrindingCutoffInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取瑕疵管需倒棱的个数
    public int getNeedRebevelPipeCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
}