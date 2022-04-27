package com.huachuan.bigdata.hos.web.security;

import com.huachuan.bigdata.hos.common.BucketModel;
import com.huachuan.bigdata.hos.core.authmgr.IAuthService;
import com.huachuan.bigdata.hos.core.authmgr.model.ServiceAuth;
import com.huachuan.bigdata.hos.core.authmgr.model.TokenInfo;
import com.huachuan.bigdata.hos.core.usermgr.CoreUtil;
import com.huachuan.bigdata.hos.core.usermgr.IUserService;
import com.huachuan.bigdata.hos.core.usermgr.model.SystemRole;
import com.huachuan.bigdata.hos.core.usermgr.model.UserInfo;
import com.huachuan.bigdata.hos.server.IBucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("DefaultAccessControl")
public class DefaultOperationAccessControl implements IOperationAccessControl {

    @Autowired
    @Qualifier("authServiceImpl")
    IAuthService authService;

    @Autowired
    @Qualifier("userServiceImpl")
    IUserService userService;

    @Autowired
    @Qualifier("bucketServiceImpl")
    IBucketService bucketService;

    @Override
    public UserInfo checkLogin(String userName, String password) {
        UserInfo userInfo = userService.getUserInfoByName(userName);
        if (userInfo == null) {
            return null;
        } else {
            return userInfo.getPassword().equals(CoreUtil.getMd5Password(password)) ? userInfo : null;
        }

    }

    @Override
    public boolean checkSystemRole(SystemRole systemRole1, SystemRole systemRole2) {
        if (systemRole1.equals(SystemRole.SUPERADMIN)) {
            return true;
        }
        return systemRole1.equals(SystemRole.ADMIN) && systemRole2.equals(SystemRole.USER);
    }

    @Override
    public boolean checkTokenOwner(String userName, String token) {
        TokenInfo tokenInfo = authService.getTokenInfo(token);
        return tokenInfo.getCreator().equals(userName);
    }

    @Override
    public boolean checkSystemRole(SystemRole systemRole1, String userId) {
        if (systemRole1.equals(SystemRole.SUPERADMIN)) {
            return true;
        }
        UserInfo userInfo = userService.getUserInfo(userId);
        return systemRole1.equals(SystemRole.ADMIN) && userInfo.getSystemRole().equals(SystemRole.USER);
    }

    @Override
    public boolean checkBucketOwner(String userName, String bucketName) {
        BucketModel bucketModel = bucketService.getBucketByName(bucketName);
        return bucketModel.getCreator().equals(userName);
    }

    @Override
    public boolean checkPermission(String token, String bucket) {
        if (authService.checkToken(token)) {
            ServiceAuth serviceAuth = authService.getServiceAuth(bucket, token);
            if (serviceAuth != null) {
                return true;
            }
        }
        return false;
    }
}
