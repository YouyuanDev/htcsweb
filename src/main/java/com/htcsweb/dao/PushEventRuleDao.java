package com.htcsweb.dao;


import com.htcsweb.entity.PushEventRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PushEventRuleDao {

    public int updatePushEventRule(PushEventRule pushEventRule);
    public List<PushEventRule> getAllPushEventRule();
   // public List<PushEventRule> getPushEventRolesByEvent(@Param("push_event")String push_event);
}
