package com.htcsweb.entity;

import java.util.Date;

public class OdBlastProcess {
    private  int id;
    private  String  pipe_no;
    private  Date operation_time;
    private  String operator_no;
    private  String surface_condition;
    private  float salt_contamination_before_blasting;
    private  float alkaline_dwell_time;
    private  float alkaline_concentration;
    private  float conductivity;
    private  float acid_wash_time;
    private  float acid_concentration;
    private float blast_line_speed;
    private String upload_files;
    private float preheat_temp;
    private  String remark;

    public OdBlastProcess() {
    }


    public OdBlastProcess(int id, String pipe_no, Date operation_time, String operator_no, String surface_condition, float salt_contamination_before_blasting, float alkaline_dwell_time, float alkaline_concentration, float conductivity, float acid_wash_time, float acid_concentration, float blast_line_speed, String upload_files, float preheat_temp, String remark) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operation_time = operation_time;
        this.operator_no = operator_no;
        this.surface_condition = surface_condition;
        this.salt_contamination_before_blasting = salt_contamination_before_blasting;
        this.alkaline_dwell_time = alkaline_dwell_time;
        this.alkaline_concentration = alkaline_concentration;
        this.conductivity = conductivity;
        this.acid_wash_time = acid_wash_time;
        this.acid_concentration = acid_concentration;
        this.blast_line_speed = blast_line_speed;
        this.upload_files = upload_files;
        this.preheat_temp = preheat_temp;
        this.remark = remark;
    }

    public String getUpload_files() {
        return upload_files;
    }

    public void setUpload_files(String upload_files) {
        this.upload_files = upload_files;
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

    public String getSurface_condition() {
        return surface_condition;
    }

    public void setSurface_condition(String surface_condition) {
        this.surface_condition = surface_condition;
    }

    public float getSalt_contamination_before_blasting() {
        return salt_contamination_before_blasting;
    }

    public void setSalt_contamination_before_blasting(float salt_contamination_before_blasting) {
        this.salt_contamination_before_blasting = salt_contamination_before_blasting;
    }

    public float getAlkaline_dwell_time() {
        return alkaline_dwell_time;
    }

    public void setAlkaline_dwell_time(float alkaline_dwell_time) {
        this.alkaline_dwell_time = alkaline_dwell_time;
    }

    public float getAlkaline_concentration() {
        return alkaline_concentration;
    }

    public void setAlkaline_concentration(float alkaline_concentration) {
        this.alkaline_concentration = alkaline_concentration;
    }

    public float getConductivity() {
        return conductivity;
    }

    public void setConductivity(float conductivity) {
        this.conductivity = conductivity;
    }

    public float getAcid_wash_time() {
        return acid_wash_time;
    }

    public void setAcid_wash_time(float acid_wash_time) {
        this.acid_wash_time = acid_wash_time;
    }

    public float getAcid_concentration() {
        return acid_concentration;
    }

    public void setAcid_concentration(float acid_concentration) {
        this.acid_concentration = acid_concentration;
    }

    public float getBlast_line_speed() {
        return blast_line_speed;
    }

    public void setBlast_line_speed(float blast_line_speed) {
        this.blast_line_speed = blast_line_speed;
    }

    public float getPreheat_temp() {
        return preheat_temp;
    }

    public void setPreheat_temp(float preheat_temp) {
        this.preheat_temp = preheat_temp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
