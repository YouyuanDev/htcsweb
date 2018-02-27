package com.htcsweb.dao;

import com.htcsweb.entity.MillInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Repository
public interface MillInfoDao {

    public List<MillInfo> getAllMillInfo();
}
