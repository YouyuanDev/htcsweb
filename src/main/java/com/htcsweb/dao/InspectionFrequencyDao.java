package com.htcsweb.dao;

import com.htcsweb.entity.InspectionFrequency;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface InspectionFrequencyDao {

    //查找所有检验频率,下拉框使用
    public List<InspectionFrequency> getAllInspectionFrequency();

    //增加检验频率
    public int addInspectionFrequency(InspectionFrequency inspectionFrequency);

    //删除检验频率
    public int delInspectionFrequency(String[] arrId);

    //更新检验频率
    public int updateInspectionFrequency(InspectionFrequency inspectionFrequency);

    //分页查询检验频率
    public List<HashMap<String,Object>> getAllByLike(@Param("inspection_frequency_no") String inspection_frequency_no, @Param("skip") int skip, @Param("take") int take);

    //检验频率总数
    public int getCount(@Param("inspection_frequency_no") String inspection_frequency_no);

    //根据项目编号查询检验频率
    public List<HashMap<String,Object>> getFrequencyInfoByProjectNo(@Param("project_no") String project_no);

    //根据钢管编号查询检验频率
    public List<HashMap<String,Object>> getFrequencyInfoByPipeNo(@Param("pipe_no") String pipe_no);
}
