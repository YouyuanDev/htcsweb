package com.htcsweb.dao;



import com.htcsweb.entity.OdCoating3LpeProcess;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface OdCoating3LpeProcessDao {
    public OdCoating3LpeProcess getOdCoating3LpeProcessById(int id);
    public int addOdCoating3LpeProcess(OdCoating3LpeProcess odCoating3LpeProcess);
    public int delOdCoating3LpeProcess(String[] arrId);
    public List<OdCoating3LpeProcess> getOdCoatingProcess();
    public int updateOdCoating3LpeProcess(OdCoating3LpeProcess odCoating3LpeProcess);
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no, @Param("skip") int skip, @Param("take") int take);
    //public int getCount();
    public int getCountAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time,@Param("mill_no") String mill_no);
    //获取3lpe外涂记录pdf
    public List<HashMap<String,Object>>getOd3LPECoatRecord(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取当天外防腐3LPE总数
    //public int getODCoating3LPECount(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //根据钢管编号查询最新一条记录
    public OdCoating3LpeProcess getRecentRecordByPipeNo(@Param("pipe_no")String pipe_no);


    //根据钢管编号查询之前的最新一条合格记录
    public OdCoating3LpeProcess getLastAcceptedRecordBeforePipeNo(@Param("pipe_no")String pipe_no,@Param("mill_no")String mill_no);

    //得到钢管后10根涂层记录管号，并且记录为待定状态 10
    public List<HashMap<String,Object>> getNextTenPipesBeforePipeNo(@Param("pipe_no")String pipe_no, @Param("mill_no")String mill_no);

}
