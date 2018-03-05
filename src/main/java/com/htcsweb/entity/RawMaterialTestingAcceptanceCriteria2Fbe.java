package com.htcsweb.entity;


import java.util.Date;

public class RawMaterialTestingAcceptanceCriteria2Fbe {

    private  int id; //流水号
    private String raw_material_testing_acceptance_criteria_no;
    private float density_max;
    private float density_min;
    private float particle_size_32um_max;
    private float particle_size_32um_min;
    private float particle_size_150um_max;
    private float particle_size_150um_min;
    private float dsc_tgi_max;
    private float dsc_tgi_min;
    private float dsc_tgf_max;
    private float dsc_tgf_min;
    private float dsc_delta_h_max;
    private float dsc_delta_h_min;
    private float gel_time_lt_20s_max;
    private float gel_time_lt_20s_min;
    private float gel_time_gt_20s_max;
    private float gel_time_gt_20s_min;

    private float volatile_max;
    private float volatile_min;
    private float foaming_cross_sectional_max;
    private float foaming_cross_sectional_min;
    private float foaming_interfacial_max;
    private float foaming_interfacial_min;
    private float hot_water_max;
    private float hot_water_min;
    private float cd_65_24h_max;
    private float cd_65_24h_min;

    private Date last_update_time;


    public RawMaterialTestingAcceptanceCriteria2Fbe() {
    }

    public RawMaterialTestingAcceptanceCriteria2Fbe(int id, String raw_material_testing_acceptance_criteria_no, float density_max, float density_min, float particle_size_32um_max, float particle_size_32um_min, float particle_size_150um_max, float particle_size_150um_min, float dsc_tgi_max, float dsc_tgi_min, float dsc_tgf_max, float dsc_tgf_min, float dsc_delta_h_max, float dsc_delta_h_min, float gel_time_lt_20s_max, float gel_time_lt_20s_min, float gel_time_gt_20s_max, float gel_time_gt_20s_min, float volatile_max, float volatile_min, float foaming_cross_sectional_max, float foaming_cross_sectional_min, float foaming_interfacial_max, float foaming_interfacial_min, float hot_water_max, float hot_water_min, float cd_65_24h_max, float cd_65_24h_min, Date last_update_time) {
        this.id = id;
        this.raw_material_testing_acceptance_criteria_no = raw_material_testing_acceptance_criteria_no;
        this.density_max = density_max;
        this.density_min = density_min;
        this.particle_size_32um_max = particle_size_32um_max;
        this.particle_size_32um_min = particle_size_32um_min;
        this.particle_size_150um_max = particle_size_150um_max;
        this.particle_size_150um_min = particle_size_150um_min;
        this.dsc_tgi_max = dsc_tgi_max;
        this.dsc_tgi_min = dsc_tgi_min;
        this.dsc_tgf_max = dsc_tgf_max;
        this.dsc_tgf_min = dsc_tgf_min;
        this.dsc_delta_h_max = dsc_delta_h_max;
        this.dsc_delta_h_min = dsc_delta_h_min;
        this.gel_time_lt_20s_max = gel_time_lt_20s_max;
        this.gel_time_lt_20s_min = gel_time_lt_20s_min;
        this.gel_time_gt_20s_max = gel_time_gt_20s_max;
        this.gel_time_gt_20s_min = gel_time_gt_20s_min;
        this.volatile_max = volatile_max;
        this.volatile_min = volatile_min;
        this.foaming_cross_sectional_max = foaming_cross_sectional_max;
        this.foaming_cross_sectional_min = foaming_cross_sectional_min;
        this.foaming_interfacial_max = foaming_interfacial_max;
        this.foaming_interfacial_min = foaming_interfacial_min;
        this.hot_water_max = hot_water_max;
        this.hot_water_min = hot_water_min;
        this.cd_65_24h_max = cd_65_24h_max;
        this.cd_65_24h_min = cd_65_24h_min;
        this.last_update_time = last_update_time;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRaw_material_testing_acceptance_criteria_no() {
        return raw_material_testing_acceptance_criteria_no;
    }

    public void setRaw_material_testing_acceptance_criteria_no(String raw_material_testing_acceptance_criteria_no) {
        this.raw_material_testing_acceptance_criteria_no = raw_material_testing_acceptance_criteria_no;
    }

    public float getDensity_max() {
        return density_max;
    }

    public void setDensity_max(float density_max) {
        this.density_max = density_max;
    }

    public float getDensity_min() {
        return density_min;
    }

    public void setDensity_min(float density_min) {
        this.density_min = density_min;
    }

    public float getParticle_size_32um_max() {
        return particle_size_32um_max;
    }

    public void setParticle_size_32um_max(float particle_size_32um_max) {
        this.particle_size_32um_max = particle_size_32um_max;
    }

    public float getParticle_size_32um_min() {
        return particle_size_32um_min;
    }

    public void setParticle_size_32um_min(float particle_size_32um_min) {
        this.particle_size_32um_min = particle_size_32um_min;
    }

    public float getParticle_size_150um_max() {
        return particle_size_150um_max;
    }

    public void setParticle_size_150um_max(float particle_size_150um_max) {
        this.particle_size_150um_max = particle_size_150um_max;
    }

    public float getParticle_size_150um_min() {
        return particle_size_150um_min;
    }

    public void setParticle_size_150um_min(float particle_size_150um_min) {
        this.particle_size_150um_min = particle_size_150um_min;
    }

    public float getDsc_tgi_max() {
        return dsc_tgi_max;
    }

    public void setDsc_tgi_max(float dsc_tgi_max) {
        this.dsc_tgi_max = dsc_tgi_max;
    }

    public float getDsc_tgi_min() {
        return dsc_tgi_min;
    }

    public void setDsc_tgi_min(float dsc_tgi_min) {
        this.dsc_tgi_min = dsc_tgi_min;
    }

    public float getDsc_tgf_max() {
        return dsc_tgf_max;
    }

    public void setDsc_tgf_max(float dsc_tgf_max) {
        this.dsc_tgf_max = dsc_tgf_max;
    }

    public float getDsc_tgf_min() {
        return dsc_tgf_min;
    }

    public void setDsc_tgf_min(float dsc_tgf_min) {
        this.dsc_tgf_min = dsc_tgf_min;
    }

    public float getDsc_delta_h_max() {
        return dsc_delta_h_max;
    }

    public void setDsc_delta_h_max(float dsc_delta_h_max) {
        this.dsc_delta_h_max = dsc_delta_h_max;
    }

    public float getDsc_delta_h_min() {
        return dsc_delta_h_min;
    }

    public void setDsc_delta_h_min(float dsc_delta_h_min) {
        this.dsc_delta_h_min = dsc_delta_h_min;
    }

    public float getGel_time_lt_20s_max() {
        return gel_time_lt_20s_max;
    }

    public void setGel_time_lt_20s_max(float gel_time_lt_20s_max) {
        this.gel_time_lt_20s_max = gel_time_lt_20s_max;
    }

    public float getGel_time_lt_20s_min() {
        return gel_time_lt_20s_min;
    }

    public void setGel_time_lt_20s_min(float gel_time_lt_20s_min) {
        this.gel_time_lt_20s_min = gel_time_lt_20s_min;
    }

    public float getGel_time_gt_20s_max() {
        return gel_time_gt_20s_max;
    }

    public void setGel_time_gt_20s_max(float gel_time_gt_20s_max) {
        this.gel_time_gt_20s_max = gel_time_gt_20s_max;
    }

    public float getGel_time_gt_20s_min() {
        return gel_time_gt_20s_min;
    }

    public void setGel_time_gt_20s_min(float gel_time_gt_20s_min) {
        this.gel_time_gt_20s_min = gel_time_gt_20s_min;
    }

    public float getVolatile_max() {
        return volatile_max;
    }

    public void setVolatile_max(float volatile_max) {
        this.volatile_max = volatile_max;
    }

    public float getVolatile_min() {
        return volatile_min;
    }

    public void setVolatile_min(float volatile_min) {
        this.volatile_min = volatile_min;
    }

    public float getFoaming_cross_sectional_max() {
        return foaming_cross_sectional_max;
    }

    public void setFoaming_cross_sectional_max(float foaming_cross_sectional_max) {
        this.foaming_cross_sectional_max = foaming_cross_sectional_max;
    }

    public float getFoaming_cross_sectional_min() {
        return foaming_cross_sectional_min;
    }

    public void setFoaming_cross_sectional_min(float foaming_cross_sectional_min) {
        this.foaming_cross_sectional_min = foaming_cross_sectional_min;
    }

    public float getFoaming_interfacial_max() {
        return foaming_interfacial_max;
    }

    public void setFoaming_interfacial_max(float foaming_interfacial_max) {
        this.foaming_interfacial_max = foaming_interfacial_max;
    }

    public float getFoaming_interfacial_min() {
        return foaming_interfacial_min;
    }

    public void setFoaming_interfacial_min(float foaming_interfacial_min) {
        this.foaming_interfacial_min = foaming_interfacial_min;
    }

    public float getHot_water_max() {
        return hot_water_max;
    }

    public void setHot_water_max(float hot_water_max) {
        this.hot_water_max = hot_water_max;
    }

    public float getHot_water_min() {
        return hot_water_min;
    }

    public void setHot_water_min(float hot_water_min) {
        this.hot_water_min = hot_water_min;
    }

    public float getCd_65_24h_max() {
        return cd_65_24h_max;
    }

    public void setCd_65_24h_max(float cd_65_24h_max) {
        this.cd_65_24h_max = cd_65_24h_max;
    }

    public float getCd_65_24h_min() {
        return cd_65_24h_min;
    }

    public void setCd_65_24h_min(float cd_65_24h_min) {
        this.cd_65_24h_min = cd_65_24h_min;
    }

    public Date getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(Date last_update_time) {
        this.last_update_time = last_update_time;
    }
}
