package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class IdCoatingInspectionProcess {
    private int id;
    private String pipe_no;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date operation_time;
    private String operator_no;
//    private float dry_film_thickness_max;
//    private float dry_film_thickness_min;
//    private float holiday_tester_volts;
//    private int holidays;
//    private String surface_condition;
//    private String bevel_check;
//    private float magnetism;
//    private int internal_repairs;
    private String upload_files;
    private String remark;
    private String result;
    private String mill_no;
    private String is_sample;
    private String wet_film_thickness_list;
    private String is_glass_sample;

    public IdCoatingInspectionProcess() {
    }

    public IdCoatingInspectionProcess(int id, String pipe_no, Date operation_time, String operator_no, String upload_files, String remark, String result, String mill_no, String is_sample, String wet_film_thickness_list, String is_glass_sample) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operation_time = operation_time;
        this.operator_no = operator_no;
        this.upload_files = upload_files;
        this.remark = remark;
        this.result = result;
        this.mill_no = mill_no;
        this.is_sample = is_sample;
        this.wet_film_thickness_list = wet_film_thickness_list;
        this.is_glass_sample = is_glass_sample;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPipe_no() {
        return pipe_no;
    }

    public void setPipe_no(String pipe_no) {
        this.pipe_no = pipe_no;
    }

    public Date getOperation_time() {
        return operation_time;
    }

    public void setOperation_time(Date operation_time) {
        this.operation_time = operation_time;
    }

    public String getOperator_no() {
        return operator_no;
    }

    public void setOperator_no(String operator_no) {
        this.operator_no = operator_no;
    }

    public String getUpload_files() {
        return upload_files;
    }

    public void setUpload_files(String upload_files) {
        this.upload_files = upload_files;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMill_no() {
        return mill_no;
    }

    public void setMill_no(String mill_no) {
        this.mill_no = mill_no;
    }

    public String getIs_sample() {
        return is_sample;
    }

    public void setIs_sample(String is_sample) {
        this.is_sample = is_sample;
    }

    public String getWet_film_thickness_list() {
        return wet_film_thickness_list;
    }

    public void setWet_film_thickness_list(String wet_film_thickness_list) {
        this.wet_film_thickness_list = wet_film_thickness_list;
    }

    public String getIs_glass_sample() {
        return is_glass_sample;
    }

    public void setIs_glass_sample(String is_glass_sample) {
        this.is_glass_sample = is_glass_sample;
    }
}
