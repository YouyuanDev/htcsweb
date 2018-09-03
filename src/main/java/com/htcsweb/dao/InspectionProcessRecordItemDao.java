package com.htcsweb.dao;

import com.htcsweb.entity.InspectionProcessRecordItem;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface InspectionProcessRecordItemDao {

    //添加检验记录
    public int addInspectionProcessRecordItem(InspectionProcessRecordItem inspectionProcessRecordItem);

    //修改检验记录
    public int updateInspectionProcessRecordItem(InspectionProcessRecordItem inspectionProcessRecordItem);

    //删除检验记录
    public int delInspectionProcessRecordItem(String[]arrId);

    //根据检验记录编号和检验项目编号获取检验记录
    public InspectionProcessRecordItem getInspectionProcessRecordItemByHeaderCodeAndItemCode(@Param("inspection_process_record_header_code") String inspection_process_record_header_code,@Param("item_code") String item_code);

    //根据检验记录编号获取检验记录
    public List<InspectionProcessRecordItem> getInspectionProcessRecordItemByInspectionProcessRecordHeaderCode(@Param("inspection_process_record_header_code") String inspection_process_record_header_code);

    //更新涂敷等待时间
    public int updateElapsedTime(@Param("inspection_process_record_header_code") String inspection_process_record_header_code,@Param("item_code") String item_code,@Param("item_value") String item_value);

    //根据钢管编号查询之前的最新一条合格记录
    public List<InspectionProcessRecordItem> getLastAcceptedRecordBeforePipeNo(@Param("pipe_no")String pipe_no, @Param("mill_no")String mill_no, @Param("process_code")String process_code);

}
