package com.htcsweb.dao;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import com.htcsweb.entity.RawMaterialTestingAcceptanceCriteria2Fbe;
import org.springframework.stereotype.Repository;



public interface RawMaterialTestingAcceptanceCriteria2FbeDao {

    public int addRawMaterialTestingAcceptanceCriteria2Fbe(RawMaterialTestingAcceptanceCriteria2Fbe rawMaterialTestingAcceptanceCriteria2Fbe);
    public int delRawMaterialTestingAcceptanceCriteria2Fbe(String[] arrId);
    public List<RawMaterialTestingAcceptanceCriteria2Fbe> getAllRawMaterialTestingAcceptanceCriteria2Fbe();
    public int updateRawMaterialTestingAcceptanceCriteria2Fbe(RawMaterialTestingAcceptanceCriteria2Fbe rawMaterialTestingAcceptanceCriteria2Fbe);
    public RawMaterialTestingAcceptanceCriteria2Fbe getRawMaterialTestingAcceptanceCriteria2FbeByContractNo(@Param("contract_no")String contract_no);
    public List<HashMap<String,Object>> getAllByLike(@Param("raw_material_testing_acceptance_criteria_no") String raw_material_testing_acceptance_criteria_no, @Param("skip") int skip, @Param("take") int take);
    public int getCountAllByLike(@Param("raw_material_testing_acceptance_criteria_no") String raw_material_testing_acceptance_criteria_no);
}


