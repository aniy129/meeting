package com.meeting.meeting.repository;

import com.meeting.meeting.model.dbo.ResourceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author jie
 * @date 2019-11-03
 */
public interface ResourceInfoRepository extends JpaRepository<ResourceInfo, Integer>, JpaSpecificationExecutor {
    @Query(value = "select r.* from resource_info r join meeting_resource_ship ship on r.id = ship.resource_id where ship.meeting_id=?1 limit 10", nativeQuery = true)
    List<ResourceInfo> getResourcesByMeetingId(Integer id);
}
