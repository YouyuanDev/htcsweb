package com.htcsweb.dao;


import com.htcsweb.entity.LabTesting3Lpe;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface LabTesting3LpeDao {
    //    public LabTesting2Fbe getLabTest2FbeById(int id);
    public int addLabTest3Lpe(LabTesting3Lpe labTesting3Lpe);
    public int delLabTest3Lpe(String[] arrId);
    public List<LabTesting3Lpe> getLabTest2Fbe();
    public int updateLabTest3Lpe(LabTesting3Lpe labTesting3Lpe);
    public List<HashMap<String,Object>> getNewAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("skip") int skip, @Param("take") int take);
    public int getCountNewAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time);
    public List<HashMap<String,Object>>getCoverLabTestingInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    public LabTesting3Lpe getRecentRecordByPipeNo(@Param("pipe_no")String pipe_no);
}
