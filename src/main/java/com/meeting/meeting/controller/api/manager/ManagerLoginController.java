package com.meeting.meeting.controller.api.manager;

import com.meeting.meeting.model.dto.request.LoginRequest;
import com.meeting.meeting.model.dto.response.ManagerLoginResult;
import com.meeting.meeting.service.ManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "平台管理员登陆控制器")
@RequestMapping("/manager")
public class ManagerLoginController {

    @Resource
    private ManagerService managerService;

    @PostMapping("/login")
    @ApiOperation(value = "登陆", response = ManagerLoginResult.class)
    public ManagerLoginResult login(LoginRequest login) {
        return managerService.login(login);
    }
}
