package com.meeting.meeting.repository;

import com.meeting.meeting.model.dbo.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author jie
* @date 2019-11-03
*/
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer>, JpaSpecificationExecutor {
}
