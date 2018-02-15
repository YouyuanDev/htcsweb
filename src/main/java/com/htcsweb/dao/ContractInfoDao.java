package com.htcsweb.dao;

import com.htcsweb.entity.ContractInfo;
import org.apache.ibatis.annotations.Param;


import java.util.HashMap;
import java.util.List;

public interface ContractInfoDao {

    public List<HashMap<String,Object>> getAllByLike(@Param("project_no") String project_no, @Param("project_name")String project_name, @Param("contract_no")String contract_no, @Param("skip")int skip, @Param("take")int take);
    public int getCount();
    public int updateContractInfo(ContractInfo contractInfo);
    public int addContractInfo(ContractInfo contractInfo);
    public int delContractInfo(String[]arrId);
    public List<ContractInfo> getContractInfoByContractNo(@Param("contract_no")String contract_no);


}
