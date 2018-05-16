package com.htcsweb.util;

public class CoverTwoRecord {
    private String repairNo;
    private String repairRemark;
    private String stripNo;
    private String stripRemark;
    private String onholdNo;
    private String onholdRemark;

    public CoverTwoRecord() {
    }

    public CoverTwoRecord(String repairNo, String repairRemark, String stripNo, String stripRemark, String onholdNo, String onholdRemark) {
        this.repairNo = repairNo;
        this.repairRemark = repairRemark;
        this.stripNo = stripNo;
        this.stripRemark = stripRemark;
        this.onholdNo = onholdNo;
        this.onholdRemark = onholdRemark;
    }

    public String getRepairNo() {
        return repairNo;
    }

    public void setRepairNo(String repairNo) {
        this.repairNo = repairNo;
    }

    public String getRepairRemark() {
        return repairRemark;
    }

    public void setRepairRemark(String repairRemark) {
        this.repairRemark = repairRemark;
    }

    public String getStripNo() {
        return stripNo;
    }

    public void setStripNo(String stripNo) {
        this.stripNo = stripNo;
    }

    public String getStripRemark() {
        return stripRemark;
    }

    public void setStripRemark(String stripRemark) {
        this.stripRemark = stripRemark;
    }

    public String getOnholdNo() {
        return onholdNo;
    }

    public void setOnholdNo(String onholdNo) {
        this.onholdNo = onholdNo;
    }

    public String getOnholdRemark() {
        return onholdRemark;
    }

    public void setOnholdRemark(String onholdRemark) {
        this.onholdRemark = onholdRemark;
    }
}
