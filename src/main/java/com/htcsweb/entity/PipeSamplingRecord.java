package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class PipeSamplingRecord {

    private  int id; //流水号
    private String pipe_no;    //钢管编号
    private String operator_no;  //操作工编号
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date operation_time;

    private String sample_no;
    private float cut_off_length;
    private float original_pipe_length;
    private float pipe_length_after_cut;

    private String upload_files;
    private String result;
    private String remark;

    public PipeSamplingRecord() {
    }

    public PipeSamplingRecord(int id, String pipe_no, String operator_no, Date operation_time, String sample_no, float cut_off_length, float original_pipe_length, float pipe_length_after_cut, String upload_files, String result, String remark) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operator_no = operator_no;
        this.operation_time = operation_time;
        this.sample_no = sample_no;
        this.cut_off_length = cut_off_length;
        this.original_pipe_length = original_pipe_length;
        this.pipe_length_after_cut = pipe_length_after_cut;
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

    public String getSample_no() {
        return sample_no;
    }

    public void setSample_no(String sample_no) {
        this.sample_no = sample_no;
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
