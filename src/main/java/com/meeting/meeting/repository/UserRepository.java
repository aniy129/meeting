package com.meeting.meeting.repository;

import com.meeting.meeting.model.dbo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author jie
 * @date 2019-11-03
 */
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor {
    User getUserByNickname(String nickname);

    @Query(value = "select u.* from meeting m  join user_meeting_ship ship on ship.id=m.id join user u on ship.use_id = u.id where u.identity=1 and m.id=?1", nativeQuery = true)
    List<User> getUsersByMeetingId(Integer id);
}
