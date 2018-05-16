package com.htcsweb.util;

public class CoverOneRecordID {
    private String steelPanelNo;
    private String glassPanelNo;

    public CoverOneRecordID() {
    }

    public CoverOneRecordID(String steelPanelNo, String glassPanelNo) {
        this.steelPanelNo = steelPanelNo;
        this.glassPanelNo = glassPanelNo;
    }

    public String getSteelPanelNo() {
        return steelPanelNo;
    }

    public void setSteelPanelNo(String steelPanelNo) {
        this.steelPanelNo = steelPanelNo;
    }

    public String getGlassPanelNo() {
        return glassPanelNo;
    }

    public void setGlassPanelNo(String glassPanelNo) {
        this.glassPanelNo = glassPanelNo;
    }
}
