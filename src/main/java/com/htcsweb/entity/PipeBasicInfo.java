package com.htcsweb.entity;

import java.util.List;

public class PipeBasicInfo {
    private int id;
    private String contract_no;
    private String pipe_no;
    private String grade;
    private float od;
    private float wt;
    private float length;
    private float weight;
    private String pipe_making_lot_no;
    private String status;

    public PipeBasicInfo() {
    }

    public PipeBasicInfo(int id, String contract_no, String pipe_no, String grade, float od, float wt, float length, float weight, String pipe_making_lot_no, String status) {
        this.id = id;
        this.contract_no = contract_no;
        this.pipe_no = pipe_no;
        this.grade = grade;
        this.od = od;
        this.wt = wt;
        this.length = length;
        this.weight = weight;
        this.pipe_making_lot_no = pipe_making_lot_no;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContract_no() {
        return contract_no;
    }

    public void setContract_no(String contract_no) {
        this.contract_no = contract_no;
    }

    public String getPipe_no() {
        return pipe_no;
    }

    public void setPipe_no(String pipe_no) {
        this.pipe_no = pipe_no;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
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

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getPipe_making_lot_no() {
        return pipe_making_lot_no;
    }

    public void setPipe_making_lot_no(String pipe_making_lot_no) {
        this.pipe_making_lot_no = pipe_making_lot_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
