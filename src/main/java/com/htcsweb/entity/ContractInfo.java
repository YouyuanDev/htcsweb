package com.htcsweb.entity;

import java.util.List;

public class ContractInfo {
    private  int id;
    private String project_no;
    private String contract_no;
    private float od;
    private float wt;
    private String external_coating;
    private  String internal_coating;
    private String grade;
    private double total_oder_length;
    private float l_weight;
    private double weight;
    private float l_length;
    private String remark;

    public ContractInfo() {
    }

    public ContractInfo(int id, String project_no, String contract_no, float od, float wt, String external_coating, String internal_coating, String grade, double total_oder_length, float l_weight, double weight, float l_length, String remark) {
        this.id = id;
        this.project_no = project_no;
        this.contract_no = contract_no;
        this.od = od;
        this.wt = wt;
        this.external_coating = external_coating;
        this.internal_coating = internal_coating;
        this.grade = grade;
        this.total_oder_length = total_oder_length;
        this.l_weight = l_weight;
        this.weight = weight;
        this.l_length = l_length;
        this.remark = remark;
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

    public String getContract_no() {
        return contract_no;
    }

    public void setContract_no(String contract_no) {
        this.contract_no = contract_no;
    }

    public float getOd() {
        return od;
    }

    public void setOd(float od) {
        this.od = od;
    }

    public float getWt() {
        return wt;
    }

    public void setWt(float wt) {
        this.wt = wt;
    }

    public String getExternal_coating() {
        return external_coating;
    }

    public void setExternal_coating(String external_coating) {
        this.external_coating = external_coating;
    }

    public String getInternal_coating() {
        return internal_coating;
    }

    public void setInternal_coating(String internal_coating) {
        this.internal_coating = internal_coating;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getTotal_oder_length() {
        return total_oder_length;
    }

    public void setTotal_oder_length(double total_oder_length) {
        this.total_oder_length = total_oder_length;
    }

    public float getL_weight() {
        return l_weight;
    }

    public void setL_weight(float l_weight) {
        this.l_weight = l_weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public float getL_length() {
        return l_length;
    }

    public void setL_length(float l_length) {
        this.l_length = l_length;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
