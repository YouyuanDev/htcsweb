package com.htcsweb.dao;

import com.htcsweb.entity.ProcessInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProcessInfoDao {

    //获取所有工序信息
    public List<ProcessInfo> getAllProcessInfo();

    //public ProcessInfo getProcessInfoByProcessCode(@Param("process_code") String process_code);
}
