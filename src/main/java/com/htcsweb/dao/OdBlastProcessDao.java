package com.htcsweb.dao;


import com.htcsweb.entity.OdBlastProcess;
import com.htcsweb.entity.ProjectInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OdBlastProcessDao {
    public OdBlastProcess getOdBlastProcessById(int id);
    public int addOdBlastProcess(OdBlastProcess odBlastProcess);
    public int delOdBlastProcess(String[]arrId);
    public List<OdBlastProcess> getOdBlastProcess();
    public int updateOdBlastProcess(OdBlastProcess odBlastProcess);
//    public List<OdBlastProcess>getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no")String operator_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time, @Param("skip")int skip, @Param("take")int take);
    public List<HashMap<String,Object>> getNewAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no")String operator_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time, @Param("skip")int skip, @Param("take")int take);
    //public int getCount();


    public int getCountNewAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no")String operator_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

}
