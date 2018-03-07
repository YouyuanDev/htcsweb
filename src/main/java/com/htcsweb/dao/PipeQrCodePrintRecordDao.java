package com.htcsweb.dao;

import com.htcsweb.entity.PipeQrCodePrintRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface PipeQrCodePrintRecordDao {

    public List<PipeQrCodePrintRecord> getQrCodeInfoByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("skip") int skip, @Param("take") int take);
    public int getCountQrCodeInfoByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no,@Param("begin_time") Date begin_time, @Param("end_time") Date end_time);
    public int delQrCode(String[] arrId);
    public int addQrCode(@Param("list") List<PipeQrCodePrintRecord> list);
}
