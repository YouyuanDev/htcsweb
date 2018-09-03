package com.htcsweb.dao;

import com.htcsweb.entity.ShipmentInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface ShipmentInfoDao {

    //分页模糊查询发运记录
    public List<HashMap<String,Object>>getAllByLike(@Param("project_no")String project_no, @Param("shipment_no")String shipment_no, @Param("pipe_no")String pipe_no, @Param("vehicle_plate_no")String vehicle_plate_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time,@Param("skip") int skip, @Param("take") int take);

    //根据项目编号查询发运记录
    public List<HashMap<String,Object>>getShipmentByProjectNo(@Param("project_no")String project_no,  @Param("begin_time") Date begin_time, @Param("end_time") Date end_time);

    //模糊查询发运记录总数
    public int getCountAllByLike(@Param("project_no")String project_no, @Param("shipment_no")String shipment_no, @Param("pipe_no")String pipe_no, @Param("vehicle_plate_no")String vehicle_plate_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time);

    //修改发运记录
    public int updateShipmentInfo(ShipmentInfo shipment);

    //增加发运记录
    public int addShipmentInfo(ShipmentInfo shipment);

    //删除发运记录
    public int delShipmentInfo(String[]arrId);

    //根据发运编号得到发运记录
    public List<HashMap<String,Object>> getShipmentInfoByShipmentNo(@Param("shipment_no")String shipment_no);

    //根据项目编号得到发运记录
    public List<HashMap<String,Object>> getAllShipmentInfoByProjectNo(@Param("project_no")String project_no,@Param("skip") int skip, @Param("take") int take );
}
