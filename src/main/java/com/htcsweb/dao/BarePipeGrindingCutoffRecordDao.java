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
    //获取光管隔离数量(4.11)
    public int getTotalBarePipeGrindCutoffOfTime(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取光管隔离修磨数量(4.11)
    public int getTotalBarePipeGrindOfTime(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取光管隔离切割数量(4.11)
    public int getTotalBarePipeCutoffOfTime(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取光管隔离信息(4.11)
    public List<BarePipeGrindingCutoffRecord>getBarePipeCutoffOfTime(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取瑕疵管需切斜的个数
    public int getTotalCutoffOfBare(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
}