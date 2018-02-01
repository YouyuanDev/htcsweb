package com.htcsweb.dao;

import com.htcsweb.entity.OdCoatingInspectionProcess;

import java.util.List;

public interface OdCoatingInspectionProcessDao {
    public OdCoatingInspectionProcess getOdCoatInProcessById(int id);
    public void addOdCoatInProcess(OdCoatingInspectionProcess odCoatingInspectionProcess);
    public List<OdCoatingInspectionProcess> getOdCoatInProcess();
    public void updateOdCoatInProcess(OdCoatingInspectionProcess odCoatingInspectionProcess);
    public List<OdCoatingInspectionProcess> feny(int pagesize, int rows);
    public int getcount();
}
