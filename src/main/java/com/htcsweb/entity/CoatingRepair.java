package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class CoatingRepair {

    private  int id; //流水号
    private String pipe_no;    //钢管编号
    private String operator_no;  //操作工编号
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date operation_time;
    private String coating_type;
    private String odid;
    private String repair_size;
    private int repair_number;
    private int holiday_number;
    private String repair_method;
    private String unqualified_reason;
    private String inspector_no;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date inspection_time;
    private String surface_condition;
    private String repair_thickness;
    private String holiday_testing;
    private String adhesion;
    private String upload_files;
    private String result;
    private String remark;

    public CoatingRepair() {
    }

    public CoatingRepair(int id, String pipe_no, String operator_no, Date operation_time, String coating_type, String odid, String repair_size, int repair_number, int holiday_number, String repair_method, String unqualified_reason, String inspector_no, Date inspection_time, String surface_condition, String repair_thickness, String holiday_testing, String adhesion, String upload_files, String result, String remark) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operator_no = operator_no;
        this.operation_time = operation_time;
        this.coating_type = coating_type;
        this.odid = odid;
        this.repair_size = repair_size;
        this.repair_number = repair_number;
        this.holiday_number = holiday_number;
        this.repair_method = repair_method;
        this.unqualified_reason = unqualified_reason;
        this.inspector_no = inspector_no;
        this.inspection_time = inspection_time;
        this.surface_condition = surface_condition;
        this.repair_thickness = repair_thickness;
        this.holiday_testing = holiday_testing;
        this.adhesion = adhesion;
        this.upload_files = upload_files;
        this.result = result;
        this.remark = remark;
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

    public String getOperator_no() {
        return operator_no;
    }

    public void setOperator_no(String operator_no) {
        this.operator_no = operator_no;
    }

    public Date getOperation_time() {
        return operation_time;
    }

    public void setOperation_time(Date operation_time) {
        this.operation_time = operation_time;
    }

    public String getCoating_type() {
        return coating_type;
    }

    public void setCoating_type(String coating_type) {
        this.coating_type = coating_type;
    }

    public String getOdid() {
        return odid;
    }

    public void setOdid(String odid) {
        this.odid = odid;
    }

    public String getRepair_size() {
        return repair_size;
    }

    public void setRepair_size(String repair_size) {
        this.repair_size = repair_size;
    }

    public int getRepair_number() {
        return repair_number;
    }

    public void setRepair_number(int repair_number) {
        this.repair_number = repair_number;
    }

    public int getHoliday_number() {
        return holiday_number;
    }

    public void setHoliday_number(int holiday_number) {
        this.holiday_number = holiday_number;
    }

    public String getRepair_method() {
        return repair_method;
    }

    public void setRepair_method(String repair_method) {
        this.repair_method = repair_method;
    }

    public String getUnqualified_reason() {
        return unqualified_reason;
    }

    public void setUnqualified_reason(String unqualified_reason) {
        this.unqualified_reason = unqualified_reason;
    }

    public String getInspector_no() {
        return inspector_no;
    }

    public void setInspector_no(String inspector_no) {
        this.inspector_no = inspector_no;
    }

    public Date getInspection_time() {
        return inspection_time;
    }

    public void setInspection_time(Date inspection_time) {
        this.inspection_time = inspection_time;
    }

    public String getSurface_condition() {
        return surface_condition;
    }

    public void setSurface_condition(String surface_condition) {
        this.surface_condition = surface_condition;
    }

    public String getRepair_thickness() {
        return repair_thickness;
    }

    public void setRepair_thickness(String repair_thickness) {
        this.repair_thickness = repair_thickness;
    }

    public String getHoliday_testing() {
        return holiday_testing;
    }

    public void setHoliday_testing(String holiday_testing) {
        this.holiday_testing = holiday_testing;
    }

    public String getAdhesion() {
        return adhesion;
    }

    public void setAdhesion(String adhesion) {
        this.adhesion = adhesion;
    }

    public String getUpload_files() {
        return upload_files;
    }

    public void setUpload_files(String upload_files) {
        this.upload_files = upload_files;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
