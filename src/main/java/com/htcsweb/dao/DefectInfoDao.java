package com.htcsweb.dao;


import com.htcsweb.entity.DefectInfo;

import java.util.List;

public interface DefectInfoDao {

    //得到Steel类型的DefectInfo
    public List<DefectInfo> getAllSteelDefectInfo();
    //得到Coating类型的DefectInfo
    public List<DefectInfo> getAllCoatingDefectInfo();
}
