package com.htcsweb.dao;


import org.apache.ibatis.annotations.Param;
import com.htcsweb.entity.LabTestingAcceptanceCriteria2Fbe;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface LabTestingAcceptanceCriteria2FbeDao {
    //    public LabTesting2Fbe getLabTest2FbeById(int id);
    public int addLabTestCriteria2Fbe(LabTestingAcceptanceCriteria2Fbe labTestingAcceptanceCriteria2Fbe);
    public int delLabTestCriteria2Fbe(String[] arrId);
    public List<LabTestingAcceptanceCriteria2Fbe> getAllLabTestingAcceptanceCriteria2Fbe();
    public int updateLabTestCriteria2Fbe(LabTestingAcceptanceCriteria2Fbe labTestingAcceptanceCriteria2Fbe);
    public LabTestingAcceptanceCriteria2Fbe getLabTestCriteria2FbeByContractNo(@Param("contract_no")String contract_no);

    public LabTestingAcceptanceCriteria2Fbe getLabTestCriteria2FbeByPipeNo(@Param("pipe_no")String pipe_no);



    public List<HashMap<String,Object>> getNewAllByLike(@Param("lab_testing_acceptance_criteria_no") String lab_testing_acceptance_criteria_no, @Param("skip") int skip, @Param("take") int take);
    public int getCountNewAllByLike(@Param("lab_testing_acceptance_criteria_no") String lab_testing_acceptance_criteria_no);
}

