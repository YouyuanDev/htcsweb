package com.htcsweb.dao;


import com.htcsweb.entity.ODCoatingAcceptanceCriteria;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public interface ODCoatingAcceptanceCriteriaDao {
    public int addOdCoatingCriterProcess(ODCoatingAcceptanceCriteria odCoatingAcceptanceCriteria);
    public int delOdCoatingCriterProcess(String[] arrId);
    public int updateOdCoatingCriterProcess(ODCoatingAcceptanceCriteria odCoatingAcceptanceCriteria);
    public List<HashMap<String,Object>> getAllByLike(@Param("coating_acceptance_criteria_no") String coating_acceptance_criteria_no,@Param("skip") int skip, @Param("take") int take);
    public int getCount();
}
