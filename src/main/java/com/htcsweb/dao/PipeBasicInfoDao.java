package com.htcsweb.dao;

import com.htcsweb.entity.PipeBasicInfo;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface PipeBasicInfoDao {

    //根据钢管编号搜索钢管信息(app使用)
    public  List<HashMap<String,Object>> searchPipe(@Param("pipe_no")String pipe_no,@Param("skip")int skip, @Param("take")int take);

    //根据钢管编号搜索总数(app使用)
    public int searchPipeCount(@Param("pipe_no")String pipe_no);

    //根据钢管编号获取钢管信息
    public  List<PipeBasicInfo> getPipeNumber(@Param("pipe_no")String pipe_no);

    //根据是否是试验管标识获取试验管信息
    public List<PipeBasicInfo>getPipeNumbers(@Param("pipe_no")String pipe_no,@Param("external_coatingtype")String external_coatingtype,@Param("pipestatus")String[] pipestatus,@Param("odsampling_mark")String odsampling_mark,@Param("od_dsc_sample_mark")String od_dsc_sample_mark,@Param("od_pe_sample_mark")String od_pe_sample_mark,@Param("idsampling_mark")String idsampling_mark,@Param("id_glass_sample_mark")String id_glass_sample_mark,@Param("rebevel_mark")String rebevel_mark);

    //获取外防待取样钢管信息 1000条
    public List<PipeBasicInfo> getODSamplePipeNumbers(@Param("pipe_no")String pipe_no);

    //获取内防待取样钢管信息 1000条
    public List<PipeBasicInfo> getIDSamplePipeNumbers(@Param("pipe_no")String pipe_no);

    //根据钢管编号获取需要倒棱的钢管信息
    public List<PipeBasicInfo> getNeedRebevelPipeNumbers(@Param("pipe_no")String pipe_no);

    //分页模糊查询钢管基础信息
    public List<HashMap<String,Object>> getAllByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no, @Param("status")String status, @Param("skip")int skip, @Param("take")int take);

    //模糊查询钢管基础信息总数
    public int getCountAllByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no,@Param("status")String status);

    //分页模糊查询外、内防光管基础信息
    public List<HashMap<String,Object>> getODIDBarePipeInfoByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no, @Param("status")String status,@Param("skip")int skip, @Param("take")int take);

    //模糊查询外、内防光管基础信息总数
    public int getCountODIDBarePipeInfoByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no, @Param("status")String status);

    //根据钢管编号查询钢管基础信息
    public List<HashMap<String,Object>>getPipeInfoByNo(@Param("pipe_no")String pipe_no);

    //根据钢管id查询钢管基础信息
    public List<HashMap<String,Object>>getPipeInfoById(@Param("id")int id);

    //修改钢管基础信息
    public int updatePipeBasicInfo(PipeBasicInfo pipeBasicInfo);

    //添加钢管基础信息
    public int addPipeBasicInfo(PipeBasicInfo pipeBasicInfo);

    //删除钢管基础信息
    public int delPipeBasicInfo(String[]arrId);

    //分页模糊查询外防成品、可出库的钢管信息
    public List<HashMap<String,Object>> getODInspectedPipeInfoByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no, @Param("skip")int skip, @Param("take")int take);

    //分页模糊查询内防成品、可出库的钢管信息
    public List<HashMap<String,Object>> getIDInspectedPipeInfoByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no, @Param("skip")int skip, @Param("take")int take);

    //模糊查询外防成品、可出库的钢管信息总数
    public int getCountODInspectedPipeInfoByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no);

    //模糊查询内防成品、可出库的钢管信息总数
    public int getCountIDInspectedPipeInfoByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no);

    //外防成品管入库
    public int odProductStockin(@Param("id")int id,@Param("storage_stack") String storage_stack,@Param("stack_level")String stack_level,@Param("level_direction")String level_direction,@Param("level_sequence")String level_sequence);

    //内防成品管入库
    public int idProductStockin(@Param("id")int id,@Param("storage_stack") String storage_stack,@Param("stack_level")String stack_level,@Param("level_direction")String level_direction,@Param("level_sequence")String level_sequence);

    //转外光管库
    public int SetToODBare(String[]arrId);

    //转内光管库
    public int SetToIDBare(String[]arrId);

    //分页模糊查询内防外防成品可出厂的钢管信息
    public List<HashMap<String,Object>> getCoatedStockinPipeInfoByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no,@Param("status")String status, @Param("skip")int skip, @Param("take")int take);

    //模糊查询内防外防成品可出厂的钢管信息总数
    public int getCountCoatedStockinPipeInfoByLike(@Param("project_no") String project_no,  @Param("contract_no")String contract_no,@Param("pipe_no")String pipe_no,@Param("status")String status);

    //成品管出厂
    public int coatingProductStockout(String[]arrId);

    //判断钢管是否有外防处理记录
    public int isPipeODProcessed(@Param("id")String id);

    //判断钢管是否有内防处理记录
    public int isPipeIDProcessed(@Param("id")String id);

    //根据合同号查询所有钢管编号
    public List<String>getPipeNoByContractNo(@Param("array")String[] array);

    //查询2FBE实验样管信息 不包括DSC实验
    public List<PipeBasicInfo> get2FBESamplePipeNo(@Param("pipe_no")String pipe_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("id")int id);

    //查询2FBE dsc实验管号，未做实验的
    public List<PipeBasicInfo> get2FBEDSCSamplePipeNo(@Param("pipe_no")String pipe_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("id")int id);

    //查询3LPE常规实验样管信息
    public List<PipeBasicInfo> get3LPESamplePipeNo(@Param("pipe_no")String pipe_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("id")int id);

    //查询3LPE DSC实验样管信息
    public List<PipeBasicInfo> get3LPEDSCSamplePipeNo(@Param("pipe_no")String pipe_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("id")int id);

    //查询3LPE PE实验样管信息
    public List<PipeBasicInfo> get3LPEPESamplePipeNo(@Param("pipe_no")String pipe_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("id")int id);

    //查询Liquid Epoxy内防实验样管信息  常规实验
    public List<PipeBasicInfo> getLiquidEpoxySamplePipeNo(@Param("pipe_no")String pipe_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("id")int id);

    //查询Liquid Epoxy内防实验样管信息  玻璃片实验
    public List<PipeBasicInfo> getLiquidEpoxyGlassSamplePipeNo(@Param("pipe_no")String pipe_no, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("id")int id);

    //钢管转运
    public int InStoageTransfer(@Param("array")String[]array,@Param("storage_stack")String storage_stack,@Param("stack_level")String stack_level,@Param("level_direction")String level_direction,@Param("level_sequence")String level_sequence);

    //查询2FBE未做实验的样管信息 包括DSC实验
    public List<PipeBasicInfo> getAll2FBESamplePipe(@Param("project_no")String project_no);

    //查询3LPE实验的样管信息 包括DSC实验、PE实验
    public List<PipeBasicInfo> getAll3LPESamplePipe(@Param("project_no")String project_no);

    //查询3LPE未做实验的样管信息 包括玻璃片实验、常规实验
    public List<PipeBasicInfo> getAllEpoxySamplePipe(@Param("project_no")String project_no);

    //根据管号判断该钢管是否可以出库
    public List<HashMap<String,Object>> checkPipeForShipment(@Param("pipe_no")String pipe_no);

    //
    //public int updateSamplingMark(@Param("pipe_no")String pipe_no);

    //获得该项目 的第一根管子
    public PipeBasicInfo getFirstPipeNoByProjectNo(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating);


    //获取外防涂层不合格原因记录
    public List<HashMap<String,Object>> getODCoatingRejectData(@Param("project_no")String project_no);

    //获取内防涂层不合格原因记录
    public List<HashMap<String,Object>> getIDCoatingRejectData(@Param("project_no")String project_no);

    //获取外防涂层总厚度记录
    public List<HashMap<String,Object>> getODTotalCoatingThicknessData(@Param("project_no")String project_no);

    //获取内防涂层干膜厚度记录
    public List<HashMap<String,Object>> getIDDryFilmThicknessData(@Param("project_no")String project_no);


}
