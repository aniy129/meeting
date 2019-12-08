package com.meeting.meeting.config;

import com.meeting.meeting.model.common.UserContext;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.model.dto.response.UserLoginResult;
import com.meeting.meeting.util.CacheHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserLoginIntercept implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("user")) {
            BaseResponse failure = BaseResponse.failure("未登陆或登陆错误");
            response.getWriter().println(failure.toString());
            return false;
        }
        Object data = CacheHelper.getData(authorization);
        if (data == null) {
            BaseResponse failure = BaseResponse.failure("登陆信息已过期，请重新登陆");
            response.getWriter().println(failure.toString());
            return false;
        } else {
            UserContext.setUser((UserLoginResult) data);
            return true;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUser();
    }
}
