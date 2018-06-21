package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class LabTestingAcceptanceCriteria2Fbe {
    private  int id;
    private String lab_testing_acceptance_criteria_no;
    private float interfacial_contamination_max;
    private float interfacial_contamination_min;
    private int foaming_cross_sectional_max;
    private int foaming_cross_sectional_min;
    private int foaming_interfacial_max;
    private int foaming_interfacial_min;
    private int resistance_to_hot_water_98_24h_max;
    private int resistance_to_hot_water_98_24h_min;
    private int resistance_to_hot_water_98_28d_max;
    private int resistance_to_hot_water_98_28d_min;
    private float resistance_to_cd_65_24h_max;
    private float resistance_to_cd_65_24h_min;
    private float resistance_to_cd_22_28d_max;
    private float resistance_to_cd_22_28d_min;
    private float resistance_to_cd_65_28d_max;
    private float resistance_to_cd_65_28d_min;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date last_update_time;
    private float dsc_delta_tg_max;
    private float dsc_delta_tg_min;
    private float dsc_c_max;
    private float dsc_c_min;
    public LabTestingAcceptanceCriteria2Fbe() {
    }

    public LabTestingAcceptanceCriteria2Fbe(int id, String lab_testing_acceptance_criteria_no, float interfacial_contamination_max, float interfacial_contamination_min, int foaming_cross_sectional_max, int foaming_cross_sectional_min, int foaming_interfacial_max, int foaming_interfacial_min, int resistance_to_hot_water_98_24h_max, int resistance_to_hot_water_98_24h_min, int resistance_to_hot_water_98_28d_max, int resistance_to_hot_water_98_28d_min, float resistance_to_cd_65_24h_max, float resistance_to_cd_65_24h_min, float resistance_to_cd_22_28d_max, float resistance_to_cd_22_28d_min, float resistance_to_cd_65_28d_max, float resistance_to_cd_65_28d_min, Date last_update_time, float dsc_delta_tg_max, float dsc_delta_tg_min, float dsc_c_max, float dsc_c_min) {
        this.id = id;
        this.lab_testing_acceptance_criteria_no = lab_testing_acceptance_criteria_no;
        this.interfacial_contamination_max = interfacial_contamination_max;
        this.interfacial_contamination_min = interfacial_contamination_min;
        this.foaming_cross_sectional_max = foaming_cross_sectional_max;
        this.foaming_cross_sectional_min = foaming_cross_sectional_min;
        this.foaming_interfacial_max = foaming_interfacial_max;
        this.foaming_interfacial_min = foaming_interfacial_min;
        this.resistance_to_hot_water_98_24h_max = resistance_to_hot_water_98_24h_max;
        this.resistance_to_hot_water_98_24h_min = resistance_to_hot_water_98_24h_min;
        this.resistance_to_hot_water_98_28d_max = resistance_to_hot_water_98_28d_max;
        this.resistance_to_hot_water_98_28d_min = resistance_to_hot_water_98_28d_min;
        this.resistance_to_cd_65_24h_max = resistance_to_cd_65_24h_max;
        this.resistance_to_cd_65_24h_min = resistance_to_cd_65_24h_min;
        this.resistance_to_cd_22_28d_max = resistance_to_cd_22_28d_max;
        this.resistance_to_cd_22_28d_min = resistance_to_cd_22_28d_min;
        this.resistance_to_cd_65_28d_max = resistance_to_cd_65_28d_max;
        this.resistance_to_cd_65_28d_min = resistance_to_cd_65_28d_min;
        this.last_update_time = last_update_time;
        this.dsc_delta_tg_max = dsc_delta_tg_max;
        this.dsc_delta_tg_min = dsc_delta_tg_min;
        this.dsc_c_max = dsc_c_max;
        this.dsc_c_min = dsc_c_min;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLab_testing_acceptance_criteria_no() {
        return lab_testing_acceptance_criteria_no;
    }

    public void setLab_testing_acceptance_criteria_no(String lab_testing_acceptance_criteria_no) {
        this.lab_testing_acceptance_criteria_no = lab_testing_acceptance_criteria_no;
    }

    public float getInterfacial_contamination_max() {
        return interfacial_contamination_max;
    }

    public void setInterfacial_contamination_max(float interfacial_contamination_max) {
        this.interfacial_contamination_max = interfacial_contamination_max;
    }

    public float getInterfacial_contamination_min() {
        return interfacial_contamination_min;
    }

    public void setInterfacial_contamination_min(float interfacial_contamination_min) {
        this.interfacial_contamination_min = interfacial_contamination_min;
    }

    public int getFoaming_cross_sectional_max() {
        return foaming_cross_sectional_max;
    }

    public void setFoaming_cross_sectional_max(int foaming_cross_sectional_max) {
        this.foaming_cross_sectional_max = foaming_cross_sectional_max;
    }

    public int getFoaming_cross_sectional_min() {
        return foaming_cross_sectional_min;
    }

    public void setFoaming_cross_sectional_min(int foaming_cross_sectional_min) {
        this.foaming_cross_sectional_min = foaming_cross_sectional_min;
    }

    public int getFoaming_interfacial_max() {
        return foaming_interfacial_max;
    }

    public void setFoaming_interfacial_max(int foaming_interfacial_max) {
        this.foaming_interfacial_max = foaming_interfacial_max;
    }

    public int getFoaming_interfacial_min() {
        return foaming_interfacial_min;
    }

    public void setFoaming_interfacial_min(int foaming_interfacial_min) {
        this.foaming_interfacial_min = foaming_interfacial_min;
    }

    public int getResistance_to_hot_water_98_24h_max() {
        return resistance_to_hot_water_98_24h_max;
    }

    public void setResistance_to_hot_water_98_24h_max(int resistance_to_hot_water_98_24h_max) {
        this.resistance_to_hot_water_98_24h_max = resistance_to_hot_water_98_24h_max;
    }

    public int getResistance_to_hot_water_98_24h_min() {
        return resistance_to_hot_water_98_24h_min;
    }

    public void setResistance_to_hot_water_98_24h_min(int resistance_to_hot_water_98_24h_min) {
        this.resistance_to_hot_water_98_24h_min = resistance_to_hot_water_98_24h_min;
    }

    public int getResistance_to_hot_water_98_28d_max() {
        return resistance_to_hot_water_98_28d_max;
    }

    public void setResistance_to_hot_water_98_28d_max(int resistance_to_hot_water_98_28d_max) {
        this.resistance_to_hot_water_98_28d_max = resistance_to_hot_water_98_28d_max;
    }

    public int getResistance_to_hot_water_98_28d_min() {
        return resistance_to_hot_water_98_28d_min;
    }

    public void setResistance_to_hot_water_98_28d_min(int resistance_to_hot_water_98_28d_min) {
        this.resistance_to_hot_water_98_28d_min = resistance_to_hot_water_98_28d_min;
    }

    public float getResistance_to_cd_65_24h_max() {
        return resistance_to_cd_65_24h_max;
    }

    public void setResistance_to_cd_65_24h_max(float resistance_to_cd_65_24h_max) {
        this.resistance_to_cd_65_24h_max = resistance_to_cd_65_24h_max;
    }

    public float getResistance_to_cd_65_24h_min() {
        return resistance_to_cd_65_24h_min;
    }

    public void setResistance_to_cd_65_24h_min(float resistance_to_cd_65_24h_min) {
        this.resistance_to_cd_65_24h_min = resistance_to_cd_65_24h_min;
    }

    public float getResistance_to_cd_22_28d_max() {
        return resistance_to_cd_22_28d_max;
    }

    public void setResistance_to_cd_22_28d_max(float resistance_to_cd_22_28d_max) {
        this.resistance_to_cd_22_28d_max = resistance_to_cd_22_28d_max;
    }

    public float getResistance_to_cd_22_28d_min() {
        return resistance_to_cd_22_28d_min;
    }

    public void setResistance_to_cd_22_28d_min(float resistance_to_cd_22_28d_min) {
        this.resistance_to_cd_22_28d_min = resistance_to_cd_22_28d_min;
    }

    public float getResistance_to_cd_65_28d_max() {
        return resistance_to_cd_65_28d_max;
    }

    public void setResistance_to_cd_65_28d_max(float resistance_to_cd_65_28d_max) {
        this.resistance_to_cd_65_28d_max = resistance_to_cd_65_28d_max;
    }

    public float getResistance_to_cd_65_28d_min() {
        return resistance_to_cd_65_28d_min;
    }

    public void setResistance_to_cd_65_28d_min(float resistance_to_cd_65_28d_min) {
        this.resistance_to_cd_65_28d_min = resistance_to_cd_65_28d_min;
    }

    public Date getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(Date last_update_time) {
        this.last_update_time = last_update_time;
    }

    public float getDsc_delta_tg_max() {
        return dsc_delta_tg_max;
    }

    public void setDsc_delta_tg_max(float dsc_delta_tg_max) {
        this.dsc_delta_tg_max = dsc_delta_tg_max;
    }

    public float getDsc_delta_tg_min() {
        return dsc_delta_tg_min;
    }

    public void setDsc_delta_tg_min(float dsc_delta_tg_min) {
        this.dsc_delta_tg_min = dsc_delta_tg_min;
    }

    public float getDsc_c_max() {
        return dsc_c_max;
    }

    public void setDsc_c_max(float dsc_c_max) {
        this.dsc_c_max = dsc_c_max;
    }

    public float getDsc_c_min() {
        return dsc_c_min;
    }

    public void setDsc_c_min(float dsc_c_min) {
        this.dsc_c_min = dsc_c_min;
    }
}
