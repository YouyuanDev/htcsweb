package com.htcsweb.entity;

public class CoatingPowderInfo {

    private  int id; //流水号
    private String coating_powder_name;    //涂层粉末名称


    public CoatingPowderInfo() {
    }

    public CoatingPowderInfo(int id, String coating_powder_name) {
        this.id = id;
        this.coating_powder_name = coating_powder_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoating_powder_name() {
        return coating_powder_name;
    }

    public void setCoating_powder_name(String coating_powder_name) {
        this.coating_powder_name = coating_powder_name;
    }
}
