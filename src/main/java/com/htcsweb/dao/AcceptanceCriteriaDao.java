package com.htcsweb.dao;

import com.htcsweb.entity.AcceptanceCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface AcceptanceCriteriaDao {

    public int addAcceptanceCriteria(AcceptanceCriteria acceptanceCriteria);
    public int updateAcceptanceCriteria(AcceptanceCriteria acceptanceCriteria);
    public int delAcceptanceCriteria(String[]arrId);

     //得到所有检验标准
     public List<AcceptanceCriteria> getAllAcceptanceCriteria();
    //根据检验标准编号得到检验标准
    public List<AcceptanceCriteria> getAcceptanceCriteriaByAcceptanceCriteriaNo(@Param("acceptance_criteria_no") String acceptance_criteria_no);

    //分页查询
    public List<HashMap<String,Object>> getAllByLike(@Param("acceptance_criteria_no") String acceptance_criteria_no, @Param("external_coating_type") String external_coating_type, @Param("internal_coating_type") String internal_coating_type,@Param("skip") int skip, @Param("take") int take);

    public int getCountAllByLike (@Param("acceptance_criteria_no") String acceptance_criteria_no, @Param("external_coating_type") String external_coating_type, @Param("internal_coating_type") String internal_coating_type);



}
