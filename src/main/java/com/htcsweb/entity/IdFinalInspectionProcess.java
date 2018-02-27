package com.htcsweb.entity;

import java.util.Date;

public class IdFinalInspectionProcess {
    private int id;
    private String pipe_no;
    private Date operation_time;
    private String operator_no;
    private String od_inspection_result;
    private String id_inspection_result;
    private String final_inspection_result;
    private String upload_files;
    private String remark;
    private String result;
    private String mill_no;

    public IdFinalInspectionProcess() {
    }

    public IdFinalInspectionProcess(int id, String pipe_no, Date operation_time, String operator_no, String od_inspection_result, String id_inspection_result, String final_inspection_result, String upload_files, String remark, String result, String mill_no) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operation_time = operation_time;
        this.operator_no = operator_no;
        this.od_inspection_result = od_inspection_result;
        this.id_inspection_result = id_inspection_result;
        this.final_inspection_result = final_inspection_result;
        this.upload_files = upload_files;
        this.remark = remark;
        this.result = result;
        this.mill_no = mill_no;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPipe_no() {
        return pipe_no;
    }

    public void setPipe_no(String pipe_no) {
        this.pipe_no = pipe_no;
    }

    public Date getOperation_time() {
        return operation_time;
    }

    public void setOperation_time(Date operation_time) {
        this.operation_time = operation_time;
    }

    public String getOperator_no() {
        return operator_no;
    }

    public void setOperator_no(String operator_no) {
        this.operator_no = operator_no;
    }

    public String getOd_inspection_result() {
        return od_inspection_result;
    }

    public void setOd_inspection_result(String od_inspection_result) {
        this.od_inspection_result = od_inspection_result;
    }

    public String getId_inspection_result() {
        return id_inspection_result;
    }

    public void setId_inspection_result(String id_inspection_result) {
        this.id_inspection_result = id_inspection_result;
    }

    public String getFinal_inspection_result() {
        return final_inspection_result;
    }

    public void setFinal_inspection_result(String final_inspection_result) {
        this.final_inspection_result = final_inspection_result;
    }

    public String getUpload_files() {
        return upload_files;
    }

    public void setUpload_files(String upload_files) {
        this.upload_files = upload_files;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMill_no() {
        return mill_no;
    }

    public void setMill_no(String mill_no) {
        this.mill_no = mill_no;
    }
}
