package com.meeting.meeting.controller.api.user;

import com.meeting.meeting.model.dbo.ResourceInfo;
import com.meeting.meeting.model.dto.request.AddResourceInfoRequest;
import com.meeting.meeting.model.dto.request.ResourceInfoRequest;
import com.meeting.meeting.model.dto.request.UpdateResourceInfoRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.service.ResourceInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController("UserResourceInfoController")
@Api(tags = "资源信息控制器(用户及企业端)")
@RequestMapping("/user/resourceInfo")
public class ResourceInfoController {
    @Resource
    private ResourceInfoService resourceInfoService;

    @GetMapping("/list")
    @ApiOperation(value = "资源信息列表")
    public BaseResponse<Page<ResourceInfo>> resourceInfoList(@Valid ResourceInfoRequest request) {
        Page<ResourceInfo> resourceList = resourceInfoService.getResourceInfoList(request);
        return BaseResponse.success(resourceList);
    }

    @GetMapping("/getResource")
    @ApiOperation(value = "获取资源信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "资源信息主键", required = true, dataType = "int", paramType = "query")
    })
    public BaseResponse<ResourceInfo> getResource(Integer id) {
        ResourceInfo resourceInfo = resourceInfoService.getResource(id);
        if (resourceInfo == null) {
            return BaseResponse.failure("资源不存在");
        }
        return BaseResponse.success(resourceInfo);
    }

}
