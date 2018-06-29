package com.htcsweb.dao;

import com.htcsweb.entity.DynamicMeasurementItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DynamicMeasurementItemDao {

    public int addDynamicMeasurementItem(DynamicMeasurementItem dynamicMeasurementItem);
    public int updateDynamicMeasurementItem(DynamicMeasurementItem dynamicMeasurementItem);
    public int delDynamicMeasurementItem(String[]arrId);

    //根据检验标准编号得到检验项目
    public List<DynamicMeasurementItem> getDynamicMeasurementItemByAcceptanceCriteriaNo(@Param("acceptance_criteria_no") String acceptance_criteria_no);

    //根据管号编号工序编号得到检验项目
    public List<DynamicMeasurementItem> getDynamicItemByPipeNoProcessCode(@Param("pipe_no") String pipe_no ,@Param("process_code") String process_code);

}
