package com.meeting.meeting.config;

import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.util.CacheHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class ManagerLoginIntercept implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String authorization = request.getHeader("Authorization");
        Object data = CacheHelper.getData(authorization);
        if (data == null) {
            BaseResponse failure = BaseResponse.failure("登陆信息已过期，请重新登陆");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().println(failure.toString());
            return false;
        } else {
            return true;
        }
    }

}
