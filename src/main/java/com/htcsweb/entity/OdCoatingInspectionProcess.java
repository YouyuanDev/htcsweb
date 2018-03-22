package com.htcsweb.entity;

import java.util.Date;

public class OdCoatingInspectionProcess {
    private int id;
    private String pipe_no;
    private Date operation_time;
    private String operator_no;
    private String base_coat_thickness_list;
    private String top_coat_thickness_list;
    private String total_coating_thickness_list;
    private int holidays;
    private float holiday_tester_volts;
    private int repairs;
    private float cutback_length;
    private String bevel;
    private String stencil_verification;
    private String surface_condition;
    private String adhesion_test;
    private String upload_files;
    private String remark;
    private String result;
    private String mill_no;
    private String is_sample;

    public OdCoatingInspectionProcess() {
    }

    public OdCoatingInspectionProcess(int id, String pipe_no, Date operation_time, String operator_no, String base_coat_thickness_list, String top_coat_thickness_list, String total_coating_thickness_list, int holidays, float holiday_tester_volts, int repairs, float cutback_length, String bevel, String stencil_verification, String surface_condition, String adhesion_test, String upload_files, String remark, String result, String mill_no, String is_sample) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operation_time = operation_time;
        this.operator_no = operator_no;
        this.base_coat_thickness_list = base_coat_thickness_list;
        this.top_coat_thickness_list = top_coat_thickness_list;
        this.total_coating_thickness_list = total_coating_thickness_list;
        this.holidays = holidays;
        this.holiday_tester_volts = holiday_tester_volts;
        this.repairs = repairs;
        this.cutback_length = cutback_length;
        this.bevel = bevel;
        this.stencil_verification = stencil_verification;
        this.surface_condition = surface_condition;
        this.adhesion_test = adhesion_test;
        this.upload_files = upload_files;
        this.remark = remark;
        this.result = result;
        this.mill_no = mill_no;
        this.is_sample = is_sample;
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

    public String getBase_coat_thickness_list() {
        return base_coat_thickness_list;
    }

    public void setBase_coat_thickness_list(String base_coat_thickness_list) {
        this.base_coat_thickness_list = base_coat_thickness_list;
    }

    public String getTop_coat_thickness_list() {
        return top_coat_thickness_list;
    }

    public void setTop_coat_thickness_list(String top_coat_thickness_list) {
        this.top_coat_thickness_list = top_coat_thickness_list;
    }

    public String getTotal_coating_thickness_list() {
        return total_coating_thickness_list;
    }

    public void setTotal_coating_thickness_list(String total_coating_thickness_list) {
        this.total_coating_thickness_list = total_coating_thickness_list;
    }

    public int getHolidays() {
        return holidays;
    }

    public void setHolidays(int holidays) {
        this.holidays = holidays;
    }

    public float getHoliday_tester_volts() {
        return holiday_tester_volts;
    }

    public void setHoliday_tester_volts(float holiday_tester_volts) {
        this.holiday_tester_volts = holiday_tester_volts;
    }

    public int getRepairs() {
        return repairs;
    }

    public void setRepairs(int repairs) {
        this.repairs = repairs;
    }

    public float getCutback_length() {
        return cutback_length;
    }

    public void setCutback_length(float cutback_length) {
        this.cutback_length = cutback_length;
    }

    public String getBevel() {
        return bevel;
    }

    public void setBevel(String bevel) {
        this.bevel = bevel;
    }

    public String getStencil_verification() {
        return stencil_verification;
    }

    public void setStencil_verification(String stencil_verification) {
        this.stencil_verification = stencil_verification;
    }

    public String getSurface_condition() {
        return surface_condition;
    }

    public void setSurface_condition(String surface_condition) {
        this.surface_condition = surface_condition;
    }

    public String getAdhesion_test() {
        return adhesion_test;
    }

    public void setAdhesion_test(String adhesion_test) {
        this.adhesion_test = adhesion_test;
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
}
