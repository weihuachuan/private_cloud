package com.huachuan.bigdata.hos.core.usermgr;

import com.google.common.base.Strings;
import com.huachuan.bigdata.hos.core.authmgr.dao.TokenInfoMapper;
import com.huachuan.bigdata.hos.core.authmgr.model.TokenInfo;
import com.huachuan.bigdata.hos.core.usermgr.dao.UserInfoMapper;
import com.huachuan.bigdata.hos.core.usermgr.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Service("userServiceImpl")
public class UserServiceImpl implements IUserService {

    //set expireTime is better
    private long LONG_REFRESH_TIME = 4670409600000L;
    private int LONG_EXPIRE_TIME = 36500;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    TokenInfoMapper tokenInfoMapper;

    @Override
    public boolean addUser(UserInfo userInfo) {
        userInfoMapper.addUser(userInfo);
        //todo add token
        Date date = new Date();
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setToken(userInfo.getUserId());
        tokenInfo.setActive(true);
        tokenInfo.setCreateTime(date);
        tokenInfo.setCreator(CoreUtil.SYSTEM_USER);
        tokenInfo.setExpireTime(LONG_EXPIRE_TIME);
        tokenInfo.setRefreshTime(date);
        tokenInfoMapper.addToken(tokenInfo);
        return true;
    }

    @Override
    public boolean updateUserInfo(String userId, String password, String detail) {
        userInfoMapper
                .updateUserInfo(userId,
                        Strings.isNullOrEmpty(password) ? null : CoreUtil.getMd5Password(password),
                        Strings.emptyToNull(detail));
        return true;
    }

    @Override
    public boolean deleteUser(String userId) {
        userInfoMapper.deleteUser(userId);
        //todo delete token
        tokenInfoMapper.deleteToken(userId);
        return true;
    }

    @Override
    public UserInfo getUserInfo(String userId) {
        return userInfoMapper.getUserInfo(userId);
    }

    @Override
    public UserInfo checkPassword(String userName, String password) {
        return userInfoMapper.checkPassword(userName, password);
    }

    @Override
    public UserInfo getUserInfoByName(String userName) {
        return userInfoMapper.getUserInfoByName(userName);
    }
}

