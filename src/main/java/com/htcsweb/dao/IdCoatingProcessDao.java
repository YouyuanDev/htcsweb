package com.htcsweb.dao;


import com.htcsweb.entity.IdCoatingProcess;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface IdCoatingProcessDao {
    public IdCoatingProcess getIdCoatingProcessById(int id);
    public int addIdCoatingProcess(IdCoatingProcess idCoatingProcess);
    public int delIdCoatingProcess(String[] arrId);
    public List<IdCoatingProcess> getIdCoatingProcess();
    public int updateIdCoatingProcess(IdCoatingProcess idCoatingProcess);
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no, @Param("skip") int skip, @Param("take") int take);
    //public int getCount();
    public int getCountAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time,@Param("mill_no") String mill_no);
    //获取内涂记录PDF
    public List<HashMap<String,Object>>getIdCoatRecord(@Param("project_no")String project_no,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
}
