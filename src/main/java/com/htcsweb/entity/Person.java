package com.htcsweb.entity;

import java.util.Date;

public class Person {
    private int id;
    private String employee_no;
    private String pname;
    private String ppassword;
    private String pidcard_no;
    private String pmobile;
    private int page;
    private String psex;
    private String pstatus;
    private String pdepartment;
    private Date pregister_time;

    public Person() {
    }

    public Person(int id, String employee_no, String pname, String ppassword, String pidcard_no, String pmobile, int page, String psex, String pstatus, String pdepartment, Date pregister_time) {
        this.id = id;
        this.employee_no = employee_no;
        this.pname = pname;
        this.ppassword = ppassword;
        this.pidcard_no = pidcard_no;
        this.pmobile = pmobile;
        this.page = page;
        this.psex = psex;
        this.pstatus = pstatus;
        this.pdepartment = pdepartment;
        this.pregister_time = pregister_time;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployee_no() {
        return employee_no;
    }

    public void setEmployee_no(String employee_no) {
        this.employee_no = employee_no;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPpassword() {
        return ppassword;
    }

    public void setPpassword(String ppassword) {
        this.ppassword = ppassword;
    }

    public String getPidcard_no() {
        return pidcard_no;
    }

    public void setPidcard_no(String pidcard_no) {
        this.pidcard_no = pidcard_no;
    }

    public String getPmobile() {
        return pmobile;
    }

    public void setPmobile(String pmobile) {
        this.pmobile = pmobile;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getPsex() {
        return psex;
    }

    public void setPsex(String psex) {
        this.psex = psex;
    }

    public String getPstatus() {
        return pstatus;
    }

    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }

    public String getPdepartment() {
        return pdepartment;
    }

    public void setPdepartment(String pdepartment) {
        this.pdepartment = pdepartment;
    }

    public Date getPregister_time() {
        return pregister_time;
    }

    public void setPregister_time(Date pregister_time) {
        this.pregister_time = pregister_time;
    }
}
