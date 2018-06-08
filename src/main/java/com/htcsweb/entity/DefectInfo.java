package com.htcsweb.entity;

public class DefectInfo {

    private  int id; //流水号
    private String defect_code;     //缺陷编号
    private String defect_name;   //缺陷名称
    private String defect_type;  //缺陷类型


    public DefectInfo() {
    }

    public DefectInfo(int id, String defect_code, String defect_name, String defect_type) {
        this.id = id;
        this.defect_code = defect_code;
        this.defect_name = defect_name;
        this.defect_type = defect_type;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDefect_code() {
        return defect_code;
    }

    public void setDefect_code(String defect_code) {
        this.defect_code = defect_code;
    }

    public String getDefect_name() {
        return defect_name;
    }

    public void setDefect_name(String defect_name) {
        this.defect_name = defect_name;
    }

    public String getDefect_type() {
        return defect_type;
    }

    public void setDefect_type(String defect_type) {
        this.defect_type = defect_type;
    }
}
