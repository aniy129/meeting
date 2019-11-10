package com.meeting.meeting.controller.api.manager;

import com.meeting.meeting.model.dbo.Resource;
import com.meeting.meeting.model.dto.request.EditResourceRequest;
import com.meeting.meeting.model.dto.request.ResourceRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "会议资源管理控制器")
@RequestMapping("/manager/meeting")
public class MeetingResourceController {

    @javax.annotation.Resource
    private ResourceService resourceService;

    @PostMapping("/resourceList")
    @ApiOperation(value = "资源类型列表")
    public BaseResponse<List<Resource>> resourceList() {
        List<Resource> resource = resourceService.getResourceList();
        return BaseResponse.success(resource);
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

    @PostMapping("/getResource")
    @ApiOperation(value = "获取资源类型")
    public BaseResponse<Resource> getResource(Integer id) {
        Resource resource = resourceService.getResource(id);
        return BaseResponse.success(resource);
    }
}
