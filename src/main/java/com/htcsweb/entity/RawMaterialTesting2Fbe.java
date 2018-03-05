package com.htcsweb.entity;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class RawMaterialTesting2Fbe {

    private int id; //流水号
    private String project_no;
    private String sample_no;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String operator_no;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operation_time;
    private String raw_material;
    private String batch_no;
    private float density;
    private float particle_size_32um;
    private float paricle_size_150um;
    private float dsc_tgi;
    private float dsc_tgf;
    private float dsc_delta_h;
    private float gel_time_lt_20s;
    private float gel_time_gt_20s;
    private float powder_volatile;
    private int foaming_cross_sectional;
    private int foaming_interfacial;
    private String impact;
    private String flexibility;
    private  int hot_water;
    private float cd_65_24h;
    private String upload_files;
    private  String remark;
    private  String result;
    public RawMaterialTesting2Fbe() {
    }

    public RawMaterialTesting2Fbe(int id, String project_no, String sample_no, String operator_no, Date operation_time, String raw_material, String batch_no, float density, float particle_size_32um, float paricle_size_150um, float dsc_tgi, float dsc_tgf, float dsc_delta_h, float gel_time_lt_20s, float gel_time_gt_20s, float powder_volatile, int foaming_cross_sectional, int foaming_interfacial, String impact, String flexibility, int hot_water, float cd_65_24h, String upload_files, String remark, String result) {
        this.id = id;
        this.project_no = project_no;
        this.sample_no = sample_no;
        this.operator_no = operator_no;
        this.operation_time = operation_time;
        this.raw_material = raw_material;
        this.batch_no = batch_no;
        this.density = density;
        this.particle_size_32um = particle_size_32um;
        this.paricle_size_150um = paricle_size_150um;
        this.dsc_tgi = dsc_tgi;
        this.dsc_tgf = dsc_tgf;
        this.dsc_delta_h = dsc_delta_h;
        this.gel_time_lt_20s = gel_time_lt_20s;
        this.gel_time_gt_20s = gel_time_gt_20s;
        this.powder_volatile = powder_volatile;
        this.foaming_cross_sectional = foaming_cross_sectional;
        this.foaming_interfacial = foaming_interfacial;
        this.impact = impact;
        this.flexibility = flexibility;
        this.hot_water = hot_water;
        this.cd_65_24h = cd_65_24h;
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

    public String getRaw_material() {
        return raw_material;
    }

    public void setRaw_material(String raw_material) {
        this.raw_material = raw_material;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getParticle_size_32um() {
        return particle_size_32um;
    }

    public void setParticle_size_32um(float particle_size_32um) {
        this.particle_size_32um = particle_size_32um;
    }

    public float getParicle_size_150um() {
        return paricle_size_150um;
    }

    public void setParicle_size_150um(float paricle_size_150um) {
        this.paricle_size_150um = paricle_size_150um;
    }

    public float getDsc_tgi() {
        return dsc_tgi;
    }

    public void setDsc_tgi(float dsc_tgi) {
        this.dsc_tgi = dsc_tgi;
    }

    public float getDsc_tgf() {
        return dsc_tgf;
    }

    public void setDsc_tgf(float dsc_tgf) {
        this.dsc_tgf = dsc_tgf;
    }

    public float getDsc_delta_h() {
        return dsc_delta_h;
    }

    public void setDsc_delta_h(float dsc_delta_h) {
        this.dsc_delta_h = dsc_delta_h;
    }

    public float getGel_time_lt_20s() {
        return gel_time_lt_20s;
    }

    public void setGel_time_lt_20s(float gel_time_lt_20s) {
        this.gel_time_lt_20s = gel_time_lt_20s;
    }

    public float getGel_time_gt_20s() {
        return gel_time_gt_20s;
    }

    public void setGel_time_gt_20s(float gel_time_gt_20s) {
        this.gel_time_gt_20s = gel_time_gt_20s;
    }

    public float getPowder_volatile() {
        return powder_volatile;
    }

    public void setPowder_volatile(float powder_volatile) {
        this.powder_volatile = powder_volatile;
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

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public String getFlexibility() {
        return flexibility;
    }

    public void setFlexibility(String flexibility) {
        this.flexibility = flexibility;
    }

    public int getHot_water() {
        return hot_water;
    }

    public void setHot_water(int hot_water) {
        this.hot_water = hot_water;
    }

    public float getCd_65_24h() {
        return cd_65_24h;
    }

    public void setCd_65_24h(float cd_65_24h) {
        this.cd_65_24h = cd_65_24h;
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
