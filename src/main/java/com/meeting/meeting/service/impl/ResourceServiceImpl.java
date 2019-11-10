package com.meeting.meeting.service.impl;

import com.meeting.meeting.model.dbo.Resource;
import com.meeting.meeting.model.dto.request.EditResourceRequest;
import com.meeting.meeting.model.dto.request.ResourceRequest;
import com.meeting.meeting.repository.ResourceRepository;
import com.meeting.meeting.service.ResourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    @javax.annotation.Resource
    private ResourceRepository resourceRepository;

    @Override
    public List<Resource> getResourceList() {
        return resourceRepository.findAll();
    }

    @Override
    public Resource addResource(ResourceRequest resourceRequest) {
        Resource resource = new Resource();
        resource.setIntro(resourceRequest.getIntro());
        resource.setType(resourceRequest.getType());
        return resourceRepository.saveAndFlush(resource);
    }

    @Override
    public Resource editResource(EditResourceRequest editResourceRequest) {
        Resource resource = new Resource();
        BeanUtils.copyProperties(editResourceRequest, resource);
        return resourceRepository.saveAndFlush(resource);
    }

    @Override
    public Resource getResource(Integer data) {
        return resourceRepository.findById(data).orElse(null);
    }
}
