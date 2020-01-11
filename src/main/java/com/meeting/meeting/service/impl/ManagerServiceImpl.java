package com.meeting.meeting.service.impl;

import com.meeting.meeting.model.dbo.Manager;
import com.meeting.meeting.model.dto.request.AddManagerRequest;
import com.meeting.meeting.model.dto.request.EditManagerRequest;
import com.meeting.meeting.model.dto.request.LoginRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.model.dto.response.ManagerLoginResult;
import com.meeting.meeting.repository.ManagerRepository;
import com.meeting.meeting.service.ManagerService;
import com.meeting.meeting.util.CacheHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class ManagerServiceImpl implements ManagerService {
    @Resource
    private ManagerRepository managerRepository;

    @Override
    public ManagerLoginResult login(LoginRequest login) {
        ManagerLoginResult result = new ManagerLoginResult();
        Manager manager = managerRepository.getManagerByAccount(login.getUserName());
        if (manager == null) {
            result.setStatus("400");
            result.setMessage("管理员不存在");
        } else {
            if (manager.getAvailable() == null || manager.getAvailable().equals(false)) {
                result.setStatus("400");
                result.setMessage("管理员已禁用");
                return result;
            }
            if (manager.getPassword().equals(DigestUtils.md5DigestAsHex(login.getPassword().getBytes()))) {
                result.setAuth(DigestUtils.md5DigestAsHex(manager.getAccount().getBytes()));
                result.setStatus("200");
                result.setMessage("登陆成功");
                String authorization = "manager" + UUID.randomUUID().toString();
                result.setAuth(authorization);
                result.setName(manager.getName());
                result.setAccount(manager.getAccount());
                CacheHelper.setData(authorization, manager);
            } else {
                result.setStatus("400");
                result.setMessage("密码错误");
            }
        }
        return result;
    }

    @Override
    public List<Manager> list() {
        List<Manager> list = managerRepository.findAll();
        list.forEach(manager -> manager.setPassword(null));
        return list;
    }

    @Override
    public BaseResponse add(AddManagerRequest request) {
        Manager account = managerRepository.getManagerByAccount(request.getAccount());
        if (account != null) {
            return BaseResponse.failure("账号已存在");
        }
        account = managerRepository.getManagerByCode(request.getCode());
        if (account != null) {
            return BaseResponse.failure("管理员编号已存在");
        }
        Manager manager = new Manager();
        BeanUtils.copyProperties(request, manager);
        manager.setPassword(DigestUtils.md5DigestAsHex(manager.getPassword().getBytes()));
        managerRepository.save(manager);
        return BaseResponse.success(null);
    }

    @Override
    public BaseResponse edit(EditManagerRequest request) {
        Manager manager = managerRepository.findById(request.getId()).orElse(null);
        if (manager == null) {
            return BaseResponse.failure("主键不存在");
        }
        BeanUtils.copyProperties(request, manager);
        Manager account = managerRepository.getManagerByAccount(request.getAccount());
        if (account != null && !account.getAccount().equals(request.getAccount())) {
            return BaseResponse.failure("账号已存在");
        }
        account = managerRepository.getManagerByCode(request.getCode());
        if (account != null && !account.getCode().equals(request.getCode())) {
            return BaseResponse.failure("管理员编号已存在");
        }
        managerRepository.save(manager);
        return BaseResponse.success(null);
    }

    @Override
    public BaseResponse<Manager> get(Integer id) {
        Manager manager = managerRepository.findById(id).orElse(null);
        if (manager == null) {
            return BaseResponse.failure("管理员编号不存在");
        }
        manager.setPassword(null);
        return BaseResponse.success(manager);
    }

    @Override
    public BaseResponse resetPassword(Integer id) {
        Manager manager = managerRepository.findById(id).orElse(null);
        if (manager == null) {
            return BaseResponse.failure("管理员编号不存在");
        }
        manager.setPassword("123456");
        managerRepository.save(manager);
        return BaseResponse.success(null);
    }
}
