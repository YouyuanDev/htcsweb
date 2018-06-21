package com.htcsweb.dao;


import com.htcsweb.entity.RawMaterialTestingLiquidEpoxy;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface RawMaterialTestingLiquidEpoxyDao {
    public int addRawMaterialTestEpoxy(RawMaterialTestingLiquidEpoxy rawMaterialTestingLiquidEpoxy);
    public int delRawMaterialTestEpoxy(String[] arrId);
    public List<RawMaterialTestingLiquidEpoxy> getLabTest2Fbe();
    public int updateRawMaterialTestEpoxy(RawMaterialTestingLiquidEpoxy rawMaterialTestingLiquidEpoxy);
    public List<HashMap<String,Object>> getNewAllByLike(@Param("project_no") String project_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("skip") int skip, @Param("take") int take);
    public int getCountNewAllByLike(@Param("project_no") String project_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time);

    public RawMaterialTestingLiquidEpoxy getRecentRecordBySampleNo(@Param("sample_no")String sample_no);
}
