package com.meeting.meeting.service;

import com.meeting.meeting.model.dbo.Meeting;
import com.meeting.meeting.model.dto.request.AddMeetingRequest;
import com.meeting.meeting.model.dto.request.AuditRequest;
import com.meeting.meeting.model.dto.request.QueryMeetingRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import org.springframework.data.domain.Page;

public interface MeetingService {
    BaseResponse addMeeting(AddMeetingRequest request);

    BaseResponse<Page<Meeting>> list(QueryMeetingRequest queryMeetingRequest);

    BaseResponse<Meeting> get(Integer id);

    BaseResponse<Page<Meeting>> listForManager(QueryMeetingRequest queryMeetingRequest);

    BaseResponse audit(AuditRequest request);
}
