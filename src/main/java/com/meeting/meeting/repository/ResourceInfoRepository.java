package com.meeting.meeting.repository;

import com.meeting.meeting.model.dbo.ResourceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author jie
* @date 2019-11-03
*/
public interface ResourceInfoRepository extends JpaRepository<ResourceInfo, Integer>, JpaSpecificationExecutor {
}
