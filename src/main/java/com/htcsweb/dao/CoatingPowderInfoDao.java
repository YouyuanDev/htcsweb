package com.htcsweb.dao;

import com.htcsweb.entity.CoatingPowderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CoatingPowderInfoDao {
    //添加涂层材料信息
    public int addCoatingPowder(CoatingPowderInfo coatingPowderInfo);

    //删除涂层材料信息
    public int delCoatingPowder(String[]arrId);

    //修改涂层材料信息
    public int updateCoatingPowder(CoatingPowderInfo coatingPowderInfo);

    //查询涂层材料信息总数
    public int getCountAllByLike(@Param("coating_powder_name") String coating_powder_name,@Param("powder_type") String powder_type);

    //分页查询涂层材料信息
    public List<CoatingPowderInfo> getAllByLike(@Param("coating_powder_name") String coating_powder_name,@Param("powder_type") String powder_type, @Param("skip") int skip, @Param("take") int take);;

    //获取某类型的所有粉末信息
    public List<CoatingPowderInfo> getAllCoatingPowderInfoByType(@Param("powder_type") String powder_type);

    //获取所有原材料类型
    public List<CoatingPowderInfo>  getAllCoatingPowderType();

}
