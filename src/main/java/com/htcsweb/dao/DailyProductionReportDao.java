package com.htcsweb.dao;

import com.htcsweb.entity.DailyProductionReport;
import com.htcsweb.entity.Function;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface DailyProductionReportDao {

    //分页模糊查询日报信息
    public List<HashMap<String,Object>> getAllByLike(@Param("project_no") String project_no,@Param("begin_time") Date begin_time, @Param("end_time") Date end_time, @Param("skip") int skip, @Param("take") int take);

    //模糊查询日报信息总数
    public int getCountAllByLike(@Param("project_no") String project_no,@Param("begin_time") Date begin_time, @Param("end_time") Date end_time);

    //修改日报信息
    public int updateDailyProductionReport(DailyProductionReport dailyProductionReport);

    //增加日报信息
    public int addDailyProductionReport(DailyProductionReport dailyProductionReport);

    //删除日报信息
    public int delDailyProductionReport(String[]arrId);

    //判断是否存在日报数据
    public List<DailyProductionReport>getDailyReportByParams(@Param("project_no") String project_no,@Param("od_coating_type")String od_coating_type,@Param("id_coating_type")String id_coating_type,@Param("od_wt")String od_wt,@Param("production_date") Date production_date);
}
