package com.meeting.meeting.service;

import com.meeting.meeting.model.dbo.Manager;
import com.meeting.meeting.model.dto.request.AddManagerRequest;
import com.meeting.meeting.model.dto.request.EditManagerRequest;
import com.meeting.meeting.model.dto.request.LoginRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.model.dto.response.ManagerLoginResult;

import java.util.List;

public interface ManagerService {
    ManagerLoginResult login(LoginRequest login);

    List<Manager> list();

    BaseResponse add(AddManagerRequest request);

    BaseResponse edit(EditManagerRequest request);

    BaseResponse<Manager> get(Integer id);
}
