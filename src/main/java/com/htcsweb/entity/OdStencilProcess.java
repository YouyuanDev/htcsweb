package com.htcsweb.entity;

import java.util.Date;

public class OdStencilProcess {
    private int id;
    private String pipe_no;
    private Date operation_time;
    private String operator_no;
    private String stencil_content;
    private String center_line_color;
    private String pipe_end_color;
    private String upload_files;
    private String remark;
    private String result;
    private String mill_no;

    public OdStencilProcess() {
    }

    public OdStencilProcess(int id, String pipe_no, Date operation_time, String operator_no, String stencil_content, String center_line_color, String pipe_end_color, String upload_files, String remark, String result, String mill_no) {
        this.id = id;
        this.pipe_no = pipe_no;
        this.operation_time = operation_time;
        this.operator_no = operator_no;
        this.stencil_content = stencil_content;
        this.center_line_color = center_line_color;
        this.pipe_end_color = pipe_end_color;
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

    public String getStencil_content() {
        return stencil_content;
    }

    public void setStencil_content(String stencil_content) {
        this.stencil_content = stencil_content;
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
