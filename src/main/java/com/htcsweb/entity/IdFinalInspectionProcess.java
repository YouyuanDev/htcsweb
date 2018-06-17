package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class IdFinalInspectionProcess {
    private int id;
    private String pipe_no;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date operation_time;
    private String operator_no;
    private String od_inspection_result;
    private String final_inspection_result;
    private String upload_files;
    private String remark;
    private String result;
    private String mill_no;
    private String dry_film_thickness_list;
    private String cutback_length;
    private String stencil_verification;
    private String roughness_list;

    private float holiday_tester_volts;
    private int holidays;
    private String surface_condition;
    private String bevel_check;
    private String magnetism_list;
    private int internal_repairs;

    public IdFinalInspectionProcess() {
    }

    public IdFinalInspectionProcess(int id, String pipe_no, Date operation_time, String operator_no, String od_inspection_result, String final_inspection_result, String upload_files, String remark, String result, String mill_no, String dry_film_thickness_list, String cutback_length, String stencil_verification, String roughness_list, float holiday_tester_volts, int holidays, String surface_condition, String bevel_check, String magnetism_list, int internal_repairs) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operation_time = operation_time;
        this.operator_no = operator_no;
        this.od_inspection_result = od_inspection_result;
        this.final_inspection_result = final_inspection_result;
        this.upload_files = upload_files;
        this.remark = remark;
        this.result = result;
        this.mill_no = mill_no;
        this.dry_film_thickness_list = dry_film_thickness_list;
        this.cutback_length = cutback_length;
        this.stencil_verification = stencil_verification;
        this.roughness_list = roughness_list;
        this.holiday_tester_volts = holiday_tester_volts;
        this.holidays = holidays;
        this.surface_condition = surface_condition;
        this.bevel_check = bevel_check;
        this.magnetism_list = magnetism_list;
        this.internal_repairs = internal_repairs;
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

    public String getOd_inspection_result() {
        return od_inspection_result;
    }

    public void setOd_inspection_result(String od_inspection_result) {
        this.od_inspection_result = od_inspection_result;
    }

    public String getFinal_inspection_result() {
        return final_inspection_result;
    }

    public void setFinal_inspection_result(String final_inspection_result) {
        this.final_inspection_result = final_inspection_result;
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

    public String getDry_film_thickness_list() {
        return dry_film_thickness_list;
    }

    public void setDry_film_thickness_list(String dry_film_thickness_list) {
        this.dry_film_thickness_list = dry_film_thickness_list;
    }

    public String getCutback_length() {
        return cutback_length;
    }

    public void setCutback_length(String cutback_length) {
        this.cutback_length = cutback_length;
    }

    public String getStencil_verification() {
        return stencil_verification;
    }

    public void setStencil_verification(String stencil_verification) {
        this.stencil_verification = stencil_verification;
    }

    public String getRoughness_list() {
        return roughness_list;
    }

    public void setRoughness_list(String roughness_list) {
        this.roughness_list = roughness_list;
    }

    public float getHoliday_tester_volts() {
        return holiday_tester_volts;
    }

    public void setHoliday_tester_volts(float holiday_tester_volts) {
        this.holiday_tester_volts = holiday_tester_volts;
    }

    public int getHolidays() {
        return holidays;
    }

    public void setHolidays(int holidays) {
        this.holidays = holidays;
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

    public String getMagnetism_list() {
        return magnetism_list;
    }

    public void setMagnetism_list(String magnetism_list) {
        this.magnetism_list = magnetism_list;
    }

    public int getInternal_repairs() {
        return internal_repairs;
    }

    public void setInternal_repairs(int internal_repairs) {
        this.internal_repairs = internal_repairs;
    }
}
