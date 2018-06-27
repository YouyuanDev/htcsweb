package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class InspectionProcessRecordHeader {
    private int id;
    private String inspection_process_record_header_code;
    private String process_code;
    private String operator_no;
    private String mill_no;
    private String pipe_no;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date operation_time;
    private String upload_files;
    private String remark;
    private String result;




    public InspectionProcessRecordHeader() {
    }

    public InspectionProcessRecordHeader(int id, String inspection_process_record_header_code, String process_code, String operator_no, String mill_no, String pipe_no, Date operation_time, String upload_files, String remark, String result) {
        this.id = id;
        this.inspection_process_record_header_code = inspection_process_record_header_code;
        this.process_code = process_code;
        this.operator_no = operator_no;
        this.mill_no = mill_no;
        this.pipe_no = pipe_no;
        this.operation_time = operation_time;
        this.upload_files = upload_files;
        this.remark = remark;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInspection_process_record_header_code() {
        return inspection_process_record_header_code;
    }

    public void setInspection_process_record_header_code(String inspection_process_record_header_code) {
        this.inspection_process_record_header_code = inspection_process_record_header_code;
    }

    public String getProcess_code() {
        return process_code;
    }

    public void setProcess_code(String process_code) {
        this.process_code = process_code;
    }

    public String getOperator_no() {
        return operator_no;
    }

    public void setOperator_no(String operator_no) {
        this.operator_no = operator_no;
    }

    public String getMill_no() {
        return mill_no;
    }

    public void setMill_no(String mill_no) {
        this.mill_no = mill_no;
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
}
