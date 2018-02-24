package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class OdCoating3LpeProcess {
    private int id;
    private String pipe_no;
  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date operation_time;
    private String operator_no;
    private float coating_line_speed;
    private String base_coat_used;
    private String base_coat_lot_no;
    private String middle_coat_used;
    private String middle_coat_lot_no;
    private String top_coat_used;
    private String top_coat_lot_no;
    private int top_coat_gun_count;
    private float application_temp;
    private float to_first_touch_duration;
    private float to_quench_duration;
    private String upload_files;
    private String remark;
    private String result;

    public OdCoating3LpeProcess() {
    }

    public OdCoating3LpeProcess(int id, String pipe_no, Date operation_time, String operator_no, float coating_line_speed, String base_coat_used, String base_coat_lot_no, String middle_coat_used, String middle_coat_lot_no, String top_coat_used, String top_coat_lot_no, int top_coat_gun_count, float application_temp, float to_first_touch_duration, float to_quench_duration, String upload_files, String remark, String result) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operation_time = operation_time;
        this.operator_no = operator_no;
        this.coating_line_speed = coating_line_speed;
        this.base_coat_used = base_coat_used;
        this.base_coat_lot_no = base_coat_lot_no;
        this.middle_coat_used = middle_coat_used;
        this.middle_coat_lot_no = middle_coat_lot_no;
        this.top_coat_used = top_coat_used;
        this.top_coat_lot_no = top_coat_lot_no;
        this.top_coat_gun_count = top_coat_gun_count;
        this.application_temp = application_temp;
        this.to_first_touch_duration = to_first_touch_duration;
        this.to_quench_duration = to_quench_duration;
        this.upload_files = upload_files;
        this.remark = remark;
        this.result = result;
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

    public float getCoating_line_speed() {
        return coating_line_speed;
    }

    public void setCoating_line_speed(float coating_line_speed) {
        this.coating_line_speed = coating_line_speed;
    }

    public String getBase_coat_used() {
        return base_coat_used;
    }

    public void setBase_coat_used(String base_coat_used) {
        this.base_coat_used = base_coat_used;
    }

    public String getBase_coat_lot_no() {
        return base_coat_lot_no;
    }

    public void setBase_coat_lot_no(String base_coat_lot_no) {
        this.base_coat_lot_no = base_coat_lot_no;
    }

    public String getMiddle_coat_used() {
        return middle_coat_used;
    }

    public void setMiddle_coat_used(String middle_coat_used) {
        this.middle_coat_used = middle_coat_used;
    }

    public String getMiddle_coat_lot_no() {
        return middle_coat_lot_no;
    }

    public void setMiddle_coat_lot_no(String middle_coat_lot_no) {
        this.middle_coat_lot_no = middle_coat_lot_no;
    }

    public String getTop_coat_used() {
        return top_coat_used;
    }

    public void setTop_coat_used(String top_coat_used) {
        this.top_coat_used = top_coat_used;
    }

    public String getTop_coat_lot_no() {
        return top_coat_lot_no;
    }

    public void setTop_coat_lot_no(String top_coat_lot_no) {
        this.top_coat_lot_no = top_coat_lot_no;
    }

    public int getTop_coat_gun_count() {
        return top_coat_gun_count;
    }

    public void setTop_coat_gun_count(int top_coat_gun_count) {
        this.top_coat_gun_count = top_coat_gun_count;
    }

    public float getApplication_temp() {
        return application_temp;
    }

    public void setApplication_temp(float application_temp) {
        this.application_temp = application_temp;
    }

    public float getTo_first_touch_duration() {
        return to_first_touch_duration;
    }

    public void setTo_first_touch_duration(float to_first_touch_duration) {
        this.to_first_touch_duration = to_first_touch_duration;
    }

    public float getTo_quench_duration() {
        return to_quench_duration;
    }

    public void setTo_quench_duration(float to_quench_duration) {
        this.to_quench_duration = to_quench_duration;
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
}
