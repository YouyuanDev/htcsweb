package com.htcsweb.entity;

import java.util.Date;

public class IdCoatingInspectionProcess {
    private int id;
    private String pipe_no;
    private Date operation_time;
    private String operator_no;
    private float dry_film_thickness_max;
    private float dry_film_thickness_min;
    private float cutback;
    private float holiday_tester_volts;
    private int holiday_test_results;
    private String surface_condition;
    private String bevel_check;
    private float magnetism;
    private int internal_repairs;
    private String upload_files;
    private String remark;
    private String result;
    private String mill_no;

    public IdCoatingInspectionProcess() {
    }

    public IdCoatingInspectionProcess(int id, String pipe_no, Date operation_time, String operator_no, float dry_film_thickness_max, float dry_film_thickness_min, float cutback, float holiday_tester_volts, int holiday_test_results, String surface_condition, String bevel_check, float magnetism, int internal_repairs, String upload_files, String remark, String result, String mill_no) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operation_time = operation_time;
        this.operator_no = operator_no;
        this.dry_film_thickness_max = dry_film_thickness_max;
        this.dry_film_thickness_min = dry_film_thickness_min;
        this.cutback = cutback;
        this.holiday_tester_volts = holiday_tester_volts;
        this.holiday_test_results = holiday_test_results;
        this.surface_condition = surface_condition;
        this.bevel_check = bevel_check;
        this.magnetism = magnetism;
        this.internal_repairs = internal_repairs;
        this.upload_files = upload_files;
        this.remark = remark;
        this.result = result;
        this.mill_no = mill_no;
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

    public float getDry_film_thickness_max() {
        return dry_film_thickness_max;
    }

    public void setDry_film_thickness_max(float dry_film_thickness_max) {
        this.dry_film_thickness_max = dry_film_thickness_max;
    }

    public float getDry_film_thickness_min() {
        return dry_film_thickness_min;
    }

    public void setDry_film_thickness_min(float dry_film_thickness_min) {
        this.dry_film_thickness_min = dry_film_thickness_min;
    }

    public float getCutback() {
        return cutback;
    }

    public void setCutback(float cutback) {
        this.cutback = cutback;
    }

    public float getHoliday_tester_volts() {
        return holiday_tester_volts;
    }

    public void setHoliday_tester_volts(float holiday_tester_volts) {
        this.holiday_tester_volts = holiday_tester_volts;
    }

    public int getHoliday_test_results() {
        return holiday_test_results;
    }

    public void setHoliday_test_results(int holiday_test_results) {
        this.holiday_test_results = holiday_test_results;
    }

    public String getSurface_condition() {
        return surface_condition;
    }

    public void setSurface_condition(String surface_condition) {
        this.surface_condition = surface_condition;
    }

    public String getBevel_check() {
        return bevel_check;
    }

    public void setBevel_check(String bevel_check) {
        this.bevel_check = bevel_check;
    }

    public float getMagnetism() {
        return magnetism;
    }

    public void setMagnetism(float magnetism) {
        this.magnetism = magnetism;
    }

    public int getInternal_repairs() {
        return internal_repairs;
    }

    public void setInternal_repairs(int internal_repairs) {
        this.internal_repairs = internal_repairs;
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
}
