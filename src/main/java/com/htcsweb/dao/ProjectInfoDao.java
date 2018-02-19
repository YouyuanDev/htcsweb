package com.htcsweb.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.htcsweb.entity.ProjectInfo;

public interface ProjectInfoDao {


    public List<HashMap<String,Object>> getAllByLike(@Param("project_no") String project_no, @Param("project_name")String project_name,@Param("client_name")String client_name, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time, @Param("skip")int skip, @Param("take")int take);
    public int getCount();
    public int updateProjectInfo(ProjectInfo projectInfo);
    public int addProjectInfo(ProjectInfo projectInfo);
    public int delProjectInfo(String[]arrId);
    public int hasProjectNo(@Param("project_no") String project_no);
    public List<ProjectInfo> getProjectInfo(@Param("project_no")String project_no);
    public List<ProjectInfo> getProjectInfoByID(@Param("id")String id);

}
