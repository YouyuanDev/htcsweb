package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class ProjectInfo {
    private int id;
    private String project_no;
    private String project_name;
    private String client_name;
    private String client_spec;
    private String coating_standard;
    private String mps;
    private String itp;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date project_time;
    private String upload_files;
    private String od_coating_acceptance_criteria_no;
    private String id_coating_acceptance_criteria_no;
    private String lab_testing_acceptance_criteria_2fbe_no;
    private String lab_testing_acceptance_criteria_3lpe_no;

    public ProjectInfo() {
    }

    public ProjectInfo(int id, String project_no, String project_name, String client_name, String client_spec, String coating_standard, String mps, String itp, Date project_time, String upload_files, String od_coating_acceptance_criteria_no, String id_coating_acceptance_criteria_no, String lab_testing_acceptance_criteria_2fbe_no, String lab_testing_acceptance_criteria_3lpe_no) {
        this.id = id;
        this.project_no = project_no;
        this.project_name = project_name;
        this.client_name = client_name;
        this.client_spec = client_spec;
        this.coating_standard = coating_standard;
        this.mps = mps;
        this.itp = itp;
        this.project_time = project_time;
        this.upload_files = upload_files;
        this.od_coating_acceptance_criteria_no = od_coating_acceptance_criteria_no;
        this.id_coating_acceptance_criteria_no = id_coating_acceptance_criteria_no;
        this.lab_testing_acceptance_criteria_2fbe_no = lab_testing_acceptance_criteria_2fbe_no;
        this.lab_testing_acceptance_criteria_3lpe_no = lab_testing_acceptance_criteria_3lpe_no;
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

    public String getCoating_standard() {
        return coating_standard;
    }

    public void setCoating_standard(String coating_standard) {
        this.coating_standard = coating_standard;
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

    public Date getProject_time() {
        return project_time;
    }

    public void setProject_time(Date project_time) {
        this.project_time = project_time;
    }

    public String getUpload_files() {
        return upload_files;
    }

    public void setUpload_files(String upload_files) {
        this.upload_files = upload_files;
    }

    public String getOd_coating_acceptance_criteria_no() {
        return od_coating_acceptance_criteria_no;
    }

    public void setOd_coating_acceptance_criteria_no(String od_coating_acceptance_criteria_no) {
        this.od_coating_acceptance_criteria_no = od_coating_acceptance_criteria_no;
    }

    public String getId_coating_acceptance_criteria_no() {
        return id_coating_acceptance_criteria_no;
    }

    public void setId_coating_acceptance_criteria_no(String id_coating_acceptance_criteria_no) {
        this.id_coating_acceptance_criteria_no = id_coating_acceptance_criteria_no;
    }

    public String getLab_testing_acceptance_criteria_2fbe_no() {
        return lab_testing_acceptance_criteria_2fbe_no;
    }

    public void setLab_testing_acceptance_criteria_2fbe_no(String lab_testing_acceptance_criteria_2fbe_no) {
        this.lab_testing_acceptance_criteria_2fbe_no = lab_testing_acceptance_criteria_2fbe_no;
    }

    public String getLab_testing_acceptance_criteria_3lpe_no() {
        return lab_testing_acceptance_criteria_3lpe_no;
    }

    public void setLab_testing_acceptance_criteria_3lpe_no(String lab_testing_acceptance_criteria_3lpe_no) {
        this.lab_testing_acceptance_criteria_3lpe_no = lab_testing_acceptance_criteria_3lpe_no;
    }
}
