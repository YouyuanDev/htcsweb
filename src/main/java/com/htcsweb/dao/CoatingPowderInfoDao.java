package com.htcsweb.dao;

import com.htcsweb.entity.CoatingPowderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CoatingPowderInfoDao {

    public int addCoatingPowder(CoatingPowderInfo coatingPowderInfo);
    public int delCoatingPowder(String[]arrId);
    public int updateCoatingPowder(CoatingPowderInfo coatingPowderInfo);
    //获取
    public int getCountAllByLike(@Param("coating_powder_name") String coating_powder_name);
    //搜索粉末型号信息
    public List<CoatingPowderInfo> getAllByLike(@Param("coating_powder_name") String coating_powder_name, @Param("skip") int skip, @Param("take") int take);;

    //获取所有粉末信息
    public List<CoatingPowderInfo> getAllCoatingPowderInfo();



}
