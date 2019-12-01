package com.meeting.meeting.controller.api.manager;

import com.meeting.meeting.model.dbo.Resource;
import com.meeting.meeting.model.dto.request.EditResourceRequest;
import com.meeting.meeting.model.dto.request.ResourceRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(tags = "资源类型管理控制器")
@RequestMapping("/manager/meeting")
public class MeetingResourceController {

    @javax.annotation.Resource
    private ResourceService resourceService;

    @GetMapping("/resourceList")
    @ApiOperation(value = "资源类型列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "int", paramType = "query")
    })
    public BaseResponse<Page<Resource>> resourceList(Integer pageSize, Integer pageIndex) {
        Page<Resource> resourceList = resourceService.getResourceList(pageSize, pageIndex);
        return BaseResponse.success(resourceList);
    }

    @PostMapping("/addResource")
    @ApiOperation(value = "添加资源类型")
    public BaseResponse<Resource> addResource(@RequestBody @Valid ResourceRequest resourceRequest) {
        Resource resource = resourceService.addResource(resourceRequest);
        return BaseResponse.success(resource);
    }

    @PostMapping("/editResource")
    @ApiOperation(value = "修改资源类型")
    public BaseResponse<Resource> editResource(@RequestBody @Valid EditResourceRequest editResourceRequest) {
        Resource rs = resourceService.getResource(editResourceRequest.getId());
        if (rs == null) {
            return BaseResponse.failure("资源类型不存在");
        }
        Resource resource = resourceService.editResource(editResourceRequest);
        return BaseResponse.success(resource);
    }

    @GetMapping("/getResource")
    @ApiOperation(value = "获取资源类型")
    public BaseResponse<Resource> getResource(Integer id) {
        Resource resource = resourceService.getResource(id);
        return BaseResponse.success(resource);
    }
}
