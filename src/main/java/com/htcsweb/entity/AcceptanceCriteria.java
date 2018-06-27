package com.htcsweb.entity;

public class AcceptanceCriteria {

    private  int id; //流水号
    private String acceptance_criteria_no;     //标准编号
    private String external_coating_type;   //外防类型
    private String internal_coating_type;   //内防类型
    private String last_update_time;   //更新日期
    private String remark;//备注


    public AcceptanceCriteria() {
    }

    public AcceptanceCriteria(int id, String acceptance_criteria_no, String external_coating_type, String internal_coating_type, String last_update_time, String remark) {
        this.id = id;
        this.acceptance_criteria_no = acceptance_criteria_no;
        this.external_coating_type = external_coating_type;
        this.internal_coating_type = internal_coating_type;
        this.last_update_time = last_update_time;
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAcceptance_criteria_no() {
        return acceptance_criteria_no;
    }

    public void setAcceptance_criteria_no(String acceptance_criteria_no) {
        this.acceptance_criteria_no = acceptance_criteria_no;
    }

    public String getExternal_coating_type() {
        return external_coating_type;
    }

    public void setExternal_coating_type(String external_coating_type) {
        this.external_coating_type = external_coating_type;
    }

    public String getInternal_coating_type() {
        return internal_coating_type;
    }

    public void setInternal_coating_type(String internal_coating_type) {
        this.internal_coating_type = internal_coating_type;
    }

    public String getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(String last_update_time) {
        this.last_update_time = last_update_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
