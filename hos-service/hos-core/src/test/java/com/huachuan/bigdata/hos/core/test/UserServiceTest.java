package com.huachuan.bigdata.hos.core.test;

import com.huachuan.bigdata.hos.core.usermgr.IUserService;
import com.huachuan.bigdata.hos.core.usermgr.model.SystemRole;
import com.huachuan.bigdata.hos.core.usermgr.model.UserInfo;
import com.huachuan.bigdata.hos.mybatis.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
public class UserServiceTest extends BaseTest {

    @Autowired
    @Qualifier("userServiceImpl")
    IUserService userService;

    @Test
    public void addUser() {
        UserInfo userInfo = new UserInfo("jixin", "123456", SystemRole.ADMIN, "no desc");
        userService.addUser(userInfo);
    }

    @Test
    public void getUser() {
        UserInfo userInfo = userService.getUserInfoByName("jixin");
        System.out
                .println(
                        userInfo.getUserId() + "|" + userInfo.getUserName() + "|" + userInfo.getPassword());
    }

    @Test
    public void deleteUser() {
        UserInfo userInfo = userService.getUserInfoByName("jixin");
        userService.deleteUser(userInfo.getUserId());
    }
}

