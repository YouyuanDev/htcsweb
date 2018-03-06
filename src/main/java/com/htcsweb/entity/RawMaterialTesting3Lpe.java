package com.htcsweb.entity;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class RawMaterialTesting3Lpe {

    private int id; //流水号
    private String project_no;
    private String sample_no;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String operator_no;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operation_time;
    private String epoxy_raw_material;
    private String epoxy_batch_no;
    private String adhesion_raw_material;
    private String adhesion_batch_no;
    private String polyethylene_raw_material;
    private String polyethylene_batch_no;
    private float epoxy_cure_time;
    private float epoxy_gel_time;
    private float epoxy_moisture_content;
    private float epoxy_particle_size_150um;
    private float epoxy_particle_size_250um;
    private String epoxy_density;
    private String epoxy_thermal_characteristics;
    private float adhesion_flow_rate;
    private float polyethylene_flow_rate;
    private String upload_files;
    private String remark;
    private String result;
    public RawMaterialTesting3Lpe() {
    }

    public RawMaterialTesting3Lpe(int id, String project_no, String sample_no, String operator_no, Date operation_time, String epoxy_raw_material, String epoxy_batch_no, String adhesion_raw_material, String adhesion_batch_no, String polyethylene_raw_material, String polyethylene_batch_no, float epoxy_cure_time, float epoxy_gel_time, float epoxy_moisture_content, float epoxy_particle_size_150um, float epoxy_particle_size_250um, String epoxy_density, String epoxy_thermal_characteristics, float adhesion_flow_rate, float polyethylene_flow_rate, String upload_files, String remark, String result) {
        this.id = id;
        this.project_no = project_no;
        this.sample_no = sample_no;
        this.operator_no = operator_no;
        this.operation_time = operation_time;
        this.epoxy_raw_material = epoxy_raw_material;
        this.epoxy_batch_no = epoxy_batch_no;
        this.adhesion_raw_material = adhesion_raw_material;
        this.adhesion_batch_no = adhesion_batch_no;
        this.polyethylene_raw_material = polyethylene_raw_material;
        this.polyethylene_batch_no = polyethylene_batch_no;
        this.epoxy_cure_time = epoxy_cure_time;
        this.epoxy_gel_time = epoxy_gel_time;
        this.epoxy_moisture_content = epoxy_moisture_content;
        this.epoxy_particle_size_150um = epoxy_particle_size_150um;
        this.epoxy_particle_size_250um = epoxy_particle_size_250um;
        this.epoxy_density = epoxy_density;
        this.epoxy_thermal_characteristics = epoxy_thermal_characteristics;
        this.adhesion_flow_rate = adhesion_flow_rate;
        this.polyethylene_flow_rate = polyethylene_flow_rate;
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

    public String getEpoxy_raw_material() {
        return epoxy_raw_material;
    }

    public void setEpoxy_raw_material(String epoxy_raw_material) {
        this.epoxy_raw_material = epoxy_raw_material;
    }

    public String getEpoxy_batch_no() {
        return epoxy_batch_no;
    }

    public void setEpoxy_batch_no(String epoxy_batch_no) {
        this.epoxy_batch_no = epoxy_batch_no;
    }

    public String getAdhesion_raw_material() {
        return adhesion_raw_material;
    }

    public void setAdhesion_raw_material(String adhesion_raw_material) {
        this.adhesion_raw_material = adhesion_raw_material;
    }

    public String getAdhesion_batch_no() {
        return adhesion_batch_no;
    }

    public void setAdhesion_batch_no(String adhesion_batch_no) {
        this.adhesion_batch_no = adhesion_batch_no;
    }

    public String getPolyethylene_raw_material() {
        return polyethylene_raw_material;
    }

    public void setPolyethylene_raw_material(String polyethylene_raw_material) {
        this.polyethylene_raw_material = polyethylene_raw_material;
    }

    public String getPolyethylene_batch_no() {
        return polyethylene_batch_no;
    }

    public void setPolyethylene_batch_no(String polyethylene_batch_no) {
        this.polyethylene_batch_no = polyethylene_batch_no;
    }

    public float getEpoxy_cure_time() {
        return epoxy_cure_time;
    }

    public void setEpoxy_cure_time(float epoxy_cure_time) {
        this.epoxy_cure_time = epoxy_cure_time;
    }

    public float getEpoxy_gel_time() {
        return epoxy_gel_time;
    }

    public void setEpoxy_gel_time(float epoxy_gel_time) {
        this.epoxy_gel_time = epoxy_gel_time;
    }

    public float getEpoxy_moisture_content() {
        return epoxy_moisture_content;
    }

    public void setEpoxy_moisture_content(float epoxy_moisture_content) {
        this.epoxy_moisture_content = epoxy_moisture_content;
    }

    public float getEpoxy_particle_size_150um() {
        return epoxy_particle_size_150um;
    }

    public void setEpoxy_particle_size_150um(float epoxy_particle_size_150um) {
        this.epoxy_particle_size_150um = epoxy_particle_size_150um;
    }

    public float getEpoxy_particle_size_250um() {
        return epoxy_particle_size_250um;
    }

    public void setEpoxy_particle_size_250um(float epoxy_particle_size_250um) {
        this.epoxy_particle_size_250um = epoxy_particle_size_250um;
    }

    public String getEpoxy_density() {
        return epoxy_density;
    }

    public void setEpoxy_density(String epoxy_density) {
        this.epoxy_density = epoxy_density;
    }

    public String getEpoxy_thermal_characteristics() {
        return epoxy_thermal_characteristics;
    }

    public void setEpoxy_thermal_characteristics(String epoxy_thermal_characteristics) {
        this.epoxy_thermal_characteristics = epoxy_thermal_characteristics;
    }

    public float getAdhesion_flow_rate() {
        return adhesion_flow_rate;
    }

    public void setAdhesion_flow_rate(float adhesion_flow_rate) {
        this.adhesion_flow_rate = adhesion_flow_rate;
    }

    public float getPolyethylene_flow_rate() {
        return polyethylene_flow_rate;
    }

    public void setPolyethylene_flow_rate(float polyethylene_flow_rate) {
        this.polyethylene_flow_rate = polyethylene_flow_rate;
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