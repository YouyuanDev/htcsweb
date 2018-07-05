package com.htcsweb.dao;

import com.htcsweb.entity.InspectionProcessRecordHeader;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface InspectionProcessRecordHeaderDao {

    public int addInspectionProcessRecordHeader(InspectionProcessRecordHeader inspectionProcessRecordHeader);
    public int updateInspectionProcessRecordHeader(InspectionProcessRecordHeader inspectionProcessRecordHeader);
    public int delInspectionProcessRecordHeader(String[]arrId);

    public List<HashMap<String,Object>> getAllByLike(@Param("process_code") String process_code,@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no, @Param("skip") int skip, @Param("take") int take);

    public int getCountAllByLike(@Param("process_code") String process_code,@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no);

    public List<HashMap<String,Object>>  getProcessRecord(@Param("process_code") String process_code,@Param("project_no") String project_no,@Param("mill_no") String mill_no,@Param("od") String od,@Param("wt") String wt, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time);

    //根据钢管编号查询上一根合格记录
    public InspectionProcessRecordHeader getLastAcceptedRecordBeforePipeNo(@Param("process_code") String process_code,@Param("pipe_no")String pipe_no,@Param("mill_no")String mill_no);

    //根据钢管编号查询最新一条记录
    public InspectionProcessRecordHeader getRecentRecordByPipeNo(@Param("process_code") String process_code,@Param("pipe_no")String pipe_no);

    //根据涂敷时间得到对应的打砂检验时间
    public List<HashMap<String,Object>>getBlastInfoByCoatingInfo(@Param("pipe_no") String pipe_no,@Param("id") int id,@Param("process_code")String process_code);


    //-----------日报
    //获取当天外防腐总数
    public int getODCoatingCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);
    //获取当天外防腐合格总数与合格长度
    public List<HashMap<String,Object>> getODCoatingAcceptedInfo(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("result")String result);
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
    public List<HashMap<String,Object>> getIDCoatingAcceptedInfo(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time,@Param("result")String result);

    public List<HashMap<String,Object>> getPipeSamplingInfo(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取样管切割个数
    public int getSampleCutoffCount(@Param("project_no")String project_no,@Param("mill_no")String mill_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("odid")String odid,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //获取倒棱合格数量
    public int getAcceptedRebevelCount(@Param("project_no")String project_no,@Param("external_coating")String external_coating,@Param("internal_coating")String internal_coating,@Param("od")float od,@Param("wt")float wt,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);


}
