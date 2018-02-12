package com.htcsweb.entity;

import java.util.Date;
import java.util.List;

public class ProjectInfo {
    private int id;
    private String project_no;
    private String project_name;
    private String client_name;
    private String client_spec;
    private String mps;
    private String itp;
    private String coating_standard;
    private Date project_time;

    public ProjectInfo(int id, String project_no, String project_name, String client_name, String client_spec, String mps, String itp, String coating_standard, Date project_time) {
        this.id = id;
        this.project_no = project_no;
        this.project_name = project_name;
        this.client_name = client_name;
        this.client_spec = client_spec;
        this.mps = mps;
        this.itp = itp;
        this.coating_standard = coating_standard;
        this.project_time = project_time;
    }

    public Date getProject_time() {
        return project_time;
    }

    public void setProject_time(Date project_time) {
        this.project_time = project_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProject_no() {
        return project_no;
    }

    public void setProject_no(String project_no) {
        this.project_no = project_no;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_spec() {
        return client_spec;
    }

    public void setClient_spec(String client_spec) {
        this.client_spec = client_spec;
    }

    public String getMps() {
        return mps;
    }

    public void setMps(String mps) {
        this.mps = mps;
    }

    public String getItp() {
        return itp;
    }

    public void setItp(String itp) {
        this.itp = itp;
    }

    public String getCoating_standard() {
        return coating_standard;
    }

    public void setCoating_standard(String coating_standard) {
        this.coating_standard = coating_standard;
    }
}
