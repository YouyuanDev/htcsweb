package com.htcsweb.dao;

import com.htcsweb.entity.ShipmentInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface ShipmentInfoDao {
    //模糊搜索带分页
    public List<HashMap<String,Object>>getAllByLike(@Param("project_no")String project_no, @Param("shipment_no")String shipment_no, @Param("pipe_no")String pipe_no, @Param("vehicle_plate_no")String vehicle_plate_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time,@Param("skip") int skip, @Param("take") int take);


    public List<HashMap<String,Object>>getShipmentByProjectNo(@Param("project_no")String project_no,  @Param("begin_time") Date begin_time, @Param("end_time") Date end_time);

    //模糊搜索总数
    public int getCountAllByLike(@Param("project_no")String project_no, @Param("shipment_no")String shipment_no, @Param("pipe_no")String pipe_no, @Param("vehicle_plate_no")String vehicle_plate_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time);

    //修改ShipmentInfo
    public int updateShipmentInfo(ShipmentInfo shipment);
    //增加ShipmentInfo
    public int addShipmentInfo(ShipmentInfo shipment);
    //删除ShipmentInfo
    public int delShipmentInfo(String[]arrId);

    //根据Shipment No 得到ShipmentInfo
    public List<ShipmentInfo> getShipmentInfoByShipmentNo(@Param("shipment_no")String shipment_no);


    public List<HashMap<String,Object>> getAllShipmentInfoByProjectNo(@Param("project_no")String project_no,@Param("skip") int skip, @Param("take") int take );


}
