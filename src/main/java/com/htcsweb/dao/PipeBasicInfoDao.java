package com.htcsweb.dao;

import com.htcsweb.entity.PipeBasicInfo;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface PipeBasicInfoDao {
    public List<PipeBasicInfo> getPipeNumber(@Param("pipe_no")String pipe_no);
    public List<PipeBasicInfo>getPipeNumbers(@Param("pipe_no")String pipe_no,@Param("pipestatus")String[] pipestatus);

    public List<PipeBasicInfo> getPipeNumberBySample(@Param("pipe_no")String pipe_no);
    public List<HashMap<String,Object>> getAllByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no, @Param("status")String status, @Param("skip")int skip, @Param("take")int take);
    //public int getCount();
    public int getCountAllByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no,@Param("status")String status);

    public List<HashMap<String,Object>> getODIDBarePipeInfoByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no, @Param("status")String status,@Param("skip")int skip, @Param("take")int take);

    public int getCountODIDBarePipeInfoByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no, @Param("status")String status);

    public List<HashMap<String,Object>>getPipeInfoByNo(@Param("pipe_no")String pipe_no);
    public int updatePipeBasicInfo(PipeBasicInfo pipeBasicInfo);
    public int addPipeBasicInfo(PipeBasicInfo pipeBasicInfo);
    public int delPipeBasicInfo(String[]arrId);
    public List<HashMap<String,Object>> getODInspectedPipeInfoByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no, @Param("skip")int skip, @Param("take")int take);
    public List<HashMap<String,Object>> getIDInspectedPipeInfoByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no, @Param("skip")int skip, @Param("take")int take);

    public int getCountODInspectedPipeInfoByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no);
    public int getCountIDInspectedPipeInfoByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no);

    public int odProductStockin(String[]arrId);
    public int idProductStockin(String[]arrId);

    public int IDBarePipeTOODBare(String[]arrId);
    public int ODBarePipeTOIDBare(String[]arrId);



    public List<HashMap<String,Object>> getCoatedStockinPipeInfoByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no,@Param("status")String status, @Param("skip")int skip, @Param("take")int take);
    public int getCountCoatedStockinPipeInfoByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no,@Param("status")String status);

    public int coatingProductStockout(String[]arrId);



}
