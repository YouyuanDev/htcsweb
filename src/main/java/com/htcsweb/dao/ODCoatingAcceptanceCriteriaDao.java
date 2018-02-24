package com.htcsweb.dao;


import org.apache.ibatis.annotations.Param;
import com.htcsweb.entity.ODCoatingAcceptanceCriteria;
import java.util.List;


public interface ODCoatingAcceptanceCriteriaDao {

    public List<ODCoatingAcceptanceCriteria> getAllODAcceptanceCriteria();
}
