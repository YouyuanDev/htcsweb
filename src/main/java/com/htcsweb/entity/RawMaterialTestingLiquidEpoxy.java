package com.htcsweb.entity;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class RawMaterialTestingLiquidEpoxy {

    private int id; //流水号
    private String project_no;
    private String sample_no;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String operator_no;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operation_time;
    private String pot_life;
    private String viscosity;
    private String upload_files;
    private String remark;
    private String result;

    public RawMaterialTestingLiquidEpoxy() {
    }

    public RawMaterialTestingLiquidEpoxy(int id, String project_no, String sample_no, String operator_no, Date operation_time, String pot_life, String viscosity, String upload_files, String remark, String result) {
        this.id = id;
        this.project_no = project_no;
        this.sample_no = sample_no;
        this.operator_no = operator_no;
        this.operation_time = operation_time;
        this.pot_life = pot_life;
        this.viscosity = viscosity;
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

    public String getProject_no() {
        return project_no;
    }

    public void setProject_no(String project_no) {
        this.project_no = project_no;
    }

    public String getSample_no() {
        return sample_no;
    }

    public void setSample_no(String sample_no) {
        this.sample_no = sample_no;
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

    public String getPot_life() {
        return pot_life;
    }

    public void setPot_life(String pot_life) {
        this.pot_life = pot_life;
    }

    public String getViscosity() {
        return viscosity;
    }

    public void setViscosity(String viscosity) {
        this.viscosity = viscosity;
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