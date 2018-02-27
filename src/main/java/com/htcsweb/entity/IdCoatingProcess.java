package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class IdCoatingProcess {
    private int id;
    private String pipe_no;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date operation_time;
    private String operator_no;
    private float coating_speed;
    private String base_used;
    private String base_batch;
    private String curing_agent_used;
    private String curing_agent_batch;
    private Date curing_start_time;
    private Date curing_finish_time;
    private float curing_temp;
    private String upload_files;
    private String remark;
    private String result;
    private String mill_no;

    public IdCoatingProcess() {
    }

    public IdCoatingProcess(int id, String pipe_no, Date operation_time, String operator_no, float coating_speed, String base_used, String base_batch, String curing_agent_used, String curing_agent_batch, Date curing_start_time, Date curing_finish_time, float curing_temp, String upload_files, String remark, String result, String mill_no) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operation_time = operation_time;
        this.operator_no = operator_no;
        this.coating_speed = coating_speed;
        this.base_used = base_used;
        this.base_batch = base_batch;
        this.curing_agent_used = curing_agent_used;
        this.curing_agent_batch = curing_agent_batch;
        this.curing_start_time = curing_start_time;
        this.curing_finish_time = curing_finish_time;
        this.curing_temp = curing_temp;
        this.upload_files = upload_files;
        this.remark = remark;
        this.result = result;
        this.mill_no = mill_no;
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

    public float getCoating_speed() {
        return coating_speed;
    }

    public void setCoating_speed(float coating_speed) {
        this.coating_speed = coating_speed;
    }

    public String getBase_used() {
        return base_used;
    }

    public void setBase_used(String base_used) {
        this.base_used = base_used;
    }

    public String getBase_batch() {
        return base_batch;
    }

    public void setBase_batch(String base_batch) {
        this.base_batch = base_batch;
    }

    public String getCuring_agent_used() {
        return curing_agent_used;
    }

    public void setCuring_agent_used(String curing_agent_used) {
        this.curing_agent_used = curing_agent_used;
    }

    public String getCuring_agent_batch() {
        return curing_agent_batch;
    }

    public void setCuring_agent_batch(String curing_agent_batch) {
        this.curing_agent_batch = curing_agent_batch;
    }

    public Date getCuring_start_time() {
        return curing_start_time;
    }

    public void setCuring_start_time(Date curing_start_time) {
        this.curing_start_time = curing_start_time;
    }

    public Date getCuring_finish_time() {
        return curing_finish_time;
    }

    public void setCuring_finish_time(Date curing_finish_time) {
        this.curing_finish_time = curing_finish_time;
    }

    public float getCuring_temp() {
        return curing_temp;
    }

    public void setCuring_temp(float curing_temp) {
        this.curing_temp = curing_temp;
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
}
