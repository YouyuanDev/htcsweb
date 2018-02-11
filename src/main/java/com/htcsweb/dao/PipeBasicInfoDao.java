package com.htcsweb.dao;

import com.htcsweb.entity.PipeBasicInfo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface PipeBasicInfoDao {
    public List<PipeBasicInfo> getPipeNumber(@Param("pipe_no")String pipe_no);
}
