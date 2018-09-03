package com.htcsweb.dao;


import com.htcsweb.entity.DefectInfo;

import java.util.List;

public interface DefectInfoDao {

    //获取钢体缺陷类型的列表
    public List<DefectInfo> getAllSteelDefectInfo();

    //获取涂层缺陷类型的列表
    public List<DefectInfo> getAllCoatingDefectInfo();
}
