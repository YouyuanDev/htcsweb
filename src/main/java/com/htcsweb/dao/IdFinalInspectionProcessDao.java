package com.htcsweb.dao;

import com.htcsweb.entity.IdFinalInspectionProcess;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface IdFinalInspectionProcessDao {
    public IdFinalInspectionProcess getIdFinalInProcessById(int id);
    public int addIdFinalInProcess(IdFinalInspectionProcess idFinalInspectionProcess);
    public int delIdFinalInProcess(String[] arrId);
    public int updateIdFinalInProcess(IdFinalInspectionProcess idFinalInspectionProcess);
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no, @Param("skip") int skip, @Param("take") int take);
    //public int getCount();
    public int getCountAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no);
    //获取内涂终检记录PDF
    public List<HashMap<String,Object>>getIdCoatFinalInspectionRecord(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取当天内防腐合格总数和合格长度
    public List<HashMap<String,Object>> getIDCoatingAcceptedInfo(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("result")String result);
    //获取内防腐支数(4.11)
    public int getIDCoatingCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取内防腐合格数(4.11)
    public int getIDCoatingAcceptedCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

}
