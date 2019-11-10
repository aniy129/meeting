package com.meeting.meeting.service;

import com.meeting.meeting.model.dbo.Resource;
import com.meeting.meeting.model.dto.request.EditResourceRequest;
import com.meeting.meeting.model.dto.request.ResourceRequest;

import java.util.List;

public interface ResourceService {
    List<Resource> getResourceList();

    Resource addResource(ResourceRequest resourceRequest);

    Resource editResource(EditResourceRequest editResourceRequest);

    Resource getResource(Integer data);
}
