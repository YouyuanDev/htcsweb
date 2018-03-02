package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class LabTestingAcceptanceCriteria3Lpe {
    private  int id;
    private String lab_testing_acceptance_criteria_no;
    private float resistance_to_cd_20_28d_max;
    private float resistance_to_cd_20_28d_min;
    private float resistance_to_cd_max_28d_max;
    private float resistance_to_cd_max_28d_min;
    private float resistance_to_cd_65_24h_max;
    private float resistance_to_cd_65_24h_min;
    private float impact_resistance_23_max;
    private float impact_resistance_23_min;
    private float impact_resistance_m40_max;
    private float impact_resistance_m40_min;
    private float indentation_hardness_23_max;
    private float indentation_hardness_23_min;
    private float indentation_hardness_70_max;
    private float indentation_hardness_70_min;
    private float elongation_at_break_max;
    private float  elongation_at_break_min;
    private float coating_resistivity_max;
    private float coating_resistivity_min;
    private float thermal_degradation_max;
    private float thermal_degradation_min;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date last_update_time;

    public LabTestingAcceptanceCriteria3Lpe() {
    }

    public LabTestingAcceptanceCriteria3Lpe(int id, String lab_testing_acceptance_criteria_no, float resistance_to_cd_20_28d_max, float resistance_to_cd_20_28d_min, float resistance_to_cd_max_28d_max, float resistance_to_cd_max_28d_min, float resistance_to_cd_65_24h_max, float resistance_to_cd_65_24h_min, float impact_resistance_23_max, float impact_resistance_23_min, float impact_resistance_m40_max, float impact_resistance_m40_min, float indentation_hardness_23_max, float indentation_hardness_23_min, float indentation_hardness_70_max, float indentation_hardness_70_min, float elongation_at_break_max, float elongation_at_break_min, float coating_resistivity_max, float coating_resistivity_min, float thermal_degradation_max, float thermal_degradation_min, Date last_update_time) {
        this.id = id;
        this.lab_testing_acceptance_criteria_no = lab_testing_acceptance_criteria_no;
        this.resistance_to_cd_20_28d_max = resistance_to_cd_20_28d_max;
        this.resistance_to_cd_20_28d_min = resistance_to_cd_20_28d_min;
        this.resistance_to_cd_max_28d_max = resistance_to_cd_max_28d_max;
        this.resistance_to_cd_max_28d_min = resistance_to_cd_max_28d_min;
        this.resistance_to_cd_65_24h_max = resistance_to_cd_65_24h_max;
        this.resistance_to_cd_65_24h_min = resistance_to_cd_65_24h_min;
        this.impact_resistance_23_max = impact_resistance_23_max;
        this.impact_resistance_23_min = impact_resistance_23_min;
        this.impact_resistance_m40_max = impact_resistance_m40_max;
        this.impact_resistance_m40_min = impact_resistance_m40_min;
        this.indentation_hardness_23_max = indentation_hardness_23_max;
        this.indentation_hardness_23_min = indentation_hardness_23_min;
        this.indentation_hardness_70_max = indentation_hardness_70_max;
        this.indentation_hardness_70_min = indentation_hardness_70_min;
        this.elongation_at_break_max = elongation_at_break_max;
        this.elongation_at_break_min = elongation_at_break_min;
        this.coating_resistivity_max = coating_resistivity_max;
        this.coating_resistivity_min = coating_resistivity_min;
        this.thermal_degradation_max = thermal_degradation_max;
        this.thermal_degradation_min = thermal_degradation_min;
        this.last_update_time = last_update_time;
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

    public float getResistance_to_cd_20_28d_max() {
        return resistance_to_cd_20_28d_max;
    }

    public void setResistance_to_cd_20_28d_max(float resistance_to_cd_20_28d_max) {
        this.resistance_to_cd_20_28d_max = resistance_to_cd_20_28d_max;
    }

    public float getResistance_to_cd_20_28d_min() {
        return resistance_to_cd_20_28d_min;
    }

    public void setResistance_to_cd_20_28d_min(float resistance_to_cd_20_28d_min) {
        this.resistance_to_cd_20_28d_min = resistance_to_cd_20_28d_min;
    }

    public float getResistance_to_cd_max_28d_max() {
        return resistance_to_cd_max_28d_max;
    }

    public void setResistance_to_cd_max_28d_max(float resistance_to_cd_max_28d_max) {
        this.resistance_to_cd_max_28d_max = resistance_to_cd_max_28d_max;
    }

    public float getResistance_to_cd_max_28d_min() {
        return resistance_to_cd_max_28d_min;
    }

    public void setResistance_to_cd_max_28d_min(float resistance_to_cd_max_28d_min) {
        this.resistance_to_cd_max_28d_min = resistance_to_cd_max_28d_min;
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

    public float getImpact_resistance_23_max() {
        return impact_resistance_23_max;
    }

    public void setImpact_resistance_23_max(float impact_resistance_23_max) {
        this.impact_resistance_23_max = impact_resistance_23_max;
    }

    public float getImpact_resistance_23_min() {
        return impact_resistance_23_min;
    }

    public void setImpact_resistance_23_min(float impact_resistance_23_min) {
        this.impact_resistance_23_min = impact_resistance_23_min;
    }

    public float getImpact_resistance_m40_max() {
        return impact_resistance_m40_max;
    }

    public void setImpact_resistance_m40_max(float impact_resistance_m40_max) {
        this.impact_resistance_m40_max = impact_resistance_m40_max;
    }

    public float getImpact_resistance_m40_min() {
        return impact_resistance_m40_min;
    }

    public void setImpact_resistance_m40_min(float impact_resistance_m40_min) {
        this.impact_resistance_m40_min = impact_resistance_m40_min;
    }

    public float getIndentation_hardness_23_max() {
        return indentation_hardness_23_max;
    }

    public void setIndentation_hardness_23_max(float indentation_hardness_23_max) {
        this.indentation_hardness_23_max = indentation_hardness_23_max;
    }

    public float getIndentation_hardness_23_min() {
        return indentation_hardness_23_min;
    }

    public void setIndentation_hardness_23_min(float indentation_hardness_23_min) {
        this.indentation_hardness_23_min = indentation_hardness_23_min;
    }

    public float getIndentation_hardness_70_max() {
        return indentation_hardness_70_max;
    }

    public void setIndentation_hardness_70_max(float indentation_hardness_70_max) {
        this.indentation_hardness_70_max = indentation_hardness_70_max;
    }

    public float getIndentation_hardness_70_min() {
        return indentation_hardness_70_min;
    }

    public void setIndentation_hardness_70_min(float indentation_hardness_70_min) {
        this.indentation_hardness_70_min = indentation_hardness_70_min;
    }

    public float getElongation_at_break_max() {
        return elongation_at_break_max;
    }

    public void setElongation_at_break_max(float elongation_at_break_max) {
        this.elongation_at_break_max = elongation_at_break_max;
    }

    public float getElongation_at_break_min() {
        return elongation_at_break_min;
    }

    public void setElongation_at_break_min(float elongation_at_break_min) {
        this.elongation_at_break_min = elongation_at_break_min;
    }

    public float getCoating_resistivity_max() {
        return coating_resistivity_max;
    }

    public void setCoating_resistivity_max(float coating_resistivity_max) {
        this.coating_resistivity_max = coating_resistivity_max;
    }

    public float getCoating_resistivity_min() {
        return coating_resistivity_min;
    }

    public void setCoating_resistivity_min(float coating_resistivity_min) {
        this.coating_resistivity_min = coating_resistivity_min;
    }

    public float getThermal_degradation_max() {
        return thermal_degradation_max;
    }

    public void setThermal_degradation_max(float thermal_degradation_max) {
        this.thermal_degradation_max = thermal_degradation_max;
    }

    public float getThermal_degradation_min() {
        return thermal_degradation_min;
    }

    public void setThermal_degradation_min(float thermal_degradation_min) {
        this.thermal_degradation_min = thermal_degradation_min;
    }

    public Date getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(Date last_update_time) {
        this.last_update_time = last_update_time;
    }
}
