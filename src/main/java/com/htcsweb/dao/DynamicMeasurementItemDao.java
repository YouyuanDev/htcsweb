package com.htcsweb.dao;

import com.htcsweb.entity.DynamicMeasurementItem;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface DynamicMeasurementItemDao {

    //添加动态测量项
    public int addDynamicMeasurementItem(DynamicMeasurementItem dynamicMeasurementItem);

    //更新动态测量项
    public int updateDynamicMeasurementItem(DynamicMeasurementItem dynamicMeasurementItem);

    //删除动态测量项
    public int delDynamicMeasurementItem(String[]arrId);

    //根据检验标准编号得到检验项目
    public List<DynamicMeasurementItem> getDynamicMeasurementItemByAcceptanceCriteriaNo(@Param("acceptance_criteria_no") String acceptance_criteria_no);

    //根据检验标准编号得到检验项目及工序信息
    public List<HashMap<String,Object>> getDynamicMeasurementItemWithProcessInfoByAcceptanceCriteriaNo(@Param("acceptance_criteria_no") String acceptance_criteria_no, @Param("skip") int skip, @Param("take") int take);

    //根据检验标准编号得到检验项目及工序信息
    public int getCountDynamicMeasurementItemWithProcessInfoByAcceptanceCriteriaNo(@Param("acceptance_criteria_no") String acceptance_criteria_no);

    //根据管号编号工序编号得到检验项目
    public List<DynamicMeasurementItem> getDynamicItemByPipeNoProcessCode(@Param("pipe_no") String pipe_no ,@Param("process_code") String process_code);

    //根据管号编号工序编号,表单编号得到检验项目及值
    public List<HashMap<String,Object>> getDynamicItemByPipeNoProcessCodeHeaderCode(@Param("pipe_no") String pipe_no , @Param("process_code") String process_code,@Param("inspection_process_record_header_code") String inspection_process_record_header_code);

    //根据管号编号工序编号得到检验项目
    public List<HashMap<String,Object>> getDynamicItemByPipeNoProcessCodeReturnMap(@Param("pipe_no") String pipe_no ,@Param("process_code") String process_code);

    //获取特殊检验项目
    public List<DynamicMeasurementItem> getSpecialDynamicItem();

   //获取内外喷标内容模板
    public List<HashMap<String,Object>> getOdIdStencilContentModel(@Param("contract_no") String contract_no);
}
