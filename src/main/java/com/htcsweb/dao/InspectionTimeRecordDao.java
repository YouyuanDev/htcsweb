package com.htcsweb.dao;

import com.htcsweb.entity.InspectionTimeRecord;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface InspectionTimeRecordDao {

    public int addInspectionTimeRecord(InspectionTimeRecord inspectionTimeRecord);
    public int delInspectionTimeRecord(String[] arrId);
    public List<InspectionTimeRecord> getRecordByProjectNoMillNo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("inspection_item")String inspection_item);
    public int updateInspectionTimeRecord(InspectionTimeRecord inspectionTimeRecord);

}
