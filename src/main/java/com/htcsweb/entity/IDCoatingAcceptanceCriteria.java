package com.htcsweb.entity;

public class IDCoatingAcceptanceCriteria {

    private  int id; //流水号
    private String coating_acceptance_criteria_no;    //接收标准编号

    private float temp_above_dew_point_max;
    private float temp_above_dew_point_min;

    private float blast_finish_sa25_max;
    private float blast_finish_sa25_min;

    private float relative_humidity_max;
    private float relative_humidity_min;

    private float id_profile_max;
    private float id_profile_min;

    private float dry_film_thickness_max;
    private float dry_film_thickness_min;

    private float cutback_max;
    private float cutback_min;

    private float residual_magnetism_max;
    private float residual_magnetism_min;

    public IDCoatingAcceptanceCriteria() {
    }

    public IDCoatingAcceptanceCriteria(int id, String coating_acceptance_criteria_no, float temp_above_dew_point_max, float temp_above_dew_point_min, float blast_finish_sa25_max, float blast_finish_sa25_min, float relative_humidity_max, float relative_humidity_min, float id_profile_max, float id_profile_min, float dry_film_thickness_max, float dry_film_thickness_min, float cutback_max, float cutback_min, float residual_magnetism_max, float residual_magnetism_min) {
        this.id = id;
        this.coating_acceptance_criteria_no = coating_acceptance_criteria_no;
        this.temp_above_dew_point_max = temp_above_dew_point_max;
        this.temp_above_dew_point_min = temp_above_dew_point_min;
        this.blast_finish_sa25_max = blast_finish_sa25_max;
        this.blast_finish_sa25_min = blast_finish_sa25_min;
        this.relative_humidity_max = relative_humidity_max;
        this.relative_humidity_min = relative_humidity_min;
        this.id_profile_max = id_profile_max;
        this.id_profile_min = id_profile_min;
        this.dry_film_thickness_max = dry_film_thickness_max;
        this.dry_film_thickness_min = dry_film_thickness_min;
        this.cutback_max = cutback_max;
        this.cutback_min = cutback_min;
        this.residual_magnetism_max = residual_magnetism_max;
        this.residual_magnetism_min = residual_magnetism_min;
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

    public float getId_profile_max() {
        return id_profile_max;
    }

    public void setId_profile_max(float id_profile_max) {
        this.id_profile_max = id_profile_max;
    }

    public float getId_profile_min() {
        return id_profile_min;
    }

    public void setId_profile_min(float id_profile_min) {
        this.id_profile_min = id_profile_min;
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
}
