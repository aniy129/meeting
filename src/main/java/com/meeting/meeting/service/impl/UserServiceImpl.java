package com.meeting.meeting.service.impl;

import com.meeting.meeting.model.dbo.User;
import com.meeting.meeting.model.dto.request.LoginRequest;
import com.meeting.meeting.model.dto.response.UserLoginResult;
import com.meeting.meeting.repository.UserRepository;
import com.meeting.meeting.service.UserService;
import com.meeting.meeting.util.CacheHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public UserLoginResult login(LoginRequest login) {
        UserLoginResult result = new UserLoginResult();
        User user = userRepository.getUserByNickname(login.getUserName());
        if (user == null) {
            result.setStatus("400");
            result.setMessage("用户名或密码错误");
        } else {
            if (user.getPassword().equals(DigestUtils.md5DigestAsHex(login.getPassword().getBytes()))) {
                result.setStatus("200");
                result.setMessage("登陆成功");
                BeanUtils.copyProperties(user, result);
                String authorization = "user" + UUID.randomUUID().toString();
                result.setAuth(authorization);
                CacheHelper.setData(authorization, user);
            } else {
                result.setStatus("400");
                result.setMessage("用户名或密码错误");
            }
        }
        return result;
    }
}
