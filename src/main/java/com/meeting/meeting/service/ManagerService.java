package com.meeting.meeting.service;

import com.meeting.meeting.model.dto.request.ManagerLogin;
import com.meeting.meeting.model.dto.response.ManagerLoginResult;

public interface ManagerService {
    ManagerLoginResult login(ManagerLogin login);
}
