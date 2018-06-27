package com.htcsweb.entity;

public class InspectionProcessRecordItem {

    private int id;
    private String inspection_process_record_header_code;
    private String item_code;
    private String item_value;


    public InspectionProcessRecordItem(int id, String inspection_process_record_header_code, String item_code, String item_value) {
        this.id = id;
        this.inspection_process_record_header_code = inspection_process_record_header_code;
        this.item_code = item_code;
        this.item_value = item_value;
    }


    public InspectionProcessRecordItem() {
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

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getItem_value() {
        return item_value;
    }

    public void setItem_value(String item_value) {
        this.item_value = item_value;
    }
}
