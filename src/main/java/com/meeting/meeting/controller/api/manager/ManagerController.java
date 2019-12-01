package com.meeting.meeting.controller.api.manager;

import com.meeting.meeting.model.dbo.Manager;
import com.meeting.meeting.model.dto.request.AddManagerRequest;
import com.meeting.meeting.model.dto.request.EditManagerRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.service.ManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "平台管理员控制器")
@RequestMapping("/manager")
public class ManagerController {

    @Resource
    private ManagerService managerService;

    @GetMapping("/list")
    @ApiOperation(value = "管理员列表")
    public BaseResponse<List<Manager>> list() {
        List<Manager> list = managerService.list();
        return BaseResponse.success(list);
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加管理员")
    public BaseResponse add(@RequestBody @Valid AddManagerRequest request) {
        return managerService.add(request);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "修改管理员")
    public BaseResponse edit(@RequestBody @Valid EditManagerRequest manager) {
        return managerService.edit(manager);
    }

    @GetMapping("/get")
    @ApiOperation(value = "获取管理员")
    public BaseResponse<Manager> get(Integer id) {
        return managerService.get(id);
    }
}
