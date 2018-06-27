package com.htcsweb.dao;

import com.htcsweb.entity.InspectionProcessRecordHeader;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface InspectionProcessRecordHeaderDao {

    public int addInspectionProcessRecordHeader(InspectionProcessRecordHeader inspectionProcessRecordHeader);
    public int updateInspectionProcessRecordHeader(InspectionProcessRecordHeader inspectionProcessRecordHeader);
    public int delInspectionProcessRecordHeader(String[]arrId);

    public List<HashMap<String,Object>> getAllByLike(@Param("process_code") String process_code,@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no, @Param("skip") int skip, @Param("take") int take);

    public int getCountAllByLike(@Param("process_code") String process_code,@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no);

    public List<HashMap<String,Object>>  getProcessRecord(@Param("process_code") String process_code,@Param("project_no") String project_no,@Param("mill_no") String mill_no,@Param("od") String od,@Param("wt") String wt, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time);

    //根据钢管编号查询上一根合格记录
    public InspectionProcessRecordHeader getLastAcceptedRecordBeforePipeNo(@Param("process_code") String process_code,@Param("pipe_no")String pipe_no,@Param("mill_no")String mill_no);

    //根据钢管编号查询最新一条记录
    public InspectionProcessRecordHeader getRecentRecordByPipeNo(@Param("process_code") String process_code,@Param("pipe_no")String pipe_no);
}
