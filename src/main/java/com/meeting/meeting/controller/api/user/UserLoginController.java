package com.meeting.meeting.controller.api.user;

import com.meeting.meeting.model.dto.request.LoginRequest;
import com.meeting.meeting.model.dto.response.UserLoginResult;
import com.meeting.meeting.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "企业或会员登陆控制器")
@RequestMapping("/user")
public class UserLoginController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    @ApiOperation(value = "登陆")
    public UserLoginResult login(@RequestBody LoginRequest login) {
        return userService.login(login);
    }
}
