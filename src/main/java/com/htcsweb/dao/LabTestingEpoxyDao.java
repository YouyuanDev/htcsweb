package com.htcsweb.dao;


import com.htcsweb.entity.LabTestingEpoxy;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface LabTestingEpoxyDao {
    //    public LabTesting2Fbe getLabTest2FbeById(int id);
    public int addLabTestEpoxy(LabTestingEpoxy labTestingEpoxy);
    public int delLabTestEpoxy(String[] arrId);
    public List<LabTestingEpoxy> getLabTest2Fbe();
    public int updateLabTestEpoxy(LabTestingEpoxy labTestingEpoxy);
    public List<HashMap<String,Object>> getNewAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("skip") int skip, @Param("take") int take);
    public int getCountNewAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time);
    //获取内防封面试验信息
    public List<HashMap<String,Object>>getCoverLabTestingInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
}
