package com.meeting.meeting.repository;

import com.meeting.meeting.model.dbo.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author jie
* @date 2019-11-03
*/
public interface ResourceRepository extends JpaRepository<Resource, Integer>, JpaSpecificationExecutor {
}
