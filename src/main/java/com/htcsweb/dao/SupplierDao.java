package com.htcsweb.dao;

import com.htcsweb.entity.Supplier;

import java.util.List;

public interface SupplierDao {

    public Supplier getSupplierById(int Supplier_id);
    public void addSupplier(Supplier supplier);
    public List<Supplier> getSupplier();
    public void updateSupplier(Supplier supplier);
    public List<Supplier> feny(int pagesize, int rows);
    public int getcount();
}
