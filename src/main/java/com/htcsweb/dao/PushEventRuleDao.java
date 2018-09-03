package com.htcsweb.dao;


import com.htcsweb.entity.PushEventRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PushEventRuleDao {

    //更新推送事件
    public int updatePushEventRule(PushEventRule pushEventRule);

    //获取所有推送事件
    public List<PushEventRule> getAllPushEventRule();
   // public List<PushEventRule> getPushEventRolesByEvent(@Param("push_event")String push_event);
}
