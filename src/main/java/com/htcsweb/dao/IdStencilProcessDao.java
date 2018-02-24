package com.htcsweb.dao;

import com.htcsweb.entity.IdStencilProcess;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface IdStencilProcessDao {
    public IdStencilProcess getIdStencilProcessById(int id);
    public int addIdStencilProcess(IdStencilProcess idStencilProcess);
    public int delIdStencilProcess(String[] arrId);
    public int updateIdStencilProcess(IdStencilProcess idStencilProcess);
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("skip") int skip, @Param("take") int take);
    public int getCount();
}
