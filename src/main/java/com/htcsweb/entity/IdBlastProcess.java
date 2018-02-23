package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class IdBlastProcess {
    private  int id;
    private  String  pipe_no;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  Date operation_time;
    private  String operator_no;
    private  String surface_condition;
    private  float salt_contamination_before_blasting;
    private  float alkaline_dwell_time;
    private  float alkaline_concentration;
    private  float conductivity;
    private  float acid_wash_time;
    private  float acid_concentration;
    private float blast_line_speed;
    private float preheat_temp;
    private String upload_files;
    private  String remark;
    private  String result;

    public IdBlastProcess() {
    }



}
