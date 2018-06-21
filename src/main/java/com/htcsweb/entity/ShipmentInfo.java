package com.htcsweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ShipmentInfo {

    private int id;
    private String operator_no;
    private String shipment_no;
    private String pipe_no;
    private String vehicle_plate_no;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date shipment_date;
    private String upload_files;
    private String remark;


    public ShipmentInfo() {
    }

    public ShipmentInfo(int id, String operator_no, String shipment_no, String pipe_no, String vehicle_plate_no, Date shipment_date, String upload_files, String remark) {
        this.id = id;
        this.operator_no = operator_no;
        this.shipment_no = shipment_no;
        this.pipe_no = pipe_no;
        this.vehicle_plate_no = vehicle_plate_no;
        this.shipment_date = shipment_date;
        this.upload_files = upload_files;
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperator_no() {
        return operator_no;
    }

    public void setOperator_no(String operator_no) {
        this.operator_no = operator_no;
    }

    public String getShipment_no() {
        return shipment_no;
    }

    public void setShipment_no(String shipment_no) {
        this.shipment_no = shipment_no;
    }

    public String getPipe_no() {
        return pipe_no;
    }

    public void setPipe_no(String pipe_no) {
        this.pipe_no = pipe_no;
    }

    public String getVehicle_plate_no() {
        return vehicle_plate_no;
    }

    public void setVehicle_plate_no(String vehicle_plate_no) {
        this.vehicle_plate_no = vehicle_plate_no;
    }

    public Date getShipment_date() {
        return shipment_date;
    }

    public void setShipment_date(Date shipment_date) {
        this.shipment_date = shipment_date;
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
}
