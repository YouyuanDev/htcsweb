package com.htcsweb.dao;

import com.htcsweb.entity.OdBlastInspectionProcess;

import java.util.List;

public interface OdBlastInspectionProcessDao {
    public OdBlastInspectionProcess getOdBlastInProcessById(int id);
    public void addOdBlastInProcess(OdBlastInspectionProcess odBlastInspectionProcess);
    public List<OdBlastInspectionProcess> getOdBlastInProcess();
    public void  updateOdBlastInProcess(OdBlastInspectionProcess odBlastInspectionProcess);
    public List<OdBlastInspectionProcess>feny(int pagesize, int rows);
    public int getCount();
}
