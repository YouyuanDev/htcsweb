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
    //public int getCount();
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no")String operator_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("mill_no")String mill_no, @Param("skip")int skip, @Param("take")int take);
    public int delOdBlastInspectionProcess(String[]arrId);
    public int getCountAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no")String operator_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time, @Param("mill_no")String mill_no);
    //获取当天外打砂记录
    public List<OdBlastInspectionProcess> getOdBlastInspectionRecord(@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //更新等待时间
//    public int updateElapsedTime(@Param("begin_time")Date begin_time,@Param("end_time")Date end_time,@Param("id")int id);
    public int updateElapsedTime(@Param("elapsed_time")float elapsed_time,@Param("id")int id);
    //根据3lpe外涂的信息查询对应的外打砂信息
    public List<HashMap<String,Object>>getOdBlastInfoBy3lpeCoatingInfo(@Param("pipe_no")String pipe_no,@Param("id")int id);
    //根据2fbe外涂的信息查询对应的外打砂信息
    public List<HashMap<String,Object>>getOdBlastInfoBy2fbeCoatingInfo(@Param("pipe_no")String pipe_no,@Param("id")int id);

}
