package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class OdBlastProcess {
    private  int id;
    private  String  pipe_no;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  Date operation_time;
    private  String operator_no;
    private  String surface_condition;
    private  float salt_contamination_before_blasting;
    private  float alkaline_dwell_time;
    private  float alkaline_concentration;
    private  float abrasive_conductivity;
    private  float acid_wash_time;
    private  float acid_concentration;
    private float blast_line_speed;
    private float preheat_temp;
    private String upload_files;
    private  String remark;
    private  String result;
    private  String mill_no;
    private  String marking;

    private float rinse_water_conductivity;


    public OdBlastProcess() {
    }

    public OdBlastProcess(int id, String pipe_no, Date operation_time, String operator_no, String surface_condition, float salt_contamination_before_blasting, float alkaline_dwell_time, float alkaline_concentration, float abrasive_conductivity, float acid_wash_time, float acid_concentration, float blast_line_speed, float preheat_temp, String upload_files, String remark, String result, String mill_no, String marking, float rinse_water_conductivity) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operation_time = operation_time;
        this.operator_no = operator_no;
        this.surface_condition = surface_condition;
        this.salt_contamination_before_blasting = salt_contamination_before_blasting;
        this.alkaline_dwell_time = alkaline_dwell_time;
        this.alkaline_concentration = alkaline_concentration;
        this.abrasive_conductivity = abrasive_conductivity;
        this.acid_wash_time = acid_wash_time;
        this.acid_concentration = acid_concentration;
        this.blast_line_speed = blast_line_speed;
        this.preheat_temp = preheat_temp;
        this.upload_files = upload_files;
        this.remark = remark;
        this.result = result;
        this.mill_no = mill_no;
        this.marking = marking;
        this.rinse_water_conductivity = rinse_water_conductivity;
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

    public float getAbrasive_conductivity() {
        return abrasive_conductivity;
    }

    public void setAbrasive_conductivity(float abrasive_conductivity) {
        this.abrasive_conductivity = abrasive_conductivity;
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

    public String getMarking() {
        return marking;
    }

    public void setMarking(String marking) {
        this.marking = marking;
    }

    public float getRinse_water_conductivity() {
        return rinse_water_conductivity;
    }

    public void setRinse_water_conductivity(float rinse_water_conductivity) {
        this.rinse_water_conductivity = rinse_water_conductivity;
    }
}
