package com.htcsweb.entity;

public class DynamicMeasurementItem {

    private  int id; //流水号
    private String acceptance_criteria_no;     //标准编号
    private String item_code;
    private String item_name;
    private String item_name_en;
    private String unit_name;
    private String unit_name_en;
    private String item_frequency;
    private String process_code;

    private String decimal_num;
    private String need_verify;
    private String multi_value;
    private String control_type;
    private String options;
    private String max_value;
    private String min_value;
    private String default_value;


    public DynamicMeasurementItem() {
    }

    public DynamicMeasurementItem(int id, String acceptance_criteria_no, String item_code, String item_name, String item_name_en, String unit_name, String unit_name_en, String item_frequency, String process_code, String decimal_num, String need_verify, String multi_value, String control_type, String options, String max_value, String min_value, String default_value) {
        this.id = id;
        this.acceptance_criteria_no = acceptance_criteria_no;
        this.item_code = item_code;
        this.item_name = item_name;
        this.item_name_en = item_name_en;
        this.unit_name = unit_name;
        this.unit_name_en = unit_name_en;
        this.item_frequency = item_frequency;
        this.process_code = process_code;
        this.decimal_num = decimal_num;
        this.need_verify = need_verify;
        this.multi_value = multi_value;
        this.control_type = control_type;
        this.options = options;
        this.max_value = max_value;
        this.min_value = min_value;
        this.default_value = default_value;
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

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_name_en() {
        return item_name_en;
    }

    public void setItem_name_en(String item_name_en) {
        this.item_name_en = item_name_en;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getUnit_name_en() {
        return unit_name_en;
    }

    public void setUnit_name_en(String unit_name_en) {
        this.unit_name_en = unit_name_en;
    }

    public String getItem_frequency() {
        return item_frequency;
    }

    public void setItem_frequency(String item_frequency) {
        this.item_frequency = item_frequency;
    }

    public String getProcess_code() {
        return process_code;
    }

    public void setProcess_code(String process_code) {
        this.process_code = process_code;
    }

    public String getDecimal_num() {
        return decimal_num;
    }

    public void setDecimal_num(String decimal_num) {
        this.decimal_num = decimal_num;
    }

    public String getNeed_verify() {
        return need_verify;
    }

    public void setNeed_verify(String need_verify) {
        this.need_verify = need_verify;
    }

    public String getMulti_value() {
        return multi_value;
    }

    public void setMulti_value(String multi_value) {
        this.multi_value = multi_value;
    }

    public String getControl_type() {
        return control_type;
    }

    public void setControl_type(String control_type) {
        this.control_type = control_type;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getMax_value() {
        return max_value;
    }

    public void setMax_value(String max_value) {
        this.max_value = max_value;
    }

    public String getMin_value() {
        return min_value;
    }

    public void setMin_value(String min_value) {
        this.min_value = min_value;
    }

    public String getDefault_value() {
        return default_value;
    }

    public void setDefault_value(String default_value) {
        this.default_value = default_value;
    }
}
