package com.htcsweb.entity;

import java.util.Date;

public class OdBlastInspectionProcess {
   private int id;
   private String pipe_no;
   private Date operation_time;
   private String operator_no;
   private float air_temp;
   private float relative_humidity;
   private float dew_point;
   private String blast_finish_sa25;
   private float profile;
   private int surface_dust_rating;
   private float pipe_temp;
   private float salt_contamination_after_blasting;
   private String surface_condition;
   private String upload_files;
   private String remark;
   private  String result;
   private  String mill_no;
   private  String oil_water_in_air_compressor;

    public OdBlastInspectionProcess() {
    }

    public OdBlastInspectionProcess(int id, String pipe_no, Date operation_time, String operator_no, float air_temp, float relative_humidity, float dew_point, String blast_finish_sa25, float profile, int surface_dust_rating, float pipe_temp, float salt_contamination_after_blasting, String surface_condition, String upload_files, String remark, String result, String mill_no, String oil_water_in_air_compressor) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operation_time = operation_time;
        this.operator_no = operator_no;
        this.air_temp = air_temp;
        this.relative_humidity = relative_humidity;
        this.dew_point = dew_point;
        this.blast_finish_sa25 = blast_finish_sa25;
        this.profile = profile;
        this.surface_dust_rating = surface_dust_rating;
        this.pipe_temp = pipe_temp;
        this.salt_contamination_after_blasting = salt_contamination_after_blasting;
        this.surface_condition = surface_condition;
        this.upload_files = upload_files;
        this.remark = remark;
        this.result = result;
        this.mill_no = mill_no;
        this.oil_water_in_air_compressor = oil_water_in_air_compressor;
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

    public float getAir_temp() {
        return air_temp;
    }

    public void setAir_temp(float air_temp) {
        this.air_temp = air_temp;
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

    public int getSurface_dust_rating() {
        return surface_dust_rating;
    }

    public void setSurface_dust_rating(int surface_dust_rating) {
        this.surface_dust_rating = surface_dust_rating;
    }

    public float getPipe_temp() {
        return pipe_temp;
    }

    public void setPipe_temp(float pipe_temp) {
        this.pipe_temp = pipe_temp;
    }

    public float getSalt_contamination_after_blasting() {
        return salt_contamination_after_blasting;
    }

    public void setSalt_contamination_after_blasting(float salt_contamination_after_blasting) {
        this.salt_contamination_after_blasting = salt_contamination_after_blasting;
    }

    public String getSurface_condition() {
        return surface_condition;
    }

    public void setSurface_condition(String surface_condition) {
        this.surface_condition = surface_condition;
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

    public String getOil_water_in_air_compressor() {
        return oil_water_in_air_compressor;
    }

    public void setOil_water_in_air_compressor(String oil_water_in_air_compressor) {
        this.oil_water_in_air_compressor = oil_water_in_air_compressor;
    }
}
