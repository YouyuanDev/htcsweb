package com.htcsweb.entity;

import java.util.Date;

public class PipeBodyAcceptanceCriteria {

    private  int id; //流水号
    private String pipe_body_acceptance_criteria_no;
    private float pipe_length_max;
    private float pipe_length_min;
    private float pipe_thickness_tolerance_max;
    private float pipe_thickness_tolerance_min;
    private float pipe_bevel_angle_max;
    private float pipe_bevel_angle_min;
    private float pipe_rootface_max;
    private float pipe_rootface_min;
    private float pipe_squareness_max;
    private float pipe_squareness_min;
    private float pipe_end_ovality_factor_max;
    private float pipe_end_ovality_factor_min;
    private float pipe_straightness_tolerance_max;
    private float pipe_straightness_tolerance_min;
    private Date last_update_time;

    public PipeBodyAcceptanceCriteria() {
    }

    public PipeBodyAcceptanceCriteria(int id, String pipe_body_acceptance_criteria_no, float pipe_length_max, float pipe_length_min, float pipe_thickness_tolerance_max, float pipe_thickness_tolerance_min, float pipe_bevel_angle_max, float pipe_bevel_angle_min, float pipe_rootface_max, float pipe_rootface_min, float pipe_squareness_max, float pipe_squareness_min, float pipe_end_ovality_factor_max, float pipe_end_ovality_factor_min, float pipe_straightness_tolerance_max, float pipe_straightness_tolerance_min, Date last_update_time) {
        this.id = id;
        this.pipe_body_acceptance_criteria_no = pipe_body_acceptance_criteria_no;
        this.pipe_length_max = pipe_length_max;
        this.pipe_length_min = pipe_length_min;
        this.pipe_thickness_tolerance_max = pipe_thickness_tolerance_max;
        this.pipe_thickness_tolerance_min = pipe_thickness_tolerance_min;
        this.pipe_bevel_angle_max = pipe_bevel_angle_max;
        this.pipe_bevel_angle_min = pipe_bevel_angle_min;
        this.pipe_rootface_max = pipe_rootface_max;
        this.pipe_rootface_min = pipe_rootface_min;
        this.pipe_squareness_max = pipe_squareness_max;
        this.pipe_squareness_min = pipe_squareness_min;
        this.pipe_end_ovality_factor_max = pipe_end_ovality_factor_max;
        this.pipe_end_ovality_factor_min = pipe_end_ovality_factor_min;
        this.pipe_straightness_tolerance_max = pipe_straightness_tolerance_max;
        this.pipe_straightness_tolerance_min = pipe_straightness_tolerance_min;
        this.last_update_time = last_update_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPipe_body_acceptance_criteria_no() {
        return pipe_body_acceptance_criteria_no;
    }

    public void setPipe_body_acceptance_criteria_no(String pipe_body_acceptance_criteria_no) {
        this.pipe_body_acceptance_criteria_no = pipe_body_acceptance_criteria_no;
    }

    public float getPipe_length_max() {
        return pipe_length_max;
    }

    public void setPipe_length_max(float pipe_length_max) {
        this.pipe_length_max = pipe_length_max;
    }

    public float getPipe_length_min() {
        return pipe_length_min;
    }

    public void setPipe_length_min(float pipe_length_min) {
        this.pipe_length_min = pipe_length_min;
    }

    public float getPipe_thickness_tolerance_max() {
        return pipe_thickness_tolerance_max;
    }

    public void setPipe_thickness_tolerance_max(float pipe_thickness_tolerance_max) {
        this.pipe_thickness_tolerance_max = pipe_thickness_tolerance_max;
    }

    public float getPipe_thickness_tolerance_min() {
        return pipe_thickness_tolerance_min;
    }

    public void setPipe_thickness_tolerance_min(float pipe_thickness_tolerance_min) {
        this.pipe_thickness_tolerance_min = pipe_thickness_tolerance_min;
    }

    public float getPipe_bevel_angle_max() {
        return pipe_bevel_angle_max;
    }

    public void setPipe_bevel_angle_max(float pipe_bevel_angle_max) {
        this.pipe_bevel_angle_max = pipe_bevel_angle_max;
    }

    public float getPipe_bevel_angle_min() {
        return pipe_bevel_angle_min;
    }

    public void setPipe_bevel_angle_min(float pipe_bevel_angle_min) {
        this.pipe_bevel_angle_min = pipe_bevel_angle_min;
    }

    public float getPipe_rootface_max() {
        return pipe_rootface_max;
    }

    public void setPipe_rootface_max(float pipe_rootface_max) {
        this.pipe_rootface_max = pipe_rootface_max;
    }

    public float getPipe_rootface_min() {
        return pipe_rootface_min;
    }

    public void setPipe_rootface_min(float pipe_rootface_min) {
        this.pipe_rootface_min = pipe_rootface_min;
    }

    public float getPipe_squareness_max() {
        return pipe_squareness_max;
    }

    public void setPipe_squareness_max(float pipe_squareness_max) {
        this.pipe_squareness_max = pipe_squareness_max;
    }

    public float getPipe_squareness_min() {
        return pipe_squareness_min;
    }

    public void setPipe_squareness_min(float pipe_squareness_min) {
        this.pipe_squareness_min = pipe_squareness_min;
    }

    public float getPipe_end_ovality_factor_max() {
        return pipe_end_ovality_factor_max;
    }

    public void setPipe_end_ovality_factor_max(float pipe_end_ovality_factor_max) {
        this.pipe_end_ovality_factor_max = pipe_end_ovality_factor_max;
    }

    public float getPipe_end_ovality_factor_min() {
        return pipe_end_ovality_factor_min;
    }

    public void setPipe_end_ovality_factor_min(float pipe_end_ovality_factor_min) {
        this.pipe_end_ovality_factor_min = pipe_end_ovality_factor_min;
    }

    public float getPipe_straightness_tolerance_max() {
        return pipe_straightness_tolerance_max;
    }

    public void setPipe_straightness_tolerance_max(float pipe_straightness_tolerance_max) {
        this.pipe_straightness_tolerance_max = pipe_straightness_tolerance_max;
    }

    public float getPipe_straightness_tolerance_min() {
        return pipe_straightness_tolerance_min;
    }

    public void setPipe_straightness_tolerance_min(float pipe_straightness_tolerance_min) {
        this.pipe_straightness_tolerance_min = pipe_straightness_tolerance_min;
    }

    public Date getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(Date last_update_time) {
        this.last_update_time = last_update_time;
    }
}
