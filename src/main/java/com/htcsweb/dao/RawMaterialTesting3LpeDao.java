package com.htcsweb.dao;


import com.htcsweb.entity.RawMaterialTesting3Lpe;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface RawMaterialTesting3LpeDao {
    public int addRawMaterialTest3Lpe(RawMaterialTesting3Lpe rawMaterialTesting3Lpe);
    public int delRawMaterialTest3Lpe(String[] arrId);
    public List<RawMaterialTesting3Lpe> getRawMaterialTest2Fbe();
    public int updateRawMaterialTest3Lpe(RawMaterialTesting3Lpe rawMaterialTesting3Lpe);
    public List<HashMap<String,Object>> getNewAllByLike(@Param("project_no") String project_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("skip") int skip, @Param("take") int take);
    public int getCountNewAllByLike(@Param("project_no") String project_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time);
}
