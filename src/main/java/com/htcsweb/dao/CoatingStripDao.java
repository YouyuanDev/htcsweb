package com.htcsweb.dao;

import org.apache.ibatis.annotations.Param;
import com.htcsweb.entity.CoatingStrip;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface CoatingStripDao {


    public List<HashMap<String,Object>> getCoatingStripInfoByLike(@Param("pipe_no")String pipe_no, @Param("operator_no")String operator_no, @Param("project_no") String project_no, @Param("contract_no")String contract_no,  @Param("begin_time") Date begin_time, @Param("end_time") Date end_time,@Param("mill_no")String mill_no, @Param("skip")int skip, @Param("take")int take);
    public int getCountCoatingStripInfoByLike(@Param("pipe_no")String pipe_no, @Param("operator_no")String operator_no,@Param("project_no") String project_no,  @Param("contract_no")String contract_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time,@Param("mill_no")String mill_no);

    public int addCoatingStrip(CoatingStrip coatingStrip);
    public int delCoatingStrip(String[]arrId);
    public int updateCoatingStrip(CoatingStrip coatingStrip);
    //获取扒皮数量
    public int getTotalBarePipeGrindingInfo(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
}
