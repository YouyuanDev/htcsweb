package com.htcsweb.dao;

import com.htcsweb.entity.OdFinalInspectionProcess;

import java.util.List;

public interface OdFinalInspectionProcessDao {
    public OdFinalInspectionProcess getOdFinalInProcessById(int id);
    public void addOdFinalInProcess(OdFinalInspectionProcess odFinalInspectionProcess);
    public List<OdFinalInspectionProcess> getOdFinalInProcess();
    public void updateOdFinalInProcess(OdFinalInspectionProcess odFinalInspectionProcess);
    public List<OdFinalInspectionProcess> feny(int pagesize, int rows);
    public int getcount();
}
