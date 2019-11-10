package com.meeting.meeting.service;

import com.meeting.meeting.model.dto.request.LoginRequest;
import com.meeting.meeting.model.dto.response.UserLoginResult;

public interface UserService {
    UserLoginResult login(LoginRequest login);
}
