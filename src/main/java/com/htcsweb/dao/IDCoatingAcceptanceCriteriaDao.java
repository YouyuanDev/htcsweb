package com.htcsweb.dao;

import org.apache.ibatis.annotations.Param;
import com.htcsweb.entity.IDCoatingAcceptanceCriteria;
import java.util.List;



public interface IDCoatingAcceptanceCriteriaDao {
    public List<IDCoatingAcceptanceCriteria> getAllIDAcceptanceCriteria();
}



