package com.meeting.meeting.repository;

import com.meeting.meeting.model.dbo.Meeting;
import com.meeting.meeting.model.dbo.ResourceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author jie
 * @date 2019-11-03
 */
public interface MeetingRepository extends JpaRepository<Meeting, Integer>, JpaSpecificationExecutor {
    Meeting getMeetingsByCorIdAndId(Integer corId, Integer id);

    @Query(value = "select m.* from meeting m join meeting_resource_ship ship on m.id = ship.meeting_id where ship.resource_id=?1",nativeQuery = true)
    List<Meeting> getMeetingsByResourceInfosId(Integer resourceId);

}
