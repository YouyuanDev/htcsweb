package com.htcsweb.dao;

import com.htcsweb.entity.CoatingRepair;
import org.apache.ibatis.annotations.Param;


import java.util.Date;
import java.util.HashMap;
import java.util.List;


public interface CoatingRepairDao {


    public List<HashMap<String,Object>> getCoatingRepairInfoByLike(@Param("pipe_no")String pipe_no, @Param("operator_no")String operator_no,@Param("project_no") String project_no,  @Param("contract_no")String contract_no, @Param("status")String status, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("skip")int skip, @Param("take")int take);
    public int getCountCoatingRepairInfoByLike(@Param("pipe_no")String pipe_no, @Param("operator_no")String operator_no,@Param("project_no") String project_no,  @Param("contract_no")String contract_no, @Param("status")String status, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time);

    public int addCoatingRepair(CoatingRepair coatingRepair);
    public int delCoatingRepair(String[]arrId);
    public int updateCoatingRepair(CoatingRepair coatingRepair);

}
