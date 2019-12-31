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

    @Query(value = "select m.* from meeting m join meeting_resource_ship ship on m.id = ship.meeting_id where ship.resource_id=?1", nativeQuery = true)
    List<Meeting> getMeetingsByResourceInfosId(Integer resourceId);

    @Query(value = "select m.*\n" +
            "from meeting m\n" +
            "where m.audit = 1\n" +
            "  and m.start_time < now()\n" +
            "  and m.start_time > date_add(now(), interval -3 month)", nativeQuery = true)
    List<Meeting> getMeetingsForStarting();

    @Query(value = "select m.*\n" +
            "from meeting m\n" +
            "where m.audit = 1\n" +
            "  and m.end_time < now()\n" +
            "  and m.state = 0", nativeQuery = true)
    List<Meeting> getMeetingsForEnd();
}
