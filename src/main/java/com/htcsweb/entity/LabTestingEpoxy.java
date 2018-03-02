package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class LabTestingEpoxy {
    private  int id;
    private  String  sample_no;
    private  String  pipe_no;
    private  String operator_no;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  Date operation_time;
    private String porosity;
    private String bend;
    private String adhesion;
    private String curing;
    private String water_immersion;
    private String upload_files;
    private  String remark;
    private  String result;

    public LabTestingEpoxy() {

    }

    public LabTestingEpoxy(int id, String sample_no, String pipe_no, String operator_no, Date operation_time, String porosity, String bend, String adhesion, String curing, String water_immersion, String upload_files, String remark, String result) {
        this.id = id;
        this.sample_no = sample_no;
        this.pipe_no = pipe_no;
        this.operator_no = operator_no;
        this.operation_time = operation_time;
        this.porosity = porosity;
        this.bend = bend;
        this.adhesion = adhesion;
        this.curing = curing;
        this.water_immersion = water_immersion;
        this.upload_files = upload_files;
        this.remark = remark;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSample_no() {
        return sample_no;
    }

    public void setSample_no(String sample_no) {
        this.sample_no = sample_no;
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

    public String getPorosity() {
        return porosity;
    }

    public void setPorosity(String porosity) {
        this.porosity = porosity;
    }

    public String getBend() {
        return bend;
    }

    public void setBend(String bend) {
        this.bend = bend;
    }

    public String getAdhesion() {
        return adhesion;
    }

    public void setAdhesion(String adhesion) {
        this.adhesion = adhesion;
    }

    public String getCuring() {
        return curing;
    }

    public void setCuring(String curing) {
        this.curing = curing;
    }

    public String getWater_immersion() {
        return water_immersion;
    }

    public void setWater_immersion(String water_immersion) {
        this.water_immersion = water_immersion;
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
}
