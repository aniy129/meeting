package com.meeting.meeting.service;

import com.meeting.meeting.model.dto.request.LoginRequest;
import com.meeting.meeting.model.dto.response.ManagerLoginResult;

public interface ManagerService {
    ManagerLoginResult login(LoginRequest login);
}
