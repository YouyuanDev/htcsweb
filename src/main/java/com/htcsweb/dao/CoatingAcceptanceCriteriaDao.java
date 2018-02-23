package com.htcsweb.dao;


import org.apache.ibatis.annotations.Param;
import com.htcsweb.entity.CoatingAcceptanceCriteria;
import java.util.List;


public interface CoatingAcceptanceCriteriaDao {

    public List<CoatingAcceptanceCriteria> getAllAcceptanceCriteria();
}
