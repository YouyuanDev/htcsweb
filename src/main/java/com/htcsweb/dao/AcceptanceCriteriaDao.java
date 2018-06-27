package com.htcsweb.dao;

import com.htcsweb.entity.AcceptanceCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AcceptanceCriteriaDao {

    public int addAcceptanceCriteria(AcceptanceCriteria acceptanceCriteria);
    public int updateAcceptanceCriteria(AcceptanceCriteria acceptanceCriteria);
    public int delAcceptanceCriteria(String[]arrId);

     //得到所有检验标准
     public List<AcceptanceCriteria> getAllAcceptanceCriteria();
    //根据检验标准编号得到检验标准
    public List<AcceptanceCriteria> getAcceptanceCriteriaByAcceptanceCriteriaNo(@Param("acceptance_criteria_no") String acceptance_criteria_no);

}
