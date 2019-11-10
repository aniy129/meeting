package com.meeting.meeting.repository;

import com.meeting.meeting.model.dbo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jie
 * @date 2019-11-03
 */
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor {
    User getUserByNickname(String nickname);
}
