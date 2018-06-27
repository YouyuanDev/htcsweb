package com.htcsweb.entity;

public class ProcessInfo {

    private  int id;
    private String process_code;
    private String process_name;
    private String process_name_en;

    public ProcessInfo() {
    }

    public ProcessInfo(int id, String process_code, String process_name, String process_name_en) {
        this.id = id;
        this.process_code = process_code;
        this.process_name = process_name;
        this.process_name_en = process_name_en;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProcess_code() {
        return process_code;
    }

    public void setProcess_code(String process_code) {
        this.process_code = process_code;
    }

    public String getProcess_name() {
        return process_name;
    }

    public void setProcess_name(String process_name) {
        this.process_name = process_name;
    }

    public String getProcess_name_en() {
        return process_name_en;
    }

    public void setProcess_name_en(String process_name_en) {
        this.process_name_en = process_name_en;
    }
}
