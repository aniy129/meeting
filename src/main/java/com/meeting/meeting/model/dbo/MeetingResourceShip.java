package com.meeting.meeting.model.dbo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "meeting_resource_ship")
@IdClass(MeetingResourceShipIds.class)
public class MeetingResourceShip implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Integer meetingId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Integer resourceId;
}
