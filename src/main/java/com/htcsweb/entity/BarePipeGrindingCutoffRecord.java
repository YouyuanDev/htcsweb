package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class BarePipeGrindingCutoffRecord {

    private int id;
    private String pipe_no;
    private Date operation_time;
    private String operator_no;
    private String odid;
    private String grinding;
    private String remaining_wall_thickness_list;
    private float cut_off_length;
    private float original_pipe_length;
    private float pipe_length_after_cut;
    private String upload_files;
    private String remark;
    private String result;
    private String mill_no;

    public BarePipeGrindingCutoffRecord() {
    }

    public BarePipeGrindingCutoffRecord(int id, String pipe_no, Date operation_time, String operator_no, String odid, String grinding, String remaining_wall_thickness_list, float cut_off_length, float original_pipe_length, float pipe_length_after_cut, String upload_files, String remark, String result, String mill_no) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operation_time = operation_time;
        this.operator_no = operator_no;
        this.odid = odid;
        this.grinding = grinding;
        this.remaining_wall_thickness_list = remaining_wall_thickness_list;
        this.cut_off_length = cut_off_length;
        this.original_pipe_length = original_pipe_length;
        this.pipe_length_after_cut = pipe_length_after_cut;
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

    public String getOdid() {
        return odid;
    }

    public void setOdid(String odid) {
        this.odid = odid;
    }

    public String getGrinding() {
        return grinding;
    }

    public void setGrinding(String grinding) {
        this.grinding = grinding;
    }

    public String getRemaining_wall_thickness_list() {
        return remaining_wall_thickness_list;
    }

    public void setRemaining_wall_thickness_list(String remaining_wall_thickness_list) {
        this.remaining_wall_thickness_list = remaining_wall_thickness_list;
    }

    public float getCut_off_length() {
        return cut_off_length;
    }

    public void setCut_off_length(float cut_off_length) {
        this.cut_off_length = cut_off_length;
    }

    public float getOriginal_pipe_length() {
        return original_pipe_length;
    }

    public void setOriginal_pipe_length(float original_pipe_length) {
        this.original_pipe_length = original_pipe_length;
    }

    public float getPipe_length_after_cut() {
        return pipe_length_after_cut;
    }

    public void setPipe_length_after_cut(float pipe_length_after_cut) {
        this.pipe_length_after_cut = pipe_length_after_cut;
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
