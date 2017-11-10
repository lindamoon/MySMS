package com.lixb.mysms.biz.user;

/**
 * Created by Administrator on 2017/11/10.
 */

public class CurrentUser {
    private Long userId;
    private String userName;
    private static CurrentUser sCurrentUser;
    private CurrentUser() {
        userId = -0xFFl;
        userName = "unknown";
    }

    public static CurrentUser getInstance() {
        synchronized (CurrentUser.class) {
            if (null == sCurrentUser) {
                synchronized (CurrentUser.class) {
                    return new CurrentUser();
                }
            }
        }
        return sCurrentUser;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
