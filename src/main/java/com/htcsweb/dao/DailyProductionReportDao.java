package com.htcsweb.dao;

import com.htcsweb.entity.DailyProductionReport;
import com.htcsweb.entity.Function;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface DailyProductionReportDao {

    //模糊搜索带分页
    public List<HashMap<String,Object>> getAllByLike(@Param("project_no") String project_no,@Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("skip") int skip, @Param("take") int take);

    //模糊搜索总数
    public int getCountAllByLike(@Param("project_no") String project_no,@Param("begin_time") Date begin_time, @Param("end_time") Date end_time);

    //修改function
    public int updateDailyProductionReport(DailyProductionReport dailyProductionReport);
    //增加function
    public int addDailyProductionReport(DailyProductionReport dailyProductionReport);
    //删除function
    public int delDailyProductionReport(String[]arrId);
    //判断是否存在日报数据
    public List<DailyProductionReport>getDailyReportByParams(@Param("project_no") String project_no,@Param("od_coating_type")String od_coating_type,@Param("id_coating_type")String id_coating_type,@Param("od_wt")String od_wt,@Param("production_date") Date production_date);
    //根据项目编号和时间查询日报
    //public List<DailyProductionReport>getDailyReportByProjectNoAndTime(@Param("project_no") String project_no,@Param("od_wt")String od_wt,@Param("external_coating")String external_coating,@Param("begin_time") Date begin_time, @Param("end_time") Date end_time);
}
