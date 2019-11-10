package com.meeting.meeting.service.impl;

import com.meeting.meeting.model.dbo.Manager;
import com.meeting.meeting.model.dto.request.ManagerLogin;
import com.meeting.meeting.model.dto.response.ManagerLoginResult;
import com.meeting.meeting.repository.ManagerRepository;
import com.meeting.meeting.service.ManagerService;
import com.meeting.meeting.util.CacheHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

@Service
public class ManagerServiceImpl implements ManagerService {
    @Resource
    private ManagerRepository managerRepository;

    @Override
    public ManagerLoginResult login(ManagerLogin login) {
        ManagerLoginResult result = new ManagerLoginResult();
        Manager manager = managerRepository.getManagerByAccount(login.getUserName());
        if (manager == null) {
            result.setStatus("400");
            result.setMessage("管理员不存在");
        } else {
            if (manager.getPassword().equals(DigestUtils.md5DigestAsHex(login.getPassword().getBytes()))) {
                result.setAuth(DigestUtils.md5DigestAsHex(manager.getAccount().getBytes()));
                result.setStatus("200");
                result.setMessage("登陆成功");
                String authorization = DigestUtils.md5DigestAsHex(("manager" + manager.getAccount() + manager.getPassword() + manager.getId()).getBytes());
                result.setAuth(authorization);
                CacheHelper.setData(authorization, manager);
            } else {
                result.setStatus("400");
                result.setMessage("密码错误");
            }
        }
        return result;
    }
}
