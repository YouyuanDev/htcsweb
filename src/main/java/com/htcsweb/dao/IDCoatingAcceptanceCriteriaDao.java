package com.htcsweb.dao;

import org.apache.ibatis.annotations.Param;
import com.htcsweb.entity.IDCoatingAcceptanceCriteria;

import java.util.HashMap;
import java.util.List;



public interface IDCoatingAcceptanceCriteriaDao {
    public List<IDCoatingAcceptanceCriteria> getAllIDAcceptanceCriteria();
    public int addIdCoatingCriterProcess(IDCoatingAcceptanceCriteria idCoatingAcceptanceCriteria);
    public int delIdCoatingCriterProcess(String[] arrId);
    public int updateIdCoatingCriterProcess(IDCoatingAcceptanceCriteria idCoatingAcceptanceCriteria);
    public List<HashMap<String,Object>> getAllByLike(@Param("coating_acceptance_criteria_no") String coating_acceptance_criteria_no, @Param("skip") int skip, @Param("take") int take);
    public int getCount(@Param("coating_acceptance_criteria_no") String coating_acceptance_criteria_no);
    public IDCoatingAcceptanceCriteria getIDAcceptanceCriteriaByContractNo(@Param("contract_no")String contract_no);
    public IDCoatingAcceptanceCriteria getIDAcceptanceCriteriaByPipeNo(@Param("pipe_no")String pipe_no);



}



