package com.meeting.meeting.controller.api.manager;

import com.meeting.meeting.model.dbo.Corporation;
import com.meeting.meeting.model.dto.request.CorporationListRequest;
import com.meeting.meeting.model.dto.request.ManagerEditCorporationRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.service.CorporationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "企业管理控制器(平台端)")
@RequestMapping("/manager/corporation")
@RestController("ManagerCorporationController")
public class CorporationController {

    @Resource
    private CorporationService corporationService;

    @GetMapping("/list")
    @ApiOperation("企业列表")
    public BaseResponse<Page<Corporation>> list(CorporationListRequest request) {
        Page<Corporation> list = corporationService.list(request);
        return BaseResponse.success(list);
    }

    @GetMapping("/get")
    @ApiOperation("获取企业")
    public BaseResponse<Corporation> get(Integer id) {
        Corporation corporation = corporationService.findById(id);
        return BaseResponse.success(corporation);
    }

    @PostMapping("/edit")
    @ApiOperation("编辑企业")
    public BaseResponse edit(@RequestBody @Valid ManagerEditCorporationRequest request) {
        return corporationService.edit(request);
    }
}
