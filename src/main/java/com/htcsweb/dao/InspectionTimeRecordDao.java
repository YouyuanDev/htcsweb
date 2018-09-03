package com.htcsweb.dao;

import com.htcsweb.entity.InspectionTimeRecord;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface InspectionTimeRecordDao {

    //添加检验时间记录
    public int addInspectionTimeRecord(InspectionTimeRecord inspectionTimeRecord);

    //删除检验时间记录
    public int delInspectionTimeRecord(String[] arrId);

    //根据项目编号、分厂号和检验项目号获取检验时间记录
    public List<InspectionTimeRecord> getRecordByProjectNoMillNo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("inspection_item")String inspection_item);

    //修改检验时间记录
    public int updateInspectionTimeRecord(InspectionTimeRecord inspectionTimeRecord);

    //根据钢管编号和分厂号获取检验时间记录
    public List<InspectionTimeRecord> getRecordByPipeNoMillNo(@Param("pipe_no")String pipe_no,@Param("mill_no")String mill_no,@Param("inspection_item")String inspection_item);


}
