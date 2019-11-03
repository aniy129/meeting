package com.meeting.meeting.model.dbo;

import javax.persistence.Column;
import java.io.Serializable;

public class UserMeetingShipIds implements Serializable {
    @Column(name = "use_id")
    private Integer useId;

    /**
     * 主键
     */
    @Column(name = "id")
    private Integer id;
}
