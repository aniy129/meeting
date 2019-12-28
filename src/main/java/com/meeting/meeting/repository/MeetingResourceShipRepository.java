package com.meeting.meeting.repository;

import com.meeting.meeting.model.dbo.MeetingResourceShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MeetingResourceShipRepository extends JpaRepository<MeetingResourceShip, Integer>, JpaSpecificationExecutor {
}
