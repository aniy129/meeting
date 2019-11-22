package com.meeting.meeting.service;

import com.meeting.meeting.model.dbo.Resource;
import com.meeting.meeting.model.dto.request.EditResourceRequest;
import com.meeting.meeting.model.dto.request.ResourceRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ResourceService {
    Page<Resource> getResourceList(Integer pageSize,Integer pageIndex);

    Resource addResource(ResourceRequest resourceRequest);

    Resource editResource(EditResourceRequest editResourceRequest);

    Resource getResource(Integer data);
}
