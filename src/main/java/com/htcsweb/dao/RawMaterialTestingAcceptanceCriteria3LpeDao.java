package com.htcsweb.dao;

import org.apache.ibatis.annotations.Param;
import com.htcsweb.entity.RawMaterialTestingAcceptanceCriteria3Lpe;
import java.util.HashMap;
import java.util.List;

public interface RawMaterialTestingAcceptanceCriteria3LpeDao {

    public int addRawMaterialTestingAcceptanceCriteria3Lpe(RawMaterialTestingAcceptanceCriteria3Lpe rawMaterialTestingAcceptanceCriteria3Lpe);
    public int delRawMaterialTestingAcceptanceCriteria3Lpe(String[] arrId);
    public List<RawMaterialTestingAcceptanceCriteria3Lpe> getAllRawMaterialTestingAcceptanceCriteria3Lpe();
    public int updateRawMaterialTestingAcceptanceCriteria3Lpe(RawMaterialTestingAcceptanceCriteria3Lpe rawMaterialTestingAcceptanceCriteria3Lpe);
    public RawMaterialTestingAcceptanceCriteria3Lpe getRawMaterialTestingAcceptanceCriteria3LpeByProjectNo(@Param("project_no")String project_no);
    public List<HashMap<String,Object>> getAllByLike(@Param("raw_material_testing_acceptance_criteria_no") String raw_material_testing_acceptance_criteria_no, @Param("skip") int skip, @Param("take") int take);
    public int getCountAllByLike(@Param("raw_material_testing_acceptance_criteria_no") String raw_material_testing_acceptance_criteria_no);

    public RawMaterialTestingAcceptanceCriteria3Lpe getRawMaterialStandard3LpeByPipeNo(@Param("pipe_no")String pipe_no);
    public RawMaterialTestingAcceptanceCriteria3Lpe getRawMaterialStandard3LpeByProjectNo(@Param("project_no")String project_no);

}
