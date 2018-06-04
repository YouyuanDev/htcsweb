package com.htcsweb.entity;

import java.util.Date;
import java.util.List;

public class PipeBasicInfo {
    private int id;
    private String contract_no;
    private String pipe_no;
    private String grade;
    private float od;
    private float wt;
    private float p_length;
    private float weight;
    private String pipe_making_lot_no;
    private String status;
    private String heat_no;
    private String storage_stack;
    private String stack_level;
    private Date od_coating_date;
    private Date id_coating_date;

    private String level_direction;
    private String level_sequence;
    private Date shipment_date;
    private String last_accepted_status;

    private String rebevel_mark;


    public PipeBasicInfo() {
    }

    public PipeBasicInfo(int id, String contract_no, String pipe_no, String grade, float od, float wt, float p_length, float weight, String pipe_making_lot_no, String status, String heat_no, String storage_stack, String stack_level, Date od_coating_date, Date id_coating_date, String level_direction, String level_sequence, Date shipment_date, String last_accepted_status, String rebevel_mark) {
        this.id = id;
        this.contract_no = contract_no;
        this.pipe_no = pipe_no;
        this.grade = grade;
        this.od = od;
        this.wt = wt;
        this.p_length = p_length;
        this.weight = weight;
        this.pipe_making_lot_no = pipe_making_lot_no;
        this.status = status;
        this.heat_no = heat_no;
        this.storage_stack = storage_stack;
        this.stack_level = stack_level;
        this.od_coating_date = od_coating_date;
        this.id_coating_date = id_coating_date;
        this.level_direction = level_direction;
        this.level_sequence = level_sequence;
        this.shipment_date = shipment_date;
        this.last_accepted_status = last_accepted_status;
        this.rebevel_mark = rebevel_mark;
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

    public float getP_length() {
        return p_length;
    }

    public void setP_length(float p_length) {
        this.p_length = p_length;
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

    public String getHeat_no() {
        return heat_no;
    }

    public void setHeat_no(String heat_no) {
        this.heat_no = heat_no;
    }

    public String getStorage_stack() {
        return storage_stack;
    }

    public void setStorage_stack(String storage_stack) {
        this.storage_stack = storage_stack;
    }

    public String getStack_level() {
        return stack_level;
    }

    public void setStack_level(String stack_level) {
        this.stack_level = stack_level;
    }

    public Date getOd_coating_date() {
        return od_coating_date;
    }

    public void setOd_coating_date(Date od_coating_date) {
        this.od_coating_date = od_coating_date;
    }

    public Date getId_coating_date() {
        return id_coating_date;
    }

    public void setId_coating_date(Date id_coating_date) {
        this.id_coating_date = id_coating_date;
    }

    public String getLevel_direction() {
        return level_direction;
    }

    public void setLevel_direction(String level_direction) {
        this.level_direction = level_direction;
    }

    public String getLevel_sequence() {
        return level_sequence;
    }

    public void setLevel_sequence(String level_sequence) {
        this.level_sequence = level_sequence;
    }

    public Date getShipment_date() {
        return shipment_date;
    }

    public void setShipment_date(Date shipment_date) {
        this.shipment_date = shipment_date;
    }

    public String getLast_accepted_status() {
        return last_accepted_status;
    }

    public void setLast_accepted_status(String last_accepted_status) {
        this.last_accepted_status = last_accepted_status;
    }

    public String getRebevel_mark() {
        return rebevel_mark;
    }

    public void setRebevel_mark(String rebevel_mark) {
        this.rebevel_mark = rebevel_mark;
    }
}
