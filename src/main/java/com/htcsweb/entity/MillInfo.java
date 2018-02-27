package com.htcsweb.entity;

public class MillInfo {

    private int id;
    private String mill_no;
    private String mill_name;

    public MillInfo() {
    }

    public MillInfo(int id, String mill_no, String mill_name) {
        this.id = id;
        this.mill_no = mill_no;
        this.mill_name = mill_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMill_no() {
        return mill_no;
    }

    public void setMill_no(String mill_no) {
        this.mill_no = mill_no;
    }

    public String getMill_name() {
        return mill_name;
    }

    public void setMill_name(String mill_name) {
        this.mill_name = mill_name;
    }
}
