package com.meeting.meeting.service.impl;

import com.meeting.meeting.model.dbo.ResourceInfo;
import com.meeting.meeting.model.dto.request.AddResourceInfoRequest;
import com.meeting.meeting.model.dto.request.ResourceInfoRequest;
import com.meeting.meeting.model.dto.request.UpdateResourceInfoRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.repository.ResourceInfoRepository;
import com.meeting.meeting.repository.ResourceRepository;
import com.meeting.meeting.service.ResourceInfoService;
import com.meeting.meeting.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ResourceInfoServiceImpl implements ResourceInfoService {

    @Resource
    private ResourceInfoRepository resourceInfoRepository;

    @Resource
    private ResourceRepository resourceRepository;

    @Override
    public Page<ResourceInfo> getResourceInfoList(ResourceInfoRequest request) {
        PageRequest page = PageRequest.of(request.getPageIndex() - 1, request.getPageSize());
        ResourceInfo query = new ResourceInfo();
        if (StringUtils.isNotBlank(request.getManager())) {
            query.setManager(request.getManager());
        }
        if (StringUtils.isNotBlank(request.getName())) {
            query.setName(request.getName());
        }
        if (request.getState() != null) {
            query.setState(request.getState());
        }
        if (request.getType() != null) {
            query.setType(request.getType());
        }
        ExampleMatcher exampleMatcher=ExampleMatcher.matching()
                .withMatcher("manager",ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("name",ExampleMatcher.GenericPropertyMatchers.contains());
        Example<ResourceInfo> example = Example.of(query,exampleMatcher);
        return resourceInfoRepository.findAll(example, page);
    }

    @Override
    public BaseResponse addResourceInfo(AddResourceInfoRequest request) {
        com.meeting.meeting.model.dbo.Resource resourceType = resourceRepository.findById(request.getResId()).orElse(null);
        if (resourceType == null) {
            return BaseResponse.failure("资源类型不存在！");
        }
        ResourceInfo resourceInfo = new ResourceInfo();
        BeanUtils.copyProperties(request, resourceInfo);
        resourceInfo.setType(Integer.parseInt(resourceType.getType()));
        resourceInfo.setState(1);
        resourceInfoRepository.save(resourceInfo);
        return BaseResponse.success(null);
    }

    @Override
    public BaseResponse editResourceInfo(UpdateResourceInfoRequest request) {
        com.meeting.meeting.model.dbo.Resource resourceType = resourceRepository.findById(request.getResId()).orElse(null);
        if (resourceType == null) {
            return BaseResponse.failure("资源类型不存在！");
        }
        ResourceInfo resourceInfo = resourceInfoRepository.findById(request.getId()).orElse(null);
        if (resourceInfo == null) {
            return BaseResponse.failure("资源信息不存在！");
        }
        BeanUtils.copyProperties(request, resourceInfo);
        resourceInfo.setType(Integer.parseInt(resourceType.getType()));
        resourceInfoRepository.save(resourceInfo);
        return BaseResponse.success(null);
    }

    @Override
    public ResourceInfo getResource(Integer id) {
        return resourceInfoRepository.findById(id).orElse(null);
    }
}
