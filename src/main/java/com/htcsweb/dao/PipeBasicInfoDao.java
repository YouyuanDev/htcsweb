package com.htcsweb.dao;

import com.htcsweb.entity.PipeBasicInfo;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface PipeBasicInfoDao {
    public List<PipeBasicInfo> getPipeNumber(@Param("pipe_no")String pipe_no);

    public List<HashMap<String,Object>> getAllByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no, @Param("skip")int skip, @Param("take")int take);
    public int getCount();

    public int updatePipeBasicInfo(PipeBasicInfo pipeBasicInfo);
    public int addPipeBasicInfo(PipeBasicInfo pipeBasicInfo);
    public int delPipeBasicInfo(String[]arrId);




}
