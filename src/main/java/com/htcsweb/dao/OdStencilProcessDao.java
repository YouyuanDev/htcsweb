package com.htcsweb.dao;

import com.htcsweb.entity.OdStencilProcess;

import java.util.List;

public interface OdStencilProcessDao {
    public OdStencilProcess getOdStencilProcessById(int id);
    public void addOdStencilProcess(OdStencilProcess odStencilProcess);
    public List<OdStencilProcess> getOdStencilProcess();
    public void  updateOdStencilProcess(OdStencilProcess odStencilProcess);
    public List<OdStencilProcess>feny(int pagesize, int rows);
    public int getCount();
}
