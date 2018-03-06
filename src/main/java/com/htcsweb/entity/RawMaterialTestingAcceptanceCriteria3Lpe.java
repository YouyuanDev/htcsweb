package com.htcsweb.entity;

import java.util.Date;

public class RawMaterialTestingAcceptanceCriteria3Lpe {

    private  int id; //流水号
    private String raw_material_testing_acceptance_criteria_no;

    private float epoxy_cure_time_max;
    private float epoxy_cure_time_min;
    private float epoxy_gel_time_max;
    private float epoxy_gel_time_min;

    private float epoxy_moisture_content_max;
    private float epoxy_moisture_content_min;

    private float  epoxy_particle_size_150um_max;
    private float  epoxy_particle_size_150um_min;
    private float  epoxy_particle_size_250um_max;
    private float  epoxy_particle_size_250um_min;

    private float  adhesion_flow_rate_max;
    private float  adhesion_flow_rate_min;

    private float polyethylene_flow_rate_max;
    private float polyethylene_flow_rate_min;

    private Date last_update_time;


    public RawMaterialTestingAcceptanceCriteria3Lpe() {
    }

    public RawMaterialTestingAcceptanceCriteria3Lpe(int id, String raw_material_testing_acceptance_criteria_no, float epoxy_cure_time_max, float epoxy_cure_time_min, float epoxy_gel_time_max, float epoxy_gel_time_min, float epoxy_moisture_content_max, float epoxy_moisture_content_min, float epoxy_particle_size_150um_max, float epoxy_particle_size_150um_min, float epoxy_particle_size_250um_max, float epoxy_particle_size_250um_min, float adhesion_flow_rate_max, float adhesion_flow_rate_min, float polyethylene_flow_rate_max, float polyethylene_flow_rate_min, Date last_update_time) {
        this.id = id;
        this.raw_material_testing_acceptance_criteria_no = raw_material_testing_acceptance_criteria_no;
        this.epoxy_cure_time_max = epoxy_cure_time_max;
        this.epoxy_cure_time_min = epoxy_cure_time_min;
        this.epoxy_gel_time_max = epoxy_gel_time_max;
        this.epoxy_gel_time_min = epoxy_gel_time_min;
        this.epoxy_moisture_content_max = epoxy_moisture_content_max;
        this.epoxy_moisture_content_min = epoxy_moisture_content_min;
        this.epoxy_particle_size_150um_max = epoxy_particle_size_150um_max;
        this.epoxy_particle_size_150um_min = epoxy_particle_size_150um_min;
        this.epoxy_particle_size_250um_max = epoxy_particle_size_250um_max;
        this.epoxy_particle_size_250um_min = epoxy_particle_size_250um_min;
        this.adhesion_flow_rate_max = adhesion_flow_rate_max;
        this.adhesion_flow_rate_min = adhesion_flow_rate_min;
        this.polyethylene_flow_rate_max = polyethylene_flow_rate_max;
        this.polyethylene_flow_rate_min = polyethylene_flow_rate_min;
        this.last_update_time = last_update_time;
    }

    public float getEpoxy_particle_size_150um_max() {
        return epoxy_particle_size_150um_max;
    }

    public void setEpoxy_particle_size_150um_max(float epoxy_particle_size_150um_max) {
        this.epoxy_particle_size_150um_max = epoxy_particle_size_150um_max;
    }

    public float getEpoxy_particle_size_150um_min() {
        return epoxy_particle_size_150um_min;
    }

    public void setEpoxy_particle_size_150um_min(float epoxy_particle_size_150um_min) {
        this.epoxy_particle_size_150um_min = epoxy_particle_size_150um_min;
    }

    public float getEpoxy_particle_size_250um_max() {
        return epoxy_particle_size_250um_max;
    }

    public void setEpoxy_particle_size_250um_max(float epoxy_particle_size_250um_max) {
        this.epoxy_particle_size_250um_max = epoxy_particle_size_250um_max;
    }

    public float getEpoxy_particle_size_250um_min() {
        return epoxy_particle_size_250um_min;
    }

    public void setEpoxy_particle_size_250um_min(float epoxy_particle_size_250um_min) {
        this.epoxy_particle_size_250um_min = epoxy_particle_size_250um_min;
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

    public float getEpoxy_cure_time_max() {
        return epoxy_cure_time_max;
    }

    public void setEpoxy_cure_time_max(float epoxy_cure_time_max) {
        this.epoxy_cure_time_max = epoxy_cure_time_max;
    }

    public float getEpoxy_cure_time_min() {
        return epoxy_cure_time_min;
    }

    public void setEpoxy_cure_time_min(float epoxy_cure_time_min) {
        this.epoxy_cure_time_min = epoxy_cure_time_min;
    }

    public float getEpoxy_gel_time_max() {
        return epoxy_gel_time_max;
    }

    public void setEpoxy_gel_time_max(float epoxy_gel_time_max) {
        this.epoxy_gel_time_max = epoxy_gel_time_max;
    }

    public float getEpoxy_gel_time_min() {
        return epoxy_gel_time_min;
    }

    public void setEpoxy_gel_time_min(float epoxy_gel_time_min) {
        this.epoxy_gel_time_min = epoxy_gel_time_min;
    }

    public float getEpoxy_moisture_content_max() {
        return epoxy_moisture_content_max;
    }

    public void setEpoxy_moisture_content_max(float epoxy_moisture_content_max) {
        this.epoxy_moisture_content_max = epoxy_moisture_content_max;
    }

    public float getEpoxy_moisture_content_min() {
        return epoxy_moisture_content_min;
    }

    public void setEpoxy_moisture_content_min(float epoxy_moisture_content_min) {
        this.epoxy_moisture_content_min = epoxy_moisture_content_min;
    }



    public float getAdhesion_flow_rate_max() {
        return adhesion_flow_rate_max;
    }

    public void setAdhesion_flow_rate_max(float adhesion_flow_rate_max) {
        this.adhesion_flow_rate_max = adhesion_flow_rate_max;
    }

    public float getAdhesion_flow_rate_min() {
        return adhesion_flow_rate_min;
    }

    public void setAdhesion_flow_rate_min(float adhesion_flow_rate_min) {
        this.adhesion_flow_rate_min = adhesion_flow_rate_min;
    }

    public float getPolyethylene_flow_rate_max() {
        return polyethylene_flow_rate_max;
    }

    public void setPolyethylene_flow_rate_max(float polyethylene_flow_rate_max) {
        this.polyethylene_flow_rate_max = polyethylene_flow_rate_max;
    }

    public float getPolyethylene_flow_rate_min() {
        return polyethylene_flow_rate_min;
    }

    public void setPolyethylene_flow_rate_min(float polyethylene_flow_rate_min) {
        this.polyethylene_flow_rate_min = polyethylene_flow_rate_min;
    }

    public Date getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(Date last_update_time) {
        this.last_update_time = last_update_time;
    }
}
