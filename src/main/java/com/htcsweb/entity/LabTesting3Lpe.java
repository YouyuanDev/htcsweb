package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class LabTesting3Lpe {
    private  int id;
    private  String  sample_no;
    private String pipe_no;
    private  String operator_no;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  Date operation_time;
    private float resistance_to_cd_20_28d;
    private float resistance_to_cd_max_28d;
    private float resistance_to_cd_65_24h;
    private float impact_resistance_23;
    private float impact_resistance_m40;
    private float indentation_hardness_23;
    private float indentation_hardness_70;
    private float elongation_at_break;
    private float coating_resistivity;
    private float thermal_degradation;
    private String upload_files;
    private  String remark;
    private  String result;

    public LabTesting3Lpe() {
    }

    public LabTesting3Lpe(int id, String sample_no, String pipe_no, String operator_no, Date operation_time, float resistance_to_cd_20_28d, float resistance_to_cd_max_28d, float resistance_to_cd_65_24h, float impact_resistance_23, float impact_resistance_m40, float indentation_hardness_23, float indentation_hardness_70, float elongation_at_break, float coating_resistivity, float thermal_degradation, String upload_files, String remark, String result) {
        this.id = id;
        this.sample_no = sample_no;
        this.pipe_no = pipe_no;
        this.operator_no = operator_no;
        this.operation_time = operation_time;
        this.resistance_to_cd_20_28d = resistance_to_cd_20_28d;
        this.resistance_to_cd_max_28d = resistance_to_cd_max_28d;
        this.resistance_to_cd_65_24h = resistance_to_cd_65_24h;
        this.impact_resistance_23 = impact_resistance_23;
        this.impact_resistance_m40 = impact_resistance_m40;
        this.indentation_hardness_23 = indentation_hardness_23;
        this.indentation_hardness_70 = indentation_hardness_70;
        this.elongation_at_break = elongation_at_break;
        this.coating_resistivity = coating_resistivity;
        this.thermal_degradation = thermal_degradation;
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

    public float getResistance_to_cd_20_28d() {
        return resistance_to_cd_20_28d;
    }

    public void setResistance_to_cd_20_28d(float resistance_to_cd_20_28d) {
        this.resistance_to_cd_20_28d = resistance_to_cd_20_28d;
    }

    public float getResistance_to_cd_max_28d() {
        return resistance_to_cd_max_28d;
    }

    public void setResistance_to_cd_max_28d(float resistance_to_cd_max_28d) {
        this.resistance_to_cd_max_28d = resistance_to_cd_max_28d;
    }

    public float getResistance_to_cd_65_24h() {
        return resistance_to_cd_65_24h;
    }

    public void setResistance_to_cd_65_24h(float resistance_to_cd_65_24h) {
        this.resistance_to_cd_65_24h = resistance_to_cd_65_24h;
    }

    public float getImpact_resistance_23() {
        return impact_resistance_23;
    }

    public void setImpact_resistance_23(float impact_resistance_23) {
        this.impact_resistance_23 = impact_resistance_23;
    }

    public float getImpact_resistance_m40() {
        return impact_resistance_m40;
    }

    public void setImpact_resistance_m40(float impact_resistance_m40) {
        this.impact_resistance_m40 = impact_resistance_m40;
    }

    public float getIndentation_hardness_23() {
        return indentation_hardness_23;
    }

    public void setIndentation_hardness_23(float indentation_hardness_23) {
        this.indentation_hardness_23 = indentation_hardness_23;
    }

    public float getIndentation_hardness_70() {
        return indentation_hardness_70;
    }

    public void setIndentation_hardness_70(float indentation_hardness_70) {
        this.indentation_hardness_70 = indentation_hardness_70;
    }

    public float getElongation_at_break() {
        return elongation_at_break;
    }

    public void setElongation_at_break(float elongation_at_break) {
        this.elongation_at_break = elongation_at_break;
    }

    public float getCoating_resistivity() {
        return coating_resistivity;
    }

    public void setCoating_resistivity(float coating_resistivity) {
        this.coating_resistivity = coating_resistivity;
    }

    public float getThermal_degradation() {
        return thermal_degradation;
    }

    public void setThermal_degradation(float thermal_degradation) {
        this.thermal_degradation = thermal_degradation;
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
