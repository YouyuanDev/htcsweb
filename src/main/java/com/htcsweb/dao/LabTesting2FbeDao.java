package com.htcsweb.dao;


import com.htcsweb.entity.LabTesting2Fbe;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface LabTesting2FbeDao {
//    public LabTesting2Fbe getLabTest2FbeById(int id);
    public int addLabTest2Fbe(LabTesting2Fbe labTesting2Fbe);
    public int delLabTest2Fbe(String[] arrId);
    public List<LabTesting2Fbe> getLabTest2Fbe();
    public int updateLabTest2Fbe(LabTesting2Fbe labTesting2Fbe);
    public List<HashMap<String,Object>> getNewAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("skip") int skip, @Param("take") int take);
    public int getCountNewAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time);
    public List<HashMap<String,Object>>getCoverLabTestingInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    public LabTesting2Fbe getRecentRecordByPipeNo(@Param("pipe_no") String pipe_no);

}
