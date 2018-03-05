package com.htcsweb.dao;


import com.htcsweb.entity.LabTestingAcceptanceCriteria3Lpe;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface LabTestingAcceptanceCriteria3LpeDao {
    //    public LabTesting2Fbe getLabTest2FbeById(int id);
    public int addLabTestCriteria3Lpe(LabTestingAcceptanceCriteria3Lpe labTestingAcceptanceCriteria3Lpe);
    public int delLabTestCriteria3Lpe(String[] arrId);
    public List<LabTestingAcceptanceCriteria3Lpe> getAllLabTestingAcceptanceCriteria3Lpe();
    public int updateLabTestCriteria3Lpe(LabTestingAcceptanceCriteria3Lpe labTestingAcceptanceCriteria3Lpe);
    public LabTestingAcceptanceCriteria3Lpe getLabTestCriteria3LpeByContractNo(@Param("contract_no")String contract_no);
    public List<HashMap<String,Object>> getNewAllByLike(@Param("lab_testing_acceptance_criteria_no") String lab_testing_acceptance_criteria_no, @Param("skip") int skip, @Param("take") int take);
    public int getCountNewAllByLike(@Param("lab_testing_acceptance_criteria_no") String lab_testing_acceptance_criteria_no);
}
