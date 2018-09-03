package com.htcsweb.dao;

import com.htcsweb.entity.PipeQrCodePrintRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface PipeQrCodePrintRecordDao {

    //分页模糊查询二维码打印记录
    public List<PipeQrCodePrintRecord> getQrCodeInfoByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("skip") int skip, @Param("take") int take);

    //模糊查询二维码打印记录总数
    public int getCountQrCodeInfoByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no,@Param("begin_time") Date begin_time, @Param("end_time") Date end_time);

    //删除二维码打印记录
    public int delQrCode(String[] arrId);

    //添加二维码打印记录
    public int addQrCode(@Param("list") List<PipeQrCodePrintRecord> list);

    //获取钢管qr码打印次数
    public int getQRCodePrintCountByPipeNo(@Param("pipe_no") String pipe_no);
}
