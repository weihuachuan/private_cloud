package com.huachuan.bigdata.hos.web;

import com.huachuan.bigdata.hos.core.usermgr.CoreUtil;
import com.huachuan.bigdata.hos.core.usermgr.IUserService;
import com.huachuan.bigdata.hos.core.usermgr.model.SystemRole;
import com.huachuan.bigdata.hos.core.usermgr.model.UserInfo;
import com.huachuan.bigdata.hos.server.IHosStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInitialization implements ApplicationRunner {

    @Autowired
    @Qualifier("hosStoreService")
    IHosStoreService hosStoreService;

    @Autowired
    @Qualifier("userServiceImpl")
    IUserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        UserInfo userInfo = userService.getUserInfoByName(CoreUtil.SYSTEM_USER);
        if (userInfo == null) {
            UserInfo userInfo1 = new UserInfo(CoreUtil.SYSTEM_USER, "superadmin", SystemRole.SUPERADMIN,
                    "this is superadmin");
            userService.addUser(userInfo1);
        }
        hosStoreService.createSeqTable();
    }
}
