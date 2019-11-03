package com.meeting.meeting.service.impl;

import com.meeting.meeting.model.dbo.Manager;
import com.meeting.meeting.model.dto.request.ManagerLogin;
import com.meeting.meeting.model.dto.response.ManagerLoginResult;
import com.meeting.meeting.repository.ManagerRepository;
import com.meeting.meeting.service.ManagerService;
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
            result.setCode("1000");
            result.setMsg("管理员不存在");
        } else {
            if (manager.getPassword().equals(DigestUtils.md5DigestAsHex(login.getPassword().getBytes()))) {
                result.setAuth(DigestUtils.md5DigestAsHex(manager.getAccount().getBytes()));
                result.setCode("0000");
                result.setMsg("登陆成功");
            } else {
                result.setCode("1000");
                result.setMsg("密码错误");
            }
        }
        return result;
    }
}
