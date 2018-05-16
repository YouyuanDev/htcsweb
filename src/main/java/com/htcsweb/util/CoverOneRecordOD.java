package com.htcsweb.util;

public class CoverOneRecordOD {
    private String testSampleNo;
    private String cutLength;
    private String dscSampleNo;
    public CoverOneRecordOD() {

    }

    public CoverOneRecordOD(String testSampleNo, String cutLength, String dscSampleNo) {
        this.testSampleNo = testSampleNo;
        this.cutLength = cutLength;
        this.dscSampleNo = dscSampleNo;
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
}
