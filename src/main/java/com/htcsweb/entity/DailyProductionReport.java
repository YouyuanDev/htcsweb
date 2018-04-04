package com.htcsweb.entity;

import java.util.Date;

public class DailyProductionReport {

    private  int id; //流水号
    private Date production_date;
    private String project_no;    //项目编号
    private String od_coating_type;  //
    private String od_wt;
    private  int bare_pipe_count;
    private  float bare_pipe_length;
    private  int od_total_coated_count;
    private  int od_total_accepted_count;
    private  int od_aiming_accepted_count;
    private  float od_total_accepted_length;
    private  float od_aiming_total_accepted_length;
    private  int od_repair_pipe_count;
    private  int od_bare_pipe_onhold_count;
    private  int od_bare_pipe_grinded_count;
    private  int od_bare_pipe_cut_count;
    private  int od_coated_pipe_rejected_count;
    private  int od_coated_pipe_strip_count;
    private  int id_total_coated_count;
    private  int id_total_accepted_count;
    private  int id_aiming_accepted_count;
    private  float id_total_accepted_length;
    private  float id_aiming_total_accepted_length;
    private  int id_repair_pipe_count;
    private  int id_bare_pipe_onhold_count;
    private  int id_bare_pipe_grinded_count;
    private  int id_bare_pipe_cut_count;
    private  int id_coated_pipe_rejected_count;
    private  int id_coated_pipe_strip_count;
    private  String od_test_pipe_no_dayshift;
    private  float od_test_pipe_length_before_cut_dayshift;
    private  float od_test_pipe_cutting_length_dayshift;
    private  String od_test_pipe_no_nightshift;
    private  float od_test_pipe_length_before_cut_nightshift;
    private  float od_test_pipe_cutting_length_nightshift;
    private  int od_test_pipe_count;
    private  int rebevel_pipe_count;
    private  int pipe_accepted_count_after_rebevel;
    private  int pipe_delivered_count;
    private  float pipe_delivered_length;

    public DailyProductionReport() {
    }

    public DailyProductionReport(int id, Date production_date, String project_no, String od_coating_type, String od_wt, int bare_pipe_count, float bare_pipe_length, int od_total_coated_count, int od_total_accepted_count, int od_aiming_accepted_count, float od_total_accepted_length, float od_aiming_total_accepted_length, int od_repair_pipe_count, int od_bare_pipe_onhold_count, int od_bare_pipe_grinded_count, int od_bare_pipe_cut_count, int od_coated_pipe_rejected_count, int od_coated_pipe_strip_count, int id_total_coated_count, int id_total_accepted_count, int id_aiming_accepted_count, float id_total_accepted_length, float id_aiming_total_accepted_length, int id_repair_pipe_count, int id_bare_pipe_onhold_count, int id_bare_pipe_grinded_count, int id_bare_pipe_cut_count, int id_coated_pipe_rejected_count, int id_coated_pipe_strip_count, String od_test_pipe_no_dayshift, float od_test_pipe_length_before_cut_dayshift, float od_test_pipe_cutting_length_dayshift, String od_test_pipe_no_nightshift, float od_test_pipe_length_before_cut_nightshift, float od_test_pipe_cutting_length_nightshift, int od_test_pipe_count, int rebevel_pipe_count, int pipe_accepted_count_after_rebevel, int pipe_delivered_count, float pipe_delivered_length) {
        this.id = id;
        this.production_date = production_date;
        this.project_no = project_no;
        this.od_coating_type = od_coating_type;
        this.od_wt = od_wt;
        this.bare_pipe_count = bare_pipe_count;
        this.bare_pipe_length = bare_pipe_length;
        this.od_total_coated_count = od_total_coated_count;
        this.od_total_accepted_count = od_total_accepted_count;
        this.od_aiming_accepted_count = od_aiming_accepted_count;
        this.od_total_accepted_length = od_total_accepted_length;
        this.od_aiming_total_accepted_length = od_aiming_total_accepted_length;
        this.od_repair_pipe_count = od_repair_pipe_count;
        this.od_bare_pipe_onhold_count = od_bare_pipe_onhold_count;
        this.od_bare_pipe_grinded_count = od_bare_pipe_grinded_count;
        this.od_bare_pipe_cut_count = od_bare_pipe_cut_count;
        this.od_coated_pipe_rejected_count = od_coated_pipe_rejected_count;
        this.od_coated_pipe_strip_count = od_coated_pipe_strip_count;
        this.id_total_coated_count = id_total_coated_count;
        this.id_total_accepted_count = id_total_accepted_count;
        this.id_aiming_accepted_count = id_aiming_accepted_count;
        this.id_total_accepted_length = id_total_accepted_length;
        this.id_aiming_total_accepted_length = id_aiming_total_accepted_length;
        this.id_repair_pipe_count = id_repair_pipe_count;
        this.id_bare_pipe_onhold_count = id_bare_pipe_onhold_count;
        this.id_bare_pipe_grinded_count = id_bare_pipe_grinded_count;
        this.id_bare_pipe_cut_count = id_bare_pipe_cut_count;
        this.id_coated_pipe_rejected_count = id_coated_pipe_rejected_count;
        this.id_coated_pipe_strip_count = id_coated_pipe_strip_count;
        this.od_test_pipe_no_dayshift = od_test_pipe_no_dayshift;
        this.od_test_pipe_length_before_cut_dayshift = od_test_pipe_length_before_cut_dayshift;
        this.od_test_pipe_cutting_length_dayshift = od_test_pipe_cutting_length_dayshift;
        this.od_test_pipe_no_nightshift = od_test_pipe_no_nightshift;
        this.od_test_pipe_length_before_cut_nightshift = od_test_pipe_length_before_cut_nightshift;
        this.od_test_pipe_cutting_length_nightshift = od_test_pipe_cutting_length_nightshift;
        this.od_test_pipe_count = od_test_pipe_count;
        this.rebevel_pipe_count = rebevel_pipe_count;
        this.pipe_accepted_count_after_rebevel = pipe_accepted_count_after_rebevel;
        this.pipe_delivered_count = pipe_delivered_count;
        this.pipe_delivered_length = pipe_delivered_length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getProduction_date() {
        return production_date;
    }

    public void setProduction_date(Date production_date) {
        this.production_date = production_date;
    }

    public String getProject_no() {
        return project_no;
    }

    public void setProject_no(String project_no) {
        this.project_no = project_no;
    }

    public String getOd_coating_type() {
        return od_coating_type;
    }

    public void setOd_coating_type(String od_coating_type) {
        this.od_coating_type = od_coating_type;
    }

    public String getOd_wt() {
        return od_wt;
    }

    public void setOd_wt(String od_wt) {
        this.od_wt = od_wt;
    }

    public int getBare_pipe_count() {
        return bare_pipe_count;
    }

    public void setBare_pipe_count(int bare_pipe_count) {
        this.bare_pipe_count = bare_pipe_count;
    }

    public float getBare_pipe_length() {
        return bare_pipe_length;
    }

    public void setBare_pipe_length(float bare_pipe_length) {
        this.bare_pipe_length = bare_pipe_length;
    }

    public int getOd_total_coated_count() {
        return od_total_coated_count;
    }

    public void setOd_total_coated_count(int od_total_coated_count) {
        this.od_total_coated_count = od_total_coated_count;
    }

    public int getOd_total_accepted_count() {
        return od_total_accepted_count;
    }

    public void setOd_total_accepted_count(int od_total_accepted_count) {
        this.od_total_accepted_count = od_total_accepted_count;
    }

    public int getOd_aiming_accepted_count() {
        return od_aiming_accepted_count;
    }

    public void setOd_aiming_accepted_count(int od_aiming_accepted_count) {
        this.od_aiming_accepted_count = od_aiming_accepted_count;
    }

    public float getOd_total_accepted_length() {
        return od_total_accepted_length;
    }

    public void setOd_total_accepted_length(float od_total_accepted_length) {
        this.od_total_accepted_length = od_total_accepted_length;
    }

    public float getOd_aiming_total_accepted_length() {
        return od_aiming_total_accepted_length;
    }

    public void setOd_aiming_total_accepted_length(float od_aiming_total_accepted_length) {
        this.od_aiming_total_accepted_length = od_aiming_total_accepted_length;
    }

    public int getOd_repair_pipe_count() {
        return od_repair_pipe_count;
    }

    public void setOd_repair_pipe_count(int od_repair_pipe_count) {
        this.od_repair_pipe_count = od_repair_pipe_count;
    }

    public int getOd_bare_pipe_onhold_count() {
        return od_bare_pipe_onhold_count;
    }

    public void setOd_bare_pipe_onhold_count(int od_bare_pipe_onhold_count) {
        this.od_bare_pipe_onhold_count = od_bare_pipe_onhold_count;
    }

    public int getOd_bare_pipe_grinded_count() {
        return od_bare_pipe_grinded_count;
    }

    public void setOd_bare_pipe_grinded_count(int od_bare_pipe_grinded_count) {
        this.od_bare_pipe_grinded_count = od_bare_pipe_grinded_count;
    }

    public int getOd_bare_pipe_cut_count() {
        return od_bare_pipe_cut_count;
    }

    public void setOd_bare_pipe_cut_count(int od_bare_pipe_cut_count) {
        this.od_bare_pipe_cut_count = od_bare_pipe_cut_count;
    }

    public int getOd_coated_pipe_rejected_count() {
        return od_coated_pipe_rejected_count;
    }

    public void setOd_coated_pipe_rejected_count(int od_coated_pipe_rejected_count) {
        this.od_coated_pipe_rejected_count = od_coated_pipe_rejected_count;
    }

    public int getOd_coated_pipe_strip_count() {
        return od_coated_pipe_strip_count;
    }

    public void setOd_coated_pipe_strip_count(int od_coated_pipe_strip_count) {
        this.od_coated_pipe_strip_count = od_coated_pipe_strip_count;
    }

    public int getId_total_coated_count() {
        return id_total_coated_count;
    }

    public void setId_total_coated_count(int id_total_coated_count) {
        this.id_total_coated_count = id_total_coated_count;
    }

    public int getId_total_accepted_count() {
        return id_total_accepted_count;
    }

    public void setId_total_accepted_count(int id_total_accepted_count) {
        this.id_total_accepted_count = id_total_accepted_count;
    }

    public int getId_aiming_accepted_count() {
        return id_aiming_accepted_count;
    }

    public void setId_aiming_accepted_count(int id_aiming_accepted_count) {
        this.id_aiming_accepted_count = id_aiming_accepted_count;
    }

    public float getId_total_accepted_length() {
        return id_total_accepted_length;
    }

    public void setId_total_accepted_length(float id_total_accepted_length) {
        this.id_total_accepted_length = id_total_accepted_length;
    }

    public float getId_aiming_total_accepted_length() {
        return id_aiming_total_accepted_length;
    }

    public void setId_aiming_total_accepted_length(float id_aiming_total_accepted_length) {
        this.id_aiming_total_accepted_length = id_aiming_total_accepted_length;
    }

    public int getId_repair_pipe_count() {
        return id_repair_pipe_count;
    }

    public void setId_repair_pipe_count(int id_repair_pipe_count) {
        this.id_repair_pipe_count = id_repair_pipe_count;
    }

    public int getId_bare_pipe_onhold_count() {
        return id_bare_pipe_onhold_count;
    }

    public void setId_bare_pipe_onhold_count(int id_bare_pipe_onhold_count) {
        this.id_bare_pipe_onhold_count = id_bare_pipe_onhold_count;
    }

    public int getId_bare_pipe_grinded_count() {
        return id_bare_pipe_grinded_count;
    }

    public void setId_bare_pipe_grinded_count(int id_bare_pipe_grinded_count) {
        this.id_bare_pipe_grinded_count = id_bare_pipe_grinded_count;
    }

    public int getId_bare_pipe_cut_count() {
        return id_bare_pipe_cut_count;
    }

    public void setId_bare_pipe_cut_count(int id_bare_pipe_cut_count) {
        this.id_bare_pipe_cut_count = id_bare_pipe_cut_count;
    }

    public int getId_coated_pipe_rejected_count() {
        return id_coated_pipe_rejected_count;
    }

    public void setId_coated_pipe_rejected_count(int id_coated_pipe_rejected_count) {
        this.id_coated_pipe_rejected_count = id_coated_pipe_rejected_count;
    }

    public int getId_coated_pipe_strip_count() {
        return id_coated_pipe_strip_count;
    }

    public void setId_coated_pipe_strip_count(int id_coated_pipe_strip_count) {
        this.id_coated_pipe_strip_count = id_coated_pipe_strip_count;
    }

    public String getOd_test_pipe_no_dayshift() {
        return od_test_pipe_no_dayshift;
    }

    public void setOd_test_pipe_no_dayshift(String od_test_pipe_no_dayshift) {
        this.od_test_pipe_no_dayshift = od_test_pipe_no_dayshift;
    }

    public float getOd_test_pipe_length_before_cut_dayshift() {
        return od_test_pipe_length_before_cut_dayshift;
    }

    public void setOd_test_pipe_length_before_cut_dayshift(float od_test_pipe_length_before_cut_dayshift) {
        this.od_test_pipe_length_before_cut_dayshift = od_test_pipe_length_before_cut_dayshift;
    }

    public float getOd_test_pipe_cutting_length_dayshift() {
        return od_test_pipe_cutting_length_dayshift;
    }

    public void setOd_test_pipe_cutting_length_dayshift(float od_test_pipe_cutting_length_dayshift) {
        this.od_test_pipe_cutting_length_dayshift = od_test_pipe_cutting_length_dayshift;
    }

    public String getOd_test_pipe_no_nightshift() {
        return od_test_pipe_no_nightshift;
    }

    public void setOd_test_pipe_no_nightshift(String od_test_pipe_no_nightshift) {
        this.od_test_pipe_no_nightshift = od_test_pipe_no_nightshift;
    }

    public float getOd_test_pipe_length_before_cut_nightshift() {
        return od_test_pipe_length_before_cut_nightshift;
    }

    public void setOd_test_pipe_length_before_cut_nightshift(float od_test_pipe_length_before_cut_nightshift) {
        this.od_test_pipe_length_before_cut_nightshift = od_test_pipe_length_before_cut_nightshift;
    }

    public float getOd_test_pipe_cutting_length_nightshift() {
        return od_test_pipe_cutting_length_nightshift;
    }

    public void setOd_test_pipe_cutting_length_nightshift(float od_test_pipe_cutting_length_nightshift) {
        this.od_test_pipe_cutting_length_nightshift = od_test_pipe_cutting_length_nightshift;
    }

    public int getOd_test_pipe_count() {
        return od_test_pipe_count;
    }

    public void setOd_test_pipe_count(int od_test_pipe_count) {
        this.od_test_pipe_count = od_test_pipe_count;
    }

    public int getRebevel_pipe_count() {
        return rebevel_pipe_count;
    }

    public void setRebevel_pipe_count(int rebevel_pipe_count) {
        this.rebevel_pipe_count = rebevel_pipe_count;
    }

    public int getPipe_accepted_count_after_rebevel() {
        return pipe_accepted_count_after_rebevel;
    }

    public void setPipe_accepted_count_after_rebevel(int pipe_accepted_count_after_rebevel) {
        this.pipe_accepted_count_after_rebevel = pipe_accepted_count_after_rebevel;
    }

    public int getPipe_delivered_count() {
        return pipe_delivered_count;
    }

    public void setPipe_delivered_count(int pipe_delivered_count) {
        this.pipe_delivered_count = pipe_delivered_count;
    }

    public float getPipe_delivered_length() {
        return pipe_delivered_length;
    }

    public void setPipe_delivered_length(float pipe_delivered_length) {
        this.pipe_delivered_length = pipe_delivered_length;
    }
}
