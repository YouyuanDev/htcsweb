package com.htcsweb.dao;

import com.htcsweb.entity.OdCoatingProcess;

import java.util.List;

public interface OdCoatingProcessDao {
    public OdCoatingProcess getOdCoatProcessById(int id);
    public void addOdCoatProcess(OdCoatingProcess odCoatingProcess);
    public List<OdCoatingProcess> getOdCoatProcess();
    public void updateOdCoatProcess(OdCoatingProcess odCoatingProcess);
    public List<OdCoatingProcess> feny(int pagesize, int rows);
    public int getcount();
}
