package com.meeting.meeting.model.dbo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class MeetingResourceShipIds implements Serializable {
    @Column(name = "meeting_id")
    private Integer meetingId;

    @Column(name = "resource_id")
    private Integer resourceId;
}
