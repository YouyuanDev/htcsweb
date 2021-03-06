package com.htcsweb.dao;

import com.htcsweb.entity.InspectionProcessRecordHeader;
import com.htcsweb.entity.PipeBasicInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface InspectionProcessRecordHeaderDao {

    //添加检验表单头部信息
    public int addInspectionProcessRecordHeader(InspectionProcessRecordHeader inspectionProcessRecordHeader);

    //更新检验表单头部信息
    public int updateInspectionProcessRecordHeader(InspectionProcessRecordHeader inspectionProcessRecordHeader);

    //删除检验表单头部信息
    public int delInspectionProcessRecordHeader(String[]arrId);

    //分页查询检验表单头部信息
    public List<HashMap<String,Object>> getAllByLike(@Param("process_code") String process_code,@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no, @Param("skip") int skip, @Param("take") int take);

    //查询检验表单头部信息总数
    public int getCountAllByLike(@Param("process_code") String process_code,@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no);

    //public List<HashMap<String,Object>>  getProcessRecord(@Param("process_code") String process_code,@Param("project_no") String project_no,@Param("mill_no") String mill_no,@Param("od") String od,@Param("wt") String wt, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time);

    //根据钢管编号查询上一根合格记录
    //public InspectionProcessRecordHeader getLastAcceptedRecordBeforePipeNo(@Param("process_code") String process_code,@Param("pipe_no")String pipe_no,@Param("mill_no")String mill_no);

    //根据钢管编号查询最新一条记录
    public InspectionProcessRecordHeader getRecentRecordByPipeNo(@Param("process_code") String process_code,@Param("pipe_no")String pipe_no);

    //根据涂敷时间得到对应的打砂检验时间
    public List<HashMap<String,Object>>getBlastInfoByCoatingInfo(@Param("pipe_no") String pipe_no,@Param("id") int id,@Param("process_code")String process_code);


    //-----------日报
    //获取当天外防腐总数
    public int getODCoatingCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取当天外防腐合格总数与合格长度
    public List<HashMap<String,Object>> getODCoatingAcceptedInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("result")String result);
    //获取涂层修补数
    public int getCoatingRepairCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);


    //获取外防光管隔离数量
    public int getODBarePipeOnholdCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取内防光管隔离数量
    public int getIDBarePipeOnholdCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取光管隔离修磨数量
    public int getBarePipeGrindingCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取光管隔离切割数量
    public int getBarePipeCutoffCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取外涂层废管数量（既需要扒皮的数量）
    public int getODCoatingRejectedPipeCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取内涂层废管数量（既需要扒皮的数量）
    public int getIDCoatingRejectedPipeCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取已扒皮处理合格数量
    public int getStripPipeAcceptedCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取内防腐支数
    public int getIDCoatingCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取当天内防腐合格总数和合格长度
    public List<HashMap<String,Object>> getIDCoatingAcceptedInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("result")String result);

    public List<HashMap<String,Object>> getPipeSamplingInfo(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取样管切割个数
    public int getSampleCutoffCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取倒棱合格数量
    public int getAcceptedRebevelCount(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取发运的数量和长度
    public List<HashMap<String,Object>> getShippingCountAndLength(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //APP根据项目编号获取试验信息
    public List<HashMap<String,Object>>getLabTestingInfoByProjectNo(@Param("project_no") String project_no, @Param("skip") int skip, @Param("take") int take);


    //获取封面上的试验管信息
    public List<HashMap<String,Object>>getCoverPipeSamplingInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取DSC固化度试验管号
    public List<HashMap<String,Object>>getCover2FBEDscTestingInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取PE试验管号
    public List<HashMap<String,Object>>getCover3LPEPETestingInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取修补管信息（4.11）
    public List<HashMap<String,Object>>getCoatingRepairInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取外防涂层管废管管号和原因
    public List<HashMap<String,Object>>getODCoatingRejectedPipeInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //封面获取外防光管隔离管编号和原因
    public List<HashMap<String,Object>> getODBarePipeOnholdInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取内防钢试片样管管子编号
    public List<HashMap<String,Object>>getCoverIdSampleInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取内防玻璃片样管管子编号
    public List<HashMap<String,Object>>getCoverIdGlassSampleInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取内防涂层管废管管号和原因
    public List<HashMap<String,Object>>getIDCoatingRejectedPipeInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //封面获取内防光管隔离管编号和原因
    public List<HashMap<String,Object>> getIDBarePipeOnholdInfo(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取岗位记录信息
    public List<HashMap<String,Object>> getPostRecord(@Param("process_code")String process_code,@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating ,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取钢管的上一工位记录头部
    public InspectionProcessRecordHeader getLastInspectionRecord(@Param("pipe_no")String pipe_no);

    //根据project_no mill_no process_code获取检验记录
    public List<HashMap<String,Object>> getInspectionRecordByProjectNoMillNoProcessCode(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("process_code")String process_code,@Param("pipe_no")String pipe_no,@Param("skip") int skip, @Param("take") int take);

    //根据project_no mill_no process_code获取检验记录总数
    public int getCountInspectionRecordByProjectNoMillNoProcessCode(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("process_code")String process_code,@Param("pipe_no")String pipe_no);

    //获取某工位后的10根管子 且为待定状态
    public List<HashMap<String,Object>> getNextTenPipesBeforePipeNo(@Param("pipe_no")String pipe_no,@Param("mill_no")String mill_no,@Param("process_code")String process_code);

}
