package com.htcsweb.dao;

import com.htcsweb.entity.OdBlastInspectionProcess;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface OdBlastInspectionProcessDao {
    public OdBlastInspectionProcess getOdBlastInProcessById(int id);
    public int addOdBlastInProcess(OdBlastInspectionProcess odBlastInspectionProcess);
    public List<OdBlastInspectionProcess> getOdBlastInProcess();
    public int  updateOdBlastInProcess(OdBlastInspectionProcess odBlastInspectionProcess);
    public List<OdBlastInspectionProcess>feny(int pagesize, int rows);
    public int getCount();
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no")String operator_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time, @Param("skip")int skip, @Param("take")int take);
    public int delOdBlastInspectionProcess(String[]arrId);
}
