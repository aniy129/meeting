package com.meeting.meeting.model.common;

import com.meeting.meeting.model.dto.response.UserLoginResult;

public class UserContext {
    private static ThreadLocal<UserLoginResult> userLocal = new ThreadLocal<UserLoginResult>();

    public static UserLoginResult getUser() {
        return userLocal.get();
    }

    public static void setUser(UserLoginResult user) {
        userLocal.set(user);
    }

    public static void removeUser() {
        userLocal.remove();
    }
}
