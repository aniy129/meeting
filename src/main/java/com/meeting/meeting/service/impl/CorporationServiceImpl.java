package com.meeting.meeting.service.impl;


import com.meeting.meeting.model.common.UserContext;
import com.meeting.meeting.model.dbo.Corporation;
import com.meeting.meeting.model.dbo.Meeting;
import com.meeting.meeting.model.dbo.User;
import com.meeting.meeting.model.dto.request.CorporationListRequest;
import com.meeting.meeting.model.dto.request.EditCorporationRequest;
import com.meeting.meeting.model.dto.request.EnterpriseRegisterRequest;
import com.meeting.meeting.model.dto.request.ManagerEditCorporationRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.model.dto.response.UserLoginResult;
import com.meeting.meeting.repository.CorporationRepository;
import com.meeting.meeting.repository.MeetingRepository;
import com.meeting.meeting.repository.UserRepository;
import com.meeting.meeting.service.CorporationService;
import com.meeting.meeting.util.StringUtils;
import com.meeting.meeting.util.ValidationUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

/**
 * @author jie
 * @date 2019-11-03
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CorporationServiceImpl implements CorporationService {

    @Autowired
    private CorporationRepository corporationRepository;

    @Resource
    private UserRepository userRepository;
    @Resource
    private MeetingRepository meetingRepository;

    @Override
    public Corporation findById(Integer id) {
        Optional<Corporation> corporation = corporationRepository.findById(id);
        ValidationUtil.isNull(corporation, "Corporation", "id", id);
        return corporation.orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Corporation create(Corporation resources) {
        return corporationRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Corporation resources) {
        Optional<Corporation> optionalCorporation = corporationRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalCorporation, "Corporation", "id", resources.getId());

        Corporation corporation = optionalCorporation.get();
        // 此处需自己修改
        resources.setId(corporation.getId());
        corporationRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        corporationRepository.deleteById(id);
        Meeting mt = new Meeting();
        mt.setCorId(id);
        meetingRepository.delete(mt);	//删除企业下的会议
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse register(EnterpriseRegisterRequest request) {
        User account = userRepository.getUserByNickname(request.getNickname());
        if (account != null) {
            return BaseResponse.failure("用户账号已存在!");
        }
        Corporation corporation = corporationRepository.getCorporationByName(request.getName());
        if (corporation != null) {
            return BaseResponse.failure("企业已存在!");
        }
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setIdentity(0);
        Corporation cp = new Corporation();
        BeanUtils.copyProperties(request, cp);
        cp.setName(request.getEnterpriseName());
        cp.setRegTime(new Timestamp(new Date().getTime()));
        cp = corporationRepository.saveAndFlush(cp);
        user.setCorId(cp.getId());
        userRepository.save(user);
        return BaseResponse.success(null);
    }

    @Override
    public BaseResponse editCorporation(EditCorporationRequest request) {
        UserLoginResult user = UserContext.getUser();
        if (user.getIdentity().equals(0)) {
            Corporation corporationByName = corporationRepository.getCorporationByName(request.getName());
            if (corporationByName != null && !corporationByName.getName().equals(user.getCorporation().getName())) {
                return BaseResponse.failure("企业名称已存在!");
            } else {
                BeanUtils.copyProperties(request, user.getCorporation());
                corporationRepository.save(user.getCorporation());
                return BaseResponse.success(null);
            }
        } else {
            return BaseResponse.failure("非企业管理员不能修改!");
        }
    }

    @Override
    public Page<Corporation> list(CorporationListRequest request) {
        PageRequest page = PageRequest.of(request.getPageIndex() - 1, request.getPageSize());
        Corporation query = new Corporation();
        if (StringUtils.isNotBlank(request.getName())) {
            query.setName(request.getName());
        }
        if (StringUtils.isNoneBlank(request.getPhone())) {
            query.setPhone(request.getPhone());
        }
        if (StringUtils.isNoneBlank(request.getManager())) {
            query.setManager(request.getManager());
        }
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("phone", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("manager",ExampleMatcher.GenericPropertyMatchers.contains());
        Example<Corporation> example = Example.of(query, matcher);
        return corporationRepository.findAll(example, page);
    }

    @Override
    public BaseResponse edit(ManagerEditCorporationRequest request) {
        Corporation corporation = findById(request.getId());
        if (corporation == null) {
            return BaseResponse.failure("企业不存在");
        }
        Corporation corporationByName = corporationRepository.getCorporationByName(request.getName());
        if (corporationByName != null && !corporationByName.getName().equals(request.getName())) {
            return BaseResponse.failure("企业名称已存在!");
        }
        BeanUtils.copyProperties(request, corporation);
        corporationRepository.save(corporation);
        return BaseResponse.success(null);
    }
}
