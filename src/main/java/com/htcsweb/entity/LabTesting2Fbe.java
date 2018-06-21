package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class LabTesting2Fbe {
    private  int id;
    private  String  sample_no;
    private String pipe_no;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  Date coating_date;
    private  String operator_no;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date operation_time;
    private float dsc_delta_tg;
    private float dsc_c;
    private int foaming_cross_sectional;
    private int foaming_interfacial;
    private float interfacial_contamination;
    private String flexibility;
    private String impact;
    private int resistance_to_hot_water_98_24h;
    private int resistance_to_hot_water_98_28d;
    private float resistance_to_cd_65_24h;
    private float resistance_to_cd_22_28d;
    private float resistance_to_cd_65_28d;
    private String upload_files;
    private  String remark;
    private  String result;
    private String dsc_pipe_no;
    private String dsc_sample_no;

    public LabTesting2Fbe() {
    }

    public LabTesting2Fbe(int id, String sample_no, String pipe_no, Date coating_date, String operator_no, Date operation_time, float dsc_delta_tg, float dsc_c, int foaming_cross_sectional, int foaming_interfacial, float interfacial_contamination, String flexibility, String impact, int resistance_to_hot_water_98_24h, int resistance_to_hot_water_98_28d, float resistance_to_cd_65_24h, float resistance_to_cd_22_28d, float resistance_to_cd_65_28d, String upload_files, String remark, String result, String dsc_pipe_no, String dsc_sample_no) {
        this.id = id;
        this.sample_no = sample_no;
        this.pipe_no = pipe_no;
        this.coating_date = coating_date;
        this.operator_no = operator_no;
        this.operation_time = operation_time;
        this.dsc_delta_tg = dsc_delta_tg;
        this.dsc_c = dsc_c;
        this.foaming_cross_sectional = foaming_cross_sectional;
        this.foaming_interfacial = foaming_interfacial;
        this.interfacial_contamination = interfacial_contamination;
        this.flexibility = flexibility;
        this.impact = impact;
        this.resistance_to_hot_water_98_24h = resistance_to_hot_water_98_24h;
        this.resistance_to_hot_water_98_28d = resistance_to_hot_water_98_28d;
        this.resistance_to_cd_65_24h = resistance_to_cd_65_24h;
        this.resistance_to_cd_22_28d = resistance_to_cd_22_28d;
        this.resistance_to_cd_65_28d = resistance_to_cd_65_28d;
        this.upload_files = upload_files;
        this.remark = remark;
        this.result = result;
        this.dsc_pipe_no = dsc_pipe_no;
        this.dsc_sample_no = dsc_sample_no;
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

    public Date getCoating_date() {
        return coating_date;
    }

    public void setCoating_date(Date coating_date) {
        this.coating_date = coating_date;
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

    public float getDsc_delta_tg() {
        return dsc_delta_tg;
    }

    public void setDsc_delta_tg(float dsc_delta_tg) {
        this.dsc_delta_tg = dsc_delta_tg;
    }

    public float getDsc_c() {
        return dsc_c;
    }

    public void setDsc_c(float dsc_c) {
        this.dsc_c = dsc_c;
    }

    public int getFoaming_cross_sectional() {
        return foaming_cross_sectional;
    }

    public void setFoaming_cross_sectional(int foaming_cross_sectional) {
        this.foaming_cross_sectional = foaming_cross_sectional;
    }

    public int getFoaming_interfacial() {
        return foaming_interfacial;
    }

    public void setFoaming_interfacial(int foaming_interfacial) {
        this.foaming_interfacial = foaming_interfacial;
    }

    public float getInterfacial_contamination() {
        return interfacial_contamination;
    }

    public void setInterfacial_contamination(float interfacial_contamination) {
        this.interfacial_contamination = interfacial_contamination;
    }

    public String getFlexibility() {
        return flexibility;
    }

    public void setFlexibility(String flexibility) {
        this.flexibility = flexibility;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public int getResistance_to_hot_water_98_24h() {
        return resistance_to_hot_water_98_24h;
    }

    public void setResistance_to_hot_water_98_24h(int resistance_to_hot_water_98_24h) {
        this.resistance_to_hot_water_98_24h = resistance_to_hot_water_98_24h;
    }

    public int getResistance_to_hot_water_98_28d() {
        return resistance_to_hot_water_98_28d;
    }

    public void setResistance_to_hot_water_98_28d(int resistance_to_hot_water_98_28d) {
        this.resistance_to_hot_water_98_28d = resistance_to_hot_water_98_28d;
    }

    public float getResistance_to_cd_65_24h() {
        return resistance_to_cd_65_24h;
    }

    public void setResistance_to_cd_65_24h(float resistance_to_cd_65_24h) {
        this.resistance_to_cd_65_24h = resistance_to_cd_65_24h;
    }

    public float getResistance_to_cd_22_28d() {
        return resistance_to_cd_22_28d;
    }

    public void setResistance_to_cd_22_28d(float resistance_to_cd_22_28d) {
        this.resistance_to_cd_22_28d = resistance_to_cd_22_28d;
    }

    public float getResistance_to_cd_65_28d() {
        return resistance_to_cd_65_28d;
    }

    public void setResistance_to_cd_65_28d(float resistance_to_cd_65_28d) {
        this.resistance_to_cd_65_28d = resistance_to_cd_65_28d;
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

    public String getDsc_pipe_no() {
        return dsc_pipe_no;
    }

    public void setDsc_pipe_no(String dsc_pipe_no) {
        this.dsc_pipe_no = dsc_pipe_no;
    }

    public String getDsc_sample_no() {
        return dsc_sample_no;
    }

    public void setDsc_sample_no(String dsc_sample_no) {
        this.dsc_sample_no = dsc_sample_no;
    }
}
