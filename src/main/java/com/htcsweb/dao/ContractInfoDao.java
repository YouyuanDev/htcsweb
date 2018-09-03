package com.htcsweb.dao;

import com.htcsweb.entity.ContractInfo;
import org.apache.ibatis.annotations.Param;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public interface ContractInfoDao {

    //添加合同信息
    public int addContractInfo(ContractInfo contractInfo);

    //删除合同信息
    public int delContractInfo(String[]arrId);

    //更新合同信息
    public int updateContractInfo(ContractInfo contractInfo);

    //分页查询合同信息
    public List<HashMap<String,Object>> getAllByLike(@Param("project_no") String project_no, @Param("project_name")String project_name, @Param("contract_no")String contract_no, @Param("skip")int skip, @Param("take")int take);

    //查询合同信息总数
    public int getCountAllByLike(@Param("project_no") String project_no, @Param("project_name")String project_name, @Param("contract_no")String contract_no);

    //根据合同编号查询合同信息
    public List<ContractInfo> getContractInfoByContractNo(@Param("contract_no")String contract_no);

    //根据项目编号获取所属的所有合同信息
    public List<ContractInfo>getAllContractInfoByProjectNo(@Param("project_no")String project_no);

    //根据管号获得合同信息
    public ContractInfo getContractInfoByPipeNo(@Param("pipe_no")String pipe_no);
}
