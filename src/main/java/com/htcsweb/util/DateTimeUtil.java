package com.htcsweb.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateTimeUtil {
    private  static  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 获取两个日期之间的所有日期(字符串格式)
     * @param start
     * @param end
     * @return
     */
    public static   List<String> getBetweenDates(Date start, Date end) {
        List<String> result = new ArrayList<String>();
        result.add(sdf.format(start));
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        tempStart.add(Calendar.DAY_OF_YEAR, 1);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        while (tempStart.before(tempEnd)) {
            result.add(sdf.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        if(!result.contains(sdf.format(end)))
          result.add(sdf.format(end));
        return result;
    }
    /**
     * 获取传过来日期的下一天的日期(字符串格式)
     * @param day(传过来日期)
     * @return
     */
    public static  String getNextDay(String day){
        String returnday=null;
        try{
            Date dayTime=sdf.parse(day);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dayTime);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            returnday=sdf.format(calendar.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnday;
    }
    /**
     * 获取传过来日期的前一天的日期(字符串格式)
     * @param day
     * @return
     */
    public  static String getLastDay(String day){
        String returnday=null;
        try{
            Date dayTime=sdf.parse(day);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dayTime);
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            returnday=sdf.format(calendar.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnday;
    }
}
