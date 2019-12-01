package com.meeting.meeting.controller.api.user;

import com.meeting.meeting.model.dto.request.EnterpriseRegisterRequest;
import com.meeting.meeting.model.dto.request.UserRegisterRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.service.CorporationService;
import com.meeting.meeting.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Api(tags = "企业管理员或会员注册控制器")
@RequestMapping
public class RegisterController {
    @Resource
    private UserService userService;

    @Resource
    private CorporationService corporationService;

    @PostMapping("/users/register")
    @ApiOperation("用户注册")
    public BaseResponse userRegister(@Valid @RequestBody UserRegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/enterprise/register")
    @ApiOperation("企业注册")
    public BaseResponse enterpriseRegister(@Valid @RequestBody EnterpriseRegisterRequest request) {
        return corporationService.register(request);
    }
}
