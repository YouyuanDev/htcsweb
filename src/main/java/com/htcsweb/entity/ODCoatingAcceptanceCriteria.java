package com.htcsweb.entity;

public class ODCoatingAcceptanceCriteria {

    private  int id; //流水号
    private String coating_acceptance_criteria_no;    //接收标准编号

    private float salt_contamination_before_blast_max;  //
    private float salt_contamination_before_blast_min;   //

    private float preheat_temp_max;
    private float preheat_temp_min;

    private float relative_humidity_max;
    private float relative_humidity_min;

    private float temp_above_dew_point_max;
    private float temp_above_dew_point_min;

    private float blast_finish_sa25_max;
    private float blast_finish_sa25_min;

    private int surface_dust_rating_max;
    private int surface_dust_rating_min;

    private float od_profile_max;
    private float od_profile_min;

    private float pipe_temp_after_blast_max;
    private float pipe_temp_after_blast_min;

    private float salt_contamination_after_blasting_max;
    private float salt_contamination_after_blasting_min;

    private float application_temp_max;
    private float application_temp_min;

    private float base_2fbe_coat_thickness_max;
    private float base_2fbe_coat_thickness_min;


    private float top_2fbe_coat_thickness_max;
    private float top_2fbe_coat_thickness_min;

    private float total_2fbe_coat_thickness_max;
    private float total_2fbe_coat_thickness_min;


    private float top_3lpe_coat_thickness_max;
    private float top_3lpe_coat_thickness_min;

    private float middle_3lpe_coat_thickness_max;
    private float middle_3lpe_coat_thickness_min;

    private float base_3lpe_coat_thickness_max;
    private float base_3lpe_coat_thickness_min;

    private float total_3lpe_coat_thickness_max;
    private float total_3lpe_coat_thickness_min;

    private int repair_max;
    private int repair_min;


    private float holiday_tester_voltage_max;
    private float holiday_tester_voltage_min;

    private float cutback_max;
    private float cutback_min;

    public ODCoatingAcceptanceCriteria() {
    }

    public ODCoatingAcceptanceCriteria(int id, String coating_acceptance_criteria_no, float salt_contamination_before_blast_max, float salt_contamination_before_blast_min, float preheat_temp_max, float preheat_temp_min, float relative_humidity_max, float relative_humidity_min, float temp_above_dew_point_max, float temp_above_dew_point_min, float blast_finish_sa25_max, float blast_finish_sa25_min, int surface_dust_rating_max, int surface_dust_rating_min, float od_profile_max, float od_profile_min, float pipe_temp_after_blast_max, float pipe_temp_after_blast_min, float salt_contamination_after_blasting_max, float salt_contamination_after_blasting_min, float application_temp_max, float application_temp_min, float base_2fbe_coat_thickness_max, float base_2fbe_coat_thickness_min, float top_2fbe_coat_thickness_max, float top_2fbe_coat_thickness_min, float total_2fbe_coat_thickness_max, float total_2fbe_coat_thickness_min, float top_3lpe_coat_thickness_max, float top_3lpe_coat_thickness_min, float middle_3lpe_coat_thickness_max, float middle_3lpe_coat_thickness_min, float base_3lpe_coat_thickness_max, float base_3lpe_coat_thickness_min, float total_3lpe_coat_thickness_max, float total_3lpe_coat_thickness_min, int repair_max, int repair_min, float holiday_tester_voltage_max, float holiday_tester_voltage_min, float cutback_max, float cutback_min) {
        this.id = id;
        this.coating_acceptance_criteria_no = coating_acceptance_criteria_no;
        this.salt_contamination_before_blast_max = salt_contamination_before_blast_max;
        this.salt_contamination_before_blast_min = salt_contamination_before_blast_min;
        this.preheat_temp_max = preheat_temp_max;
        this.preheat_temp_min = preheat_temp_min;
        this.relative_humidity_max = relative_humidity_max;
        this.relative_humidity_min = relative_humidity_min;
        this.temp_above_dew_point_max = temp_above_dew_point_max;
        this.temp_above_dew_point_min = temp_above_dew_point_min;
        this.blast_finish_sa25_max = blast_finish_sa25_max;
        this.blast_finish_sa25_min = blast_finish_sa25_min;
        this.surface_dust_rating_max = surface_dust_rating_max;
        this.surface_dust_rating_min = surface_dust_rating_min;
        this.od_profile_max = od_profile_max;
        this.od_profile_min = od_profile_min;
        this.pipe_temp_after_blast_max = pipe_temp_after_blast_max;
        this.pipe_temp_after_blast_min = pipe_temp_after_blast_min;
        this.salt_contamination_after_blasting_max = salt_contamination_after_blasting_max;
        this.salt_contamination_after_blasting_min = salt_contamination_after_blasting_min;
        this.application_temp_max = application_temp_max;
        this.application_temp_min = application_temp_min;
        this.base_2fbe_coat_thickness_max = base_2fbe_coat_thickness_max;
        this.base_2fbe_coat_thickness_min = base_2fbe_coat_thickness_min;
        this.top_2fbe_coat_thickness_max = top_2fbe_coat_thickness_max;
        this.top_2fbe_coat_thickness_min = top_2fbe_coat_thickness_min;
        this.total_2fbe_coat_thickness_max = total_2fbe_coat_thickness_max;
        this.total_2fbe_coat_thickness_min = total_2fbe_coat_thickness_min;
        this.top_3lpe_coat_thickness_max = top_3lpe_coat_thickness_max;
        this.top_3lpe_coat_thickness_min = top_3lpe_coat_thickness_min;
        this.middle_3lpe_coat_thickness_max = middle_3lpe_coat_thickness_max;
        this.middle_3lpe_coat_thickness_min = middle_3lpe_coat_thickness_min;
        this.base_3lpe_coat_thickness_max = base_3lpe_coat_thickness_max;
        this.base_3lpe_coat_thickness_min = base_3lpe_coat_thickness_min;
        this.total_3lpe_coat_thickness_max = total_3lpe_coat_thickness_max;
        this.total_3lpe_coat_thickness_min = total_3lpe_coat_thickness_min;
        this.repair_max = repair_max;
        this.repair_min = repair_min;
        this.holiday_tester_voltage_max = holiday_tester_voltage_max;
        this.holiday_tester_voltage_min = holiday_tester_voltage_min;
        this.cutback_max = cutback_max;
        this.cutback_min = cutback_min;
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

    public float getPreheat_temp_max() {
        return preheat_temp_max;
    }

    public void setPreheat_temp_max(float preheat_temp_max) {
        this.preheat_temp_max = preheat_temp_max;
    }

    public float getPreheat_temp_min() {
        return preheat_temp_min;
    }

    public void setPreheat_temp_min(float preheat_temp_min) {
        this.preheat_temp_min = preheat_temp_min;
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

    public float getOd_profile_max() {
        return od_profile_max;
    }

    public void setOd_profile_max(float od_profile_max) {
        this.od_profile_max = od_profile_max;
    }

    public float getOd_profile_min() {
        return od_profile_min;
    }

    public void setOd_profile_min(float od_profile_min) {
        this.od_profile_min = od_profile_min;
    }

    public float getPipe_temp_after_blast_max() {
        return pipe_temp_after_blast_max;
    }

    public void setPipe_temp_after_blast_max(float pipe_temp_after_blast_max) {
        this.pipe_temp_after_blast_max = pipe_temp_after_blast_max;
    }

    public float getPipe_temp_after_blast_min() {
        return pipe_temp_after_blast_min;
    }

    public void setPipe_temp_after_blast_min(float pipe_temp_after_blast_min) {
        this.pipe_temp_after_blast_min = pipe_temp_after_blast_min;
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

    public float getApplication_temp_max() {
        return application_temp_max;
    }

    public void setApplication_temp_max(float application_temp_max) {
        this.application_temp_max = application_temp_max;
    }

    public float getApplication_temp_min() {
        return application_temp_min;
    }

    public void setApplication_temp_min(float application_temp_min) {
        this.application_temp_min = application_temp_min;
    }

    public float getBase_2fbe_coat_thickness_max() {
        return base_2fbe_coat_thickness_max;
    }

    public void setBase_2fbe_coat_thickness_max(float base_2fbe_coat_thickness_max) {
        this.base_2fbe_coat_thickness_max = base_2fbe_coat_thickness_max;
    }

    public float getBase_2fbe_coat_thickness_min() {
        return base_2fbe_coat_thickness_min;
    }

    public void setBase_2fbe_coat_thickness_min(float base_2fbe_coat_thickness_min) {
        this.base_2fbe_coat_thickness_min = base_2fbe_coat_thickness_min;
    }

    public float getTop_2fbe_coat_thickness_max() {
        return top_2fbe_coat_thickness_max;
    }

    public void setTop_2fbe_coat_thickness_max(float top_2fbe_coat_thickness_max) {
        this.top_2fbe_coat_thickness_max = top_2fbe_coat_thickness_max;
    }

    public float getTop_2fbe_coat_thickness_min() {
        return top_2fbe_coat_thickness_min;
    }

    public void setTop_2fbe_coat_thickness_min(float top_2fbe_coat_thickness_min) {
        this.top_2fbe_coat_thickness_min = top_2fbe_coat_thickness_min;
    }

    public float getTotal_2fbe_coat_thickness_max() {
        return total_2fbe_coat_thickness_max;
    }

    public void setTotal_2fbe_coat_thickness_max(float total_2fbe_coat_thickness_max) {
        this.total_2fbe_coat_thickness_max = total_2fbe_coat_thickness_max;
    }

    public float getTotal_2fbe_coat_thickness_min() {
        return total_2fbe_coat_thickness_min;
    }

    public void setTotal_2fbe_coat_thickness_min(float total_2fbe_coat_thickness_min) {
        this.total_2fbe_coat_thickness_min = total_2fbe_coat_thickness_min;
    }

    public float getTop_3lpe_coat_thickness_max() {
        return top_3lpe_coat_thickness_max;
    }

    public void setTop_3lpe_coat_thickness_max(float top_3lpe_coat_thickness_max) {
        this.top_3lpe_coat_thickness_max = top_3lpe_coat_thickness_max;
    }

    public float getTop_3lpe_coat_thickness_min() {
        return top_3lpe_coat_thickness_min;
    }

    public void setTop_3lpe_coat_thickness_min(float top_3lpe_coat_thickness_min) {
        this.top_3lpe_coat_thickness_min = top_3lpe_coat_thickness_min;
    }

    public float getMiddle_3lpe_coat_thickness_max() {
        return middle_3lpe_coat_thickness_max;
    }

    public void setMiddle_3lpe_coat_thickness_max(float middle_3lpe_coat_thickness_max) {
        this.middle_3lpe_coat_thickness_max = middle_3lpe_coat_thickness_max;
    }

    public float getMiddle_3lpe_coat_thickness_min() {
        return middle_3lpe_coat_thickness_min;
    }

    public void setMiddle_3lpe_coat_thickness_min(float middle_3lpe_coat_thickness_min) {
        this.middle_3lpe_coat_thickness_min = middle_3lpe_coat_thickness_min;
    }

    public float getBase_3lpe_coat_thickness_max() {
        return base_3lpe_coat_thickness_max;
    }

    public void setBase_3lpe_coat_thickness_max(float base_3lpe_coat_thickness_max) {
        this.base_3lpe_coat_thickness_max = base_3lpe_coat_thickness_max;
    }

    public float getBase_3lpe_coat_thickness_min() {
        return base_3lpe_coat_thickness_min;
    }

    public void setBase_3lpe_coat_thickness_min(float base_3lpe_coat_thickness_min) {
        this.base_3lpe_coat_thickness_min = base_3lpe_coat_thickness_min;
    }

    public float getTotal_3lpe_coat_thickness_max() {
        return total_3lpe_coat_thickness_max;
    }

    public void setTotal_3lpe_coat_thickness_max(float total_3lpe_coat_thickness_max) {
        this.total_3lpe_coat_thickness_max = total_3lpe_coat_thickness_max;
    }

    public float getTotal_3lpe_coat_thickness_min() {
        return total_3lpe_coat_thickness_min;
    }

    public void setTotal_3lpe_coat_thickness_min(float total_3lpe_coat_thickness_min) {
        this.total_3lpe_coat_thickness_min = total_3lpe_coat_thickness_min;
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
}
