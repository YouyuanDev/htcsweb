package com.htcsweb.util;

public class CoverOneRecordOD {
    private String testSampleNo;
    private String cutLength;
    private String dscSampleNo;
    private String peSampleNo;
    public CoverOneRecordOD() {

    }

    public CoverOneRecordOD(String testSampleNo, String cutLength, String dscSampleNo, String peSampleNo) {
        this.testSampleNo = testSampleNo;
        this.cutLength = cutLength;
        this.dscSampleNo = dscSampleNo;
        this.peSampleNo = peSampleNo;
    }

    public String getTestSampleNo() {
        return testSampleNo;
    }

    public void setTestSampleNo(String testSampleNo) {
        this.testSampleNo = testSampleNo;
    }

    public String getCutLength() {
        return cutLength;
    }

    public void setCutLength(String cutLength) {
        this.cutLength = cutLength;
    }

    public String getDscSampleNo() {
        return dscSampleNo;
    }

    public void setDscSampleNo(String dscSampleNo) {
        this.dscSampleNo = dscSampleNo;
    }

    public String getPeSampleNo() {
        return peSampleNo;
    }

    public void setPeSampleNo(String peSampleNo) {
        this.peSampleNo = peSampleNo;
    }
}
