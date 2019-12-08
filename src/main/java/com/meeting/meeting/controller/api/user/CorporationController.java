package com.meeting.meeting.controller.api.user;

import com.meeting.meeting.model.dto.request.EditCorporationRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.repository.CorporationRepository;
import com.meeting.meeting.service.CorporationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "企业管理控制器(用户端)")
@RequestMapping("/user/corporation")
@RestController("UserCorporationController")
public class CorporationController {

    @Resource
    private CorporationService corporationService;

    @PostMapping("/edit")
    @ApiOperation("编辑当前企业信息")
    public BaseResponse editCorporation(@RequestBody @Valid EditCorporationRequest request) {
        return corporationService.editCorporation(request);
    }
}
