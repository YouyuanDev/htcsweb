package com.htcsweb.entity;

import java.util.List;

public class PipeStatus {
    private  int id;
    private String status_code;
    private String status_name;
    //一对一和钢管基础信息表
    private PipeBasicInfo pipeBasicInfo;
    public PipeStatus() {
    }

    public PipeStatus(int id, String status_code, String status_name) {
        this.id = id;
        this.status_code = status_code;
        this.status_name = status_name;
    }

    public PipeBasicInfo getPipeBasicInfo() {
        return pipeBasicInfo;
    }

    public void setPipeBasicInfo(PipeBasicInfo pipeBasicInfo) {
        this.pipeBasicInfo = pipeBasicInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }
}
