package com.htcsweb.entity;

import java.util.Date;

public class IDCoatingAcceptanceCriteria {

    private  int id; //流水号
    private String coating_acceptance_criteria_no;    //接收标准编号

    private float temp_above_dew_point_max;
    private float temp_above_dew_point_min;

    private float blast_finish_sa25_max;
    private float blast_finish_sa25_min;

    private float relative_humidity_max;
    private float relative_humidity_min;

    private float profile_max;
    private float profile_min;

    private float dry_film_thickness_max;
    private float dry_film_thickness_min;

    private float cutback_max;
    private float cutback_min;

    private float residual_magnetism_max;
    private float residual_magnetism_min;

    private Date last_update_time;

    private float wet_film_thickness_max;
    private float wet_film_thickness_min;

    private float salt_contamination_before_blast_max;
    private float salt_contamination_before_blast_min;

    private float salt_contamination_after_blasting_max;
    private float salt_contamination_after_blasting_min;

    private int surface_dust_rating_max;
    private int surface_dust_rating_min;

    private int holiday_max;
    private int holiday_min;

    private float roughness_max;
    private float roughness_min;

    private float pipe_temp_max;
    private float pipe_temp_min;

    private int repair_max;
    private int repair_min;

    private float holiday_tester_voltage_max;
    private float holiday_tester_voltage_min;

    private float strip_temp_max;
    private float strip_temp_min;

    private String stencil_content;

    private float abrasive_conductivity_max;
    private float abrasive_conductivity_min;


    public IDCoatingAcceptanceCriteria() {
    }

    public IDCoatingAcceptanceCriteria(int id, String coating_acceptance_criteria_no, float temp_above_dew_point_max, float temp_above_dew_point_min, float blast_finish_sa25_max, float blast_finish_sa25_min, float relative_humidity_max, float relative_humidity_min, float profile_max, float profile_min, float dry_film_thickness_max, float dry_film_thickness_min, float cutback_max, float cutback_min, float residual_magnetism_max, float residual_magnetism_min, Date last_update_time, float wet_film_thickness_max, float wet_film_thickness_min, float salt_contamination_before_blast_max, float salt_contamination_before_blast_min, float salt_contamination_after_blasting_max, float salt_contamination_after_blasting_min, int surface_dust_rating_max, int surface_dust_rating_min, int holiday_max, int holiday_min, float roughness_max, float roughness_min, float pipe_temp_max, float pipe_temp_min, int repair_max, int repair_min, float holiday_tester_voltage_max, float holiday_tester_voltage_min, float strip_temp_max, float strip_temp_min, String stencil_content, float abrasive_conductivity_max, float abrasive_conductivity_min) {
        this.id = id;
        this.coating_acceptance_criteria_no = coating_acceptance_criteria_no;
        this.temp_above_dew_point_max = temp_above_dew_point_max;
        this.temp_above_dew_point_min = temp_above_dew_point_min;
        this.blast_finish_sa25_max = blast_finish_sa25_max;
        this.blast_finish_sa25_min = blast_finish_sa25_min;
        this.relative_humidity_max = relative_humidity_max;
        this.relative_humidity_min = relative_humidity_min;
        this.profile_max = profile_max;
        this.profile_min = profile_min;
        this.dry_film_thickness_max = dry_film_thickness_max;
        this.dry_film_thickness_min = dry_film_thickness_min;
        this.cutback_max = cutback_max;
        this.cutback_min = cutback_min;
        this.residual_magnetism_max = residual_magnetism_max;
        this.residual_magnetism_min = residual_magnetism_min;
        this.last_update_time = last_update_time;
        this.wet_film_thickness_max = wet_film_thickness_max;
        this.wet_film_thickness_min = wet_film_thickness_min;
        this.salt_contamination_before_blast_max = salt_contamination_before_blast_max;
        this.salt_contamination_before_blast_min = salt_contamination_before_blast_min;
        this.salt_contamination_after_blasting_max = salt_contamination_after_blasting_max;
        this.salt_contamination_after_blasting_min = salt_contamination_after_blasting_min;
        this.surface_dust_rating_max = surface_dust_rating_max;
        this.surface_dust_rating_min = surface_dust_rating_min;
        this.holiday_max = holiday_max;
        this.holiday_min = holiday_min;
        this.roughness_max = roughness_max;
        this.roughness_min = roughness_min;
        this.pipe_temp_max = pipe_temp_max;
        this.pipe_temp_min = pipe_temp_min;
        this.repair_max = repair_max;
        this.repair_min = repair_min;
        this.holiday_tester_voltage_max = holiday_tester_voltage_max;
        this.holiday_tester_voltage_min = holiday_tester_voltage_min;
        this.strip_temp_max = strip_temp_max;
        this.strip_temp_min = strip_temp_min;
        this.stencil_content = stencil_content;
        this.abrasive_conductivity_max = abrasive_conductivity_max;
        this.abrasive_conductivity_min = abrasive_conductivity_min;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoating_acceptance_criteria_no() {
        return coating_acceptance_criteria_no;
    }

    public void setCoating_acceptance_criteria_no(String coating_acceptance_criteria_no) {
        this.coating_acceptance_criteria_no = coating_acceptance_criteria_no;
    }

    public float getTemp_above_dew_point_max() {
        return temp_above_dew_point_max;
    }

    public void setTemp_above_dew_point_max(float temp_above_dew_point_max) {
        this.temp_above_dew_point_max = temp_above_dew_point_max;
    }

    public float getTemp_above_dew_point_min() {
        return temp_above_dew_point_min;
    }

    public void setTemp_above_dew_point_min(float temp_above_dew_point_min) {
        this.temp_above_dew_point_min = temp_above_dew_point_min;
    }

    public float getBlast_finish_sa25_max() {
        return blast_finish_sa25_max;
    }

    public void setBlast_finish_sa25_max(float blast_finish_sa25_max) {
        this.blast_finish_sa25_max = blast_finish_sa25_max;
    }

    public float getBlast_finish_sa25_min() {
        return blast_finish_sa25_min;
    }

    public void setBlast_finish_sa25_min(float blast_finish_sa25_min) {
        this.blast_finish_sa25_min = blast_finish_sa25_min;
    }

    public float getRelative_humidity_max() {
        return relative_humidity_max;
    }

    public void setRelative_humidity_max(float relative_humidity_max) {
        this.relative_humidity_max = relative_humidity_max;
    }

    public float getRelative_humidity_min() {
        return relative_humidity_min;
    }

    public void setRelative_humidity_min(float relative_humidity_min) {
        this.relative_humidity_min = relative_humidity_min;
    }

    public float getProfile_max() {
        return profile_max;
    }

    public void setProfile_max(float profile_max) {
        this.profile_max = profile_max;
    }

    public float getProfile_min() {
        return profile_min;
    }

    public void setProfile_min(float profile_min) {
        this.profile_min = profile_min;
    }

    public float getDry_film_thickness_max() {
        return dry_film_thickness_max;
    }

    public void setDry_film_thickness_max(float dry_film_thickness_max) {
        this.dry_film_thickness_max = dry_film_thickness_max;
    }

    public float getDry_film_thickness_min() {
        return dry_film_thickness_min;
    }

    public void setDry_film_thickness_min(float dry_film_thickness_min) {
        this.dry_film_thickness_min = dry_film_thickness_min;
    }

    public float getCutback_max() {
        return cutback_max;
    }

    public void setCutback_max(float cutback_max) {
        this.cutback_max = cutback_max;
    }

    public float getCutback_min() {
        return cutback_min;
    }

    public void setCutback_min(float cutback_min) {
        this.cutback_min = cutback_min;
    }

    public float getResidual_magnetism_max() {
        return residual_magnetism_max;
    }

    public void setResidual_magnetism_max(float residual_magnetism_max) {
        this.residual_magnetism_max = residual_magnetism_max;
    }

    public float getResidual_magnetism_min() {
        return residual_magnetism_min;
    }

    public void setResidual_magnetism_min(float residual_magnetism_min) {
        this.residual_magnetism_min = residual_magnetism_min;
    }

    public Date getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(Date last_update_time) {
        this.last_update_time = last_update_time;
    }

    public float getWet_film_thickness_max() {
        return wet_film_thickness_max;
    }

    public void setWet_film_thickness_max(float wet_film_thickness_max) {
        this.wet_film_thickness_max = wet_film_thickness_max;
    }

    public float getWet_film_thickness_min() {
        return wet_film_thickness_min;
    }

    public void setWet_film_thickness_min(float wet_film_thickness_min) {
        this.wet_film_thickness_min = wet_film_thickness_min;
    }

    public float getSalt_contamination_before_blast_max() {
        return salt_contamination_before_blast_max;
    }

    public void setSalt_contamination_before_blast_max(float salt_contamination_before_blast_max) {
        this.salt_contamination_before_blast_max = salt_contamination_before_blast_max;
    }

    public float getSalt_contamination_before_blast_min() {
        return salt_contamination_before_blast_min;
    }

    public void setSalt_contamination_before_blast_min(float salt_contamination_before_blast_min) {
        this.salt_contamination_before_blast_min = salt_contamination_before_blast_min;
    }

    public float getSalt_contamination_after_blasting_max() {
        return salt_contamination_after_blasting_max;
    }

    public void setSalt_contamination_after_blasting_max(float salt_contamination_after_blasting_max) {
        this.salt_contamination_after_blasting_max = salt_contamination_after_blasting_max;
    }

    public float getSalt_contamination_after_blasting_min() {
        return salt_contamination_after_blasting_min;
    }

    public void setSalt_contamination_after_blasting_min(float salt_contamination_after_blasting_min) {
        this.salt_contamination_after_blasting_min = salt_contamination_after_blasting_min;
    }

    public int getSurface_dust_rating_max() {
        return surface_dust_rating_max;
    }

    public void setSurface_dust_rating_max(int surface_dust_rating_max) {
        this.surface_dust_rating_max = surface_dust_rating_max;
    }

    public int getSurface_dust_rating_min() {
        return surface_dust_rating_min;
    }

    public void setSurface_dust_rating_min(int surface_dust_rating_min) {
        this.surface_dust_rating_min = surface_dust_rating_min;
    }

    public int getHoliday_max() {
        return holiday_max;
    }

    public void setHoliday_max(int holiday_max) {
        this.holiday_max = holiday_max;
    }

    public int getHoliday_min() {
        return holiday_min;
    }

    public void setHoliday_min(int holiday_min) {
        this.holiday_min = holiday_min;
    }

    public float getRoughness_max() {
        return roughness_max;
    }

    public void setRoughness_max(float roughness_max) {
        this.roughness_max = roughness_max;
    }

    public float getRoughness_min() {
        return roughness_min;
    }

    public void setRoughness_min(float roughness_min) {
        this.roughness_min = roughness_min;
    }

    public float getPipe_temp_max() {
        return pipe_temp_max;
    }

    public void setPipe_temp_max(float pipe_temp_max) {
        this.pipe_temp_max = pipe_temp_max;
    }

    public float getPipe_temp_min() {
        return pipe_temp_min;
    }

    public void setPipe_temp_min(float pipe_temp_min) {
        this.pipe_temp_min = pipe_temp_min;
    }

    public int getRepair_max() {
        return repair_max;
    }

    public void setRepair_max(int repair_max) {
        this.repair_max = repair_max;
    }

    public int getRepair_min() {
        return repair_min;
    }

    public void setRepair_min(int repair_min) {
        this.repair_min = repair_min;
    }

    public float getHoliday_tester_voltage_max() {
        return holiday_tester_voltage_max;
    }

    public void setHoliday_tester_voltage_max(float holiday_tester_voltage_max) {
        this.holiday_tester_voltage_max = holiday_tester_voltage_max;
    }

    public float getHoliday_tester_voltage_min() {
        return holiday_tester_voltage_min;
    }

    public void setHoliday_tester_voltage_min(float holiday_tester_voltage_min) {
        this.holiday_tester_voltage_min = holiday_tester_voltage_min;
    }

    public float getStrip_temp_max() {
        return strip_temp_max;
    }

    public void setStrip_temp_max(float strip_temp_max) {
        this.strip_temp_max = strip_temp_max;
    }

    public float getStrip_temp_min() {
        return strip_temp_min;
    }

    public void setStrip_temp_min(float strip_temp_min) {
        this.strip_temp_min = strip_temp_min;
    }

    public String getStencil_content() {
        return stencil_content;
    }

    public void setStencil_content(String stencil_content) {
        this.stencil_content = stencil_content;
    }

    public float getAbrasive_conductivity_max() {
        return abrasive_conductivity_max;
    }

    public void setAbrasive_conductivity_max(float abrasive_conductivity_max) {
        this.abrasive_conductivity_max = abrasive_conductivity_max;
    }

    public float getAbrasive_conductivity_min() {
        return abrasive_conductivity_min;
    }

    public void setAbrasive_conductivity_min(float abrasive_conductivity_min) {
        this.abrasive_conductivity_min = abrasive_conductivity_min;
    }
}
