package com.htcsweb.dao;


import com.htcsweb.entity.RawMaterialTesting2Fbe;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface RawMaterialTesting2FbeDao {
    public int addRawMaterialTest2Fbe(RawMaterialTesting2Fbe rawMaterialTesting2Fbe);
    public int delRawMaterialTest2Fbe(String[] arrId);
    public List<RawMaterialTesting2Fbe> getRawMaterialTest2Fbe();
    public int updateRawMaterialTest2Fbe(RawMaterialTesting2Fbe rawMaterialTesting2Fbe);
    public List<HashMap<String,Object>> getNewAllByLike(@Param("project_no") String project_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("skip") int skip, @Param("take") int take);
    public int getCountNewAllByLike(@Param("project_no") String project_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time);
    public RawMaterialTesting2Fbe getRecentRecordBySampleNo(@Param("sample_no")String sample_no);
}
