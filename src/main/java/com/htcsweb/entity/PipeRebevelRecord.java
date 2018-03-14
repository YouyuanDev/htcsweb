package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class PipeRebevelRecord {

    private  int id; //流水号
    private String pipe_no;    //钢管编号
    private String operator_no;  //操作工编号
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date operation_time;

    private String squareness;
    private String ovality;
    private String bevel;
    private String rootface;

    private String upload_files;
    private String result;
    private String remark;

    public PipeRebevelRecord() {
    }

    public PipeRebevelRecord(int id, String pipe_no, String operator_no, Date operation_time, String squareness, String ovality, String bevel, String rootface, String upload_files, String result, String remark) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operator_no = operator_no;
        this.operation_time = operation_time;
        this.squareness = squareness;
        this.ovality = ovality;
        this.bevel = bevel;
        this.rootface = rootface;
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

    public String getSquareness() {
        return squareness;
    }

    public void setSquareness(String squareness) {
        this.squareness = squareness;
    }

    public String getOvality() {
        return ovality;
    }

    public void setOvality(String ovality) {
        this.ovality = ovality;
    }

    public String getBevel() {
        return bevel;
    }

    public void setBevel(String bevel) {
        this.bevel = bevel;
    }

    public String getRootface() {
        return rootface;
    }

    public void setRootface(String rootface) {
        this.rootface = rootface;
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
