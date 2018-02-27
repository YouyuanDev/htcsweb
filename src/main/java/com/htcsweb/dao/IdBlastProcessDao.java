package com.htcsweb.dao;



import com.htcsweb.entity.IdBlastProcess;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface IdBlastProcessDao {
    public IdBlastProcess getIdBlastProcessById(int id);
    public int addIdBlastProcess(IdBlastProcess idBlastProcess);
    public int delIdBlastProcess(String[] arrId);
    public List<IdBlastProcess> getIdBlastProcess();
    public int updateIdBlastProcess(IdBlastProcess idBlastProcess);
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time,@Param("mill_no") String mill_no, @Param("skip") int skip, @Param("take") int take);
    //public int getCount();
    public int getCountAllByLike(@Param("pipe_no") String pipe_no, @Param("operator_no") String operator_no, @Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("mill_no") String mill_no);

}
