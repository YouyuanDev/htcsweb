package com.htcsweb.dao;

import com.htcsweb.entity.InspectionFrequency;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface InspectionFrequencyDao {



    public List<InspectionFrequency> getAllInspectionFrequency();
    public int addInspectionFrequency(InspectionFrequency inspectionFrequency);
    public int delInspectionFrequency(String[] arrId);
    public int updateInspectionFrequency(InspectionFrequency inspectionFrequency);
    public List<HashMap<String,Object>> getAllByLike(@Param("inspection_frequency_no") String inspection_frequency_no, @Param("skip") int skip, @Param("take") int take);
    public int getCount(@Param("inspection_frequency_no") String inspection_frequency_no);
    public InspectionFrequency getInspectionFrequencyByContractNo(@Param("contract_no")String contract_no);

    public List<HashMap<String,Object>> getFrequencyInfoByProjectNo(@Param("project_no") String project_no);

    public List<HashMap<String,Object>> getFrequencyInfoByPipeNo(@Param("pipe_no") String pipe_no);



}
