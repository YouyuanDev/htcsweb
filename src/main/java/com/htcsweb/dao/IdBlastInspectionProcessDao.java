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


    //根据内涂的信息查询对应的内打砂检验信息
    public List<HashMap<String,Object>>getIdBlastInfoByCoatingInfo(@Param("pipe_no")String pipe_no,@Param("id")int id);
    //更新内打砂检验等待时间
    public int updateElapsedTime(@Param("elapsed_time")float elapsed_time,@Param("id")int id);

    //获取内打砂检验记录PDF
    public List<HashMap<String,Object>>getIdBlastInspectionRecord(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //根据钢管编号查询最新一条记录
    public IdBlastInspectionProcess getRecentRecordByPipeNo(@Param("pipe_no")String pipe_no);
}
