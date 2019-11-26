package com.meeting.meeting.service;

import com.meeting.meeting.model.dbo.ResourceInfo;
import com.meeting.meeting.model.dto.request.AddResourceInfoRequest;
import com.meeting.meeting.model.dto.request.ResourceInfoRequest;
import com.meeting.meeting.model.dto.request.UpdateResourceInfoRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import org.springframework.data.domain.Page;

public interface ResourceInfoService {

    Page<ResourceInfo> getResourceInfoList(ResourceInfoRequest request);

    BaseResponse addResourceInfo(AddResourceInfoRequest request);

    BaseResponse editResourceInfo(UpdateResourceInfoRequest request);

    ResourceInfo getResource(Integer id);
}
