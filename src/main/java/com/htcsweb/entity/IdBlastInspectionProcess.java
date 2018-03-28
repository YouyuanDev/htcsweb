package com.htcsweb.entity;

import java.util.Date;

public class IdBlastInspectionProcess {
   private int id;
   private String pipe_no;
   private Date operation_time;
   private String operator_no;
   private float relative_humidity;
   private float dew_point;
   private float pipe_temp;
   private String surface_condition;
   private float blast_time;
   private String blast_finish_sa25;
   private float profile;
   private String upload_files;
   private String remark;
   private  String result;
   private  String mill_no;

   private float air_temp;
   private int surface_dust_rating;
   private float salt_contamination_after_blasting;
   private float elapsed_time;


   public IdBlastInspectionProcess() {

   }

    public IdBlastInspectionProcess(int id, String pipe_no, Date operation_time, String operator_no, float relative_humidity, float dew_point, float pipe_temp, String surface_condition, float blast_time, String blast_finish_sa25, float profile, String upload_files, String remark, String result, String mill_no, float air_temp, int surface_dust_rating, float salt_contamination_after_blasting, float elapsed_time) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operation_time = operation_time;
        this.operator_no = operator_no;
        this.relative_humidity = relative_humidity;
        this.dew_point = dew_point;
        this.pipe_temp = pipe_temp;
        this.surface_condition = surface_condition;
        this.blast_time = blast_time;
        this.blast_finish_sa25 = blast_finish_sa25;
        this.profile = profile;
        this.upload_files = upload_files;
        this.remark = remark;
        this.result = result;
        this.mill_no = mill_no;
        this.air_temp = air_temp;
        this.surface_dust_rating = surface_dust_rating;
        this.salt_contamination_after_blasting = salt_contamination_after_blasting;
        this.elapsed_time = elapsed_time;
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

    public float getRelative_humidity() {
        return relative_humidity;
    }

    public void setRelative_humidity(float relative_humidity) {
        this.relative_humidity = relative_humidity;
    }

    public float getDew_point() {
        return dew_point;
    }

    public void setDew_point(float dew_point) {
        this.dew_point = dew_point;
    }

    public float getPipe_temp() {
        return pipe_temp;
    }

    public void setPipe_temp(float pipe_temp) {
        this.pipe_temp = pipe_temp;
    }

    public String getSurface_condition() {
        return surface_condition;
    }

    public void setSurface_condition(String surface_condition) {
        this.surface_condition = surface_condition;
    }

    public float getBlast_time() {
        return blast_time;
    }

    public void setBlast_time(float blast_time) {
        this.blast_time = blast_time;
    }

    public String getBlast_finish_sa25() {
        return blast_finish_sa25;
    }

    public void setBlast_finish_sa25(String blast_finish_sa25) {
        this.blast_finish_sa25 = blast_finish_sa25;
    }

    public float getProfile() {
        return profile;
    }

    public void setProfile(float profile) {
        this.profile = profile;
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

    public float getAir_temp() {
        return air_temp;
    }

    public void setAir_temp(float air_temp) {
        this.air_temp = air_temp;
    }

    public int getSurface_dust_rating() {
        return surface_dust_rating;
    }

    public void setSurface_dust_rating(int surface_dust_rating) {
        this.surface_dust_rating = surface_dust_rating;
    }

    public float getSalt_contamination_after_blasting() {
        return salt_contamination_after_blasting;
    }

    public void setSalt_contamination_after_blasting(float salt_contamination_after_blasting) {
        this.salt_contamination_after_blasting = salt_contamination_after_blasting;
    }

    public float getElapsed_time() {
        return elapsed_time;
    }

    public void setElapsed_time(float elapsed_time) {
        this.elapsed_time = elapsed_time;
    }
}
