package com.htcsweb.dao;


import com.htcsweb.entity.IdCoatingInspectionProcess;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface IdCoatingInspectionProcessDao {
    public IdCoatingInspectionProcess getIdCoatingInProcessById(int id);
    public int addIdCoatingInProcess(IdCoatingInspectionProcess idCoatingInspectionProcess);
    public int delIdCoatingInProcess(String[] arrId);
    public int updateIdCoatingInProcess(IdCoatingInspectionProcess idCoatingInspectionProcess);
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no, @Param("skip") int skip, @Param("take") int take);
    //public int getCount();

    public int getCountAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no);
    //获取内涂检验记录PDF
    public List<HashMap<String,Object>>getIdCoatInspectionRecord(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取当天内防待扒皮数量
    public int getTotalIdWastePipe(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
}
