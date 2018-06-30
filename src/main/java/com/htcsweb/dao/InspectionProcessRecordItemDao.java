package com.htcsweb.dao;

import com.htcsweb.entity.InspectionProcessRecordItem;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface InspectionProcessRecordItemDao {

    public int addInspectionProcessRecordItem(InspectionProcessRecordItem inspectionProcessRecordItem);
    public int updateInspectionProcessRecordItem(InspectionProcessRecordItem inspectionProcessRecordItem);
    public int delInspectionProcessRecordItem(String[]arrId);


    public InspectionProcessRecordItem getInspectionProcessRecordItemByHeaderCodeAndItemCode(@Param("inspection_process_record_header_code") String inspection_process_record_header_code,@Param("item_code") String item_code);

    public List<InspectionProcessRecordItem> getInspectionProcessRecordItemByInspectionProcessRecordHeaderCode(@Param("inspection_process_record_header_code") String inspection_process_record_header_code);

}
