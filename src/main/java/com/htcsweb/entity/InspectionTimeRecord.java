package com.htcsweb.entity;

import java.util.Date;

public class InspectionTimeRecord {
    private int id;
    private String project_no;
    private String mill_no;
    private String inspection_item;
    private Date inspection_time;


    public InspectionTimeRecord() {
    }

    public InspectionTimeRecord(int id, String project_no, String mill_no, String inspection_item, Date inspection_time) {
        this.id = id;
        this.project_no = project_no;
        this.mill_no = mill_no;
        this.inspection_item = inspection_item;
        this.inspection_time = inspection_time;
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

    public String getMill_no() {
        return mill_no;
    }

    public void setMill_no(String mill_no) {
        this.mill_no = mill_no;
    }

    public String getInspection_item() {
        return inspection_item;
    }

    public void setInspection_item(String inspection_item) {
        this.inspection_item = inspection_item;
    }

    public Date getInspction_time() {
        return inspection_time;
    }

    public void setInspction_time(Date inspction_time) {
        this.inspection_time = inspction_time;
    }
}
