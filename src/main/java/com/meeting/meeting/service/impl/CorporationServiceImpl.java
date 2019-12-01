package com.meeting.meeting.service.impl;


import com.meeting.meeting.model.dbo.Corporation;
import com.meeting.meeting.model.dbo.User;
import com.meeting.meeting.model.dto.request.EnterpriseRegisterRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.repository.CorporationRepository;
import com.meeting.meeting.repository.UserRepository;
import com.meeting.meeting.service.CorporationService;
import com.meeting.meeting.util.ValidationUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
}
