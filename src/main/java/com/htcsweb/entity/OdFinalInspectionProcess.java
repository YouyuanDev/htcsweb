package com.htcsweb.entity;

import java.util.Date;

public class OdFinalInspectionProcess {
    private int id;
    private String pipe_no;
    private Date operation_time;
    private String operator_no;
    private String inspection_result;
    private String upload_files;
    private String remark;
    private String result;
    private String mill_no;
    private String cutback_length;
    private String stencil_verification;

    public OdFinalInspectionProcess() {
    }

    public OdFinalInspectionProcess(int id, String pipe_no, Date operation_time, String operator_no, String inspection_result, String upload_files, String remark, String result, String mill_no, String cutback_length, String stencil_verification) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operation_time = operation_time;
        this.operator_no = operator_no;
        this.inspection_result = inspection_result;
        this.upload_files = upload_files;
        this.remark = remark;
        this.result = result;
        this.mill_no = mill_no;
        this.cutback_length = cutback_length;
        this.stencil_verification = stencil_verification;
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

    public String getInspection_result() {
        return inspection_result;
    }

    public void setInspection_result(String inspection_result) {
        this.inspection_result = inspection_result;
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

    public String getCutback_length() {
        return cutback_length;
    }

    public void setCutback_length(String cutback_length) {
        this.cutback_length = cutback_length;
    }

    public String getStencil_verification() {
        return stencil_verification;
    }

    public void setStencil_verification(String stencil_verification) {
        this.stencil_verification = stencil_verification;
    }
}
