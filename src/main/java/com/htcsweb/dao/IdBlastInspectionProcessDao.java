package com.htcsweb.dao;

import com.htcsweb.entity.IdBlastInspectionProcess;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface IdBlastInspectionProcessDao {
    public IdBlastInspectionProcess getIdBlastInProcessById(int id);
    public int addIdBlastInProcess(IdBlastInspectionProcess idBlastInspectionProcess);
    public List<IdBlastInspectionProcess> getIdBlastInProcess();
    public int  updateIdBlastInProcess(IdBlastInspectionProcess idBlastInspectionProcess);
    //public int getCount();

    public int getCountAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no);
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no, @Param("skip") int skip, @Param("take") int take);
    public int delIdBlastInspectionProcess(String[] arrId);
}
