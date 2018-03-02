package com.htcsweb.dao;


import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface LabTestingAcceptanceCriteria2FbeDao {
    //    public LabTesting2Fbe getLabTest2FbeById(int id);
    public int addLabTestCriteria2Fbe(LabTestingAcceptanceCriteria2FbeDao labTestingAcceptanceCriteria2FbeDao);
    public int delLabTestCriteria2Fbe(String[] arrId);
    //public List<LabTestingAcceptanceCriteria2FbeDao> getLabTestCriteria2Fbe();
    public int updateLabTestCriteria2Fbe(LabTestingAcceptanceCriteria2FbeDao labTestingAcceptanceCriteria2FbeDao);
    public LabTestingAcceptanceCriteria2FbeDao getLabTestCriteria2FbeByContractNo(@Param("contract_no")String contract_no);
    public List<HashMap<String,Object>> getNewAllByLike(@Param("lab_testing_acceptance_criteria_no") String lab_testing_acceptance_criteria_no, @Param("skip") int skip, @Param("take") int take);
    public int getCountNewAllByLike(@Param("lab_testing_acceptance_criteria_no") String lab_testing_acceptance_criteria_no);
}

