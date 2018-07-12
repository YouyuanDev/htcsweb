package com.htcsweb.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.htcsweb.entity.ProjectInfo;

public interface ProjectInfoDao {


    public List<HashMap<String,Object>> getAllByLike(@Param("project_no") String project_no, @Param("project_name")String project_name,@Param("client_name")String client_name, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time, @Param("skip")int skip, @Param("take")int take);
    //public int getCount();
    public int getCountAllByLike(@Param("project_no") String project_no, @Param("project_name")String project_name,@Param("client_name")String client_name, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    public int updateProjectInfo(ProjectInfo projectInfo);
    public int addProjectInfo(ProjectInfo projectInfo);
    public int delProjectInfo(String[]arrId);
    public int hasProjectNo(@Param("project_no") String project_no);
    public List<ProjectInfo> getProjectInfo(@Param("project_no")String project_no);
    public List<ProjectInfo> getProjectInfoByID(@Param("id")String id);
    public String getProjectNameByNo(@Param("project_no")String project_no);
    public List<ProjectInfo> getProjectInfoByContract(@Param("contract_no")String contract_no);
    public List<HashMap<String,Object>>getProjectInfoByNoOrName(@Param("project_no")String project_no,@Param("project_name")String project_name);
    //获取项目编号和名字
    public  List<HashMap<String,Object>>getProjectNoAndName();
    //根据项目编号获取项目信息
    public List<ProjectInfo>getProjectInfoByProjectNo(@Param("project_no")String project_no);

    //获取产品质量保证书的基础信息
    public List<HashMap<String,Object>>getMTCBasicInfo(@Param("project_no")String project_no);
    //获取产品质量保证书的涂敷时间信息
    public List<HashMap<String,Object>>getMTCCoatinDurationInfo(@Param("project_no")String project_no);
    //获取产品质量保证书的在线检测信息
    public List<HashMap<String,Object>>getMTCOnlineInspectionInfo(@Param("project_no")String project_no);
    //获取产品质量保证书的实验室项目信息
    public List<HashMap<String,Object>>getMTCLabInfo(@Param("project_no")String project_no);
}
