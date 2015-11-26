package com.ledboot.wegirls.event;

import com.avos.avoscloud.AVUser;

/**
 * Created by Administrator on 2015/11/24 0024.
 */
public class LoginEvent {
    private AVUser user;

    public LoginEvent(AVUser user){
        this.user = user;
    }

    public AVUser getUser() {
        return user;
    }

    public void setUser(AVUser user) {
        this.user = user;
    }
}
