package com.htcsweb.dao;

import java.util.List;
import com.htcsweb.entity.PipeStatus;

public interface PipeStatusDao {

    //获取所有钢管状态信息
    public List<PipeStatus> getAllPipeStatus();
}
