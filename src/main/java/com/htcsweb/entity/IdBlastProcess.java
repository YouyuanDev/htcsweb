package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class IdBlastProcess {
    private  int id;
    private  String  pipe_no;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  Date operation_time;
    private  String operator_no;
    private String original_pipe_no;
    private String new_pipe_no;
    private String pipe_no_update;
    private String upload_files;
    private  String remark;
    private  String result;
    private  String mill_no;
    private  float salt_contamination_before_blasting;
    private  String internal_surface_condition;
    private  String external_coating_condition;
    private  String marking;

    public IdBlastProcess() {
    }

    public IdBlastProcess(int id, String pipe_no, Date operation_time, String operator_no, String original_pipe_no, String new_pipe_no, String pipe_no_update, String upload_files, String remark, String result, String mill_no, float salt_contamination_before_blasting, String internal_surface_condition, String external_coating_condition, String marking) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operation_time = operation_time;
        this.operator_no = operator_no;
        this.original_pipe_no = original_pipe_no;
        this.new_pipe_no = new_pipe_no;
        this.pipe_no_update = pipe_no_update;
        this.upload_files = upload_files;
        this.remark = remark;
        this.result = result;
        this.mill_no = mill_no;
        this.salt_contamination_before_blasting = salt_contamination_before_blasting;
        this.internal_surface_condition = internal_surface_condition;
        this.external_coating_condition = external_coating_condition;
        this.marking = marking;
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

    public String getOriginal_pipe_no() {
        return original_pipe_no;
    }

    public void setOriginal_pipe_no(String original_pipe_no) {
        this.original_pipe_no = original_pipe_no;
    }

    public String getNew_pipe_no() {
        return new_pipe_no;
    }

    public void setNew_pipe_no(String new_pipe_no) {
        this.new_pipe_no = new_pipe_no;
    }

    public String getPipe_no_update() {
        return pipe_no_update;
    }

    public void setPipe_no_update(String pipe_no_update) {
        this.pipe_no_update = pipe_no_update;
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

    public float getSalt_contamination_before_blasting() {
        return salt_contamination_before_blasting;
    }

    public void setSalt_contamination_before_blasting(float salt_contamination_before_blasting) {
        this.salt_contamination_before_blasting = salt_contamination_before_blasting;
    }

    public String getInternal_surface_condition() {
        return internal_surface_condition;
    }

    public void setInternal_surface_condition(String internal_surface_condition) {
        this.internal_surface_condition = internal_surface_condition;
    }

    public String getExternal_coating_condition() {
        return external_coating_condition;
    }

    public void setExternal_coating_condition(String external_coating_condition) {
        this.external_coating_condition = external_coating_condition;
    }

    public String getMarking() {
        return marking;
    }

    public void setMarking(String marking) {
        this.marking = marking;
    }
}
