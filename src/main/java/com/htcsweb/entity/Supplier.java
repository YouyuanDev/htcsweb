package com.htcsweb.entity;

public class Supplier {
    private int supplier_id;
    private String supplier_code;
    private String supplier_name;
    private String forwar_address;
    private String contact_phone;
    private String clearing_form;
    private String is_validity;
    private int bank;
    private String instructions;
    private String  picture;
    private String mac_c_no;
    private String mac_s_no;

    public Supplier() {
    }

    public Supplier(int supplier_id, String supplier_code, String supplier_name, String forwar_address, String contact_phone, String clearing_form, String is_validity, int bank, String instructions, String picture, String mac_c_no, String mac_s_no) {
        this.supplier_id = supplier_id;
        this.supplier_code = supplier_code;
        this.supplier_name = supplier_name;
        this.forwar_address = forwar_address;
        this.contact_phone = contact_phone;
        this.clearing_form = clearing_form;
        this.is_validity = is_validity;
        this.bank = bank;
        this.instructions = instructions;
        this.picture = picture;
        this.mac_c_no = mac_c_no;
        this.mac_s_no = mac_s_no;
    }

    public Supplier(String supplier_code, String supplier_name, String forwar_address, String contact_phone, String clearing_form, String is_validity, int bank, String instructions, String picture, String mac_c_no, String mac_s_no) {
        this.supplier_code = supplier_code;
        this.supplier_name = supplier_name;
        this.forwar_address = forwar_address;
        this.contact_phone = contact_phone;
        this.clearing_form = clearing_form;
        this.is_validity = is_validity;
        this.bank = bank;
        this.instructions = instructions;
        this.picture = picture;
        this.mac_c_no = mac_c_no;
        this.mac_s_no = mac_s_no;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getSupplier_code() {
        return supplier_code;
    }

    public void setSupplier_code(String supplier_code) {
        this.supplier_code = supplier_code;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getForwar_address() {
        return forwar_address;
    }

    public void setForwar_address(String forwar_address) {
        this.forwar_address = forwar_address;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getClearing_form() {
        return clearing_form;
    }

    public void setClearing_form(String clearing_form) {
        this.clearing_form = clearing_form;
    }

    public String getIs_validity() {
        return is_validity;
    }

    public void setIs_validity(String is_validity) {
        this.is_validity = is_validity;
    }

    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getMac_c_no() {
        return mac_c_no;
    }

    public void setMac_c_no(String mac_c_no) {
        this.mac_c_no = mac_c_no;
    }

    public String getMac_s_no() {
        return mac_s_no;
    }

    public void setMac_s_no(String mac_s_no) {
        this.mac_s_no = mac_s_no;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "supplier_id=" + supplier_id +
                ", supplier_code='" + supplier_code + '\'' +
                ", supplier_name='" + supplier_name + '\'' +
                ", forwar_address='" + forwar_address + '\'' +
                ", contact_phone='" + contact_phone + '\'' +
                ", clearing_form=" + clearing_form +
                ", is_validity=" + is_validity +
                ", bank=" + bank +
                ", instructions='" + instructions + '\'' +
                ", picture='" + picture + '\'' +
                ", mac_c_no='" + mac_c_no + '\'' +
                ", mac_s_no='" + mac_s_no + '\'' +
                '}';
    }
}
