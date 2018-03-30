package com.htcsweb.dao;

import com.htcsweb.entity.PipeBodyAcceptanceCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface PipeBodyAcceptanceCriteriaDao {

    public List<PipeBodyAcceptanceCriteria> getAllPipeBodyAcceptanceCriteria();
    public int addPipeBodyAcceptanceCriteria(PipeBodyAcceptanceCriteria pipeBodyAcceptanceCriteria);
    public int delPipeBodyAcceptanceCriteria(String[] arrId);
    public int updatePipeBodyAcceptanceCriteria(PipeBodyAcceptanceCriteria pipeBodyAcceptanceCriteria);
    public List<HashMap<String,Object>> getAllByLike(@Param("pipe_body_acceptance_criteria_no") String pipe_body_acceptance_criteria_no, @Param("skip") int skip, @Param("take") int take);
    public int getCount(@Param("pipe_body_acceptance_criteria_no") String pipe_body_acceptance_criteria_no);
    public PipeBodyAcceptanceCriteria getPipeBodyAcceptanceCriteriaByContractNo(@Param("contract_no")String contract_no);

}
