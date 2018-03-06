package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class PipeQrCodePrintRecord {

    private  int id; //流水号
    private String pipe_no;    //钢管编号
    private String operator_no;  //操作工编号
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date operation_time;
    private String remark;

    public PipeQrCodePrintRecord() {
    }

    public PipeQrCodePrintRecord(int id, String pipe_no, String operator_no, Date operation_time, String remark) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operator_no = operator_no;
        this.operation_time = operation_time;
        this.remark = remark;
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

    public String getOperator_no() {
        return operator_no;
    }

    public void setOperator_no(String operator_no) {
        this.operator_no = operator_no;
    }

    public Date getOperation_time() {
        return operation_time;
    }

    public void setOperation_time(Date operation_time) {
        this.operation_time = operation_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
