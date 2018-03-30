package com.htcsweb.entity;

import java.util.List;

public class ContractInfo {
    private  int id; //流水号
    private String project_no;    //项目编号
    private String contract_no;  //合同编号
    private float od;   //外径
    private float wt;   //壁厚
    private String external_coating;   //外防腐
    private  String internal_coating;  //内防腐
    private String grade;           //钢种
    private double total_order_length;  //合同总长度
    private double total_order_weight;  //合同总重量
    private float weight_per_meter; //米重
    private float pipe_length; //管厂
    private String remark;  //备注

    private String center_line_color;//中心色环
    private String pipe_end_color;//管端色环


    public ContractInfo() {
    }

    public ContractInfo(int id, String project_no, String contract_no, float od, float wt, String external_coating, String internal_coating, String grade, double total_order_length, double total_order_weight, float weight_per_meter, float pipe_length, String remark, String center_line_color, String pipe_end_color) {
        this.id = id;
        this.project_no = project_no;
        this.contract_no = contract_no;
        this.od = od;
        this.wt = wt;
        this.external_coating = external_coating;
        this.internal_coating = internal_coating;
        this.grade = grade;
        this.total_order_length = total_order_length;
        this.total_order_weight = total_order_weight;
        this.weight_per_meter = weight_per_meter;
        this.pipe_length = pipe_length;
        this.remark = remark;
        this.center_line_color = center_line_color;
        this.pipe_end_color = pipe_end_color;
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

    public double getTotal_order_length() {
        return total_order_length;
    }

    public void setTotal_order_length(double total_order_length) {
        this.total_order_length = total_order_length;
    }

    public double getTotal_order_weight() {
        return total_order_weight;
    }

    public void setTotal_order_weight(double total_order_weight) {
        this.total_order_weight = total_order_weight;
    }

    public float getWeight_per_meter() {
        return weight_per_meter;
    }

    public void setWeight_per_meter(float weight_per_meter) {
        this.weight_per_meter = weight_per_meter;
    }

    public float getPipe_length() {
        return pipe_length;
    }

    public void setPipe_length(float pipe_length) {
        this.pipe_length = pipe_length;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCenter_line_color() {
        return center_line_color;
    }

    public void setCenter_line_color(String center_line_color) {
        this.center_line_color = center_line_color;
    }

    public String getPipe_end_color() {
        return pipe_end_color;
    }

    public void setPipe_end_color(String pipe_end_color) {
        this.pipe_end_color = pipe_end_color;
    }
}
