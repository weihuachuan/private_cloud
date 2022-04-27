package com.huachuan.bigdata.hos.core.test;

import com.huachuan.bigdata.hos.core.authmgr.IAuthService;
import com.huachuan.bigdata.hos.core.authmgr.model.ServiceAuth;
import com.huachuan.bigdata.hos.core.authmgr.model.TokenInfo;
import com.huachuan.bigdata.hos.mybatis.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.List;

public class AuthServiceTest extends BaseTest {

    @Autowired
    @Qualifier("authServiceImpl")
    IAuthService authService;

    @Test
    public void addToken() {
        TokenInfo tokenInfo = new TokenInfo("jixin");
        authService.addToken(tokenInfo);
    }

    @Test
    public void refreshToken() {
        List<TokenInfo> tokenInfos = authService.getTokenInfos("jixin");
        tokenInfos.forEach(tokenInfo -> {
            authService.refreshToken(tokenInfo.getToken());
        });
    }

    @Test
    public void deleteToken() {
        List<TokenInfo> tokenInfos = authService.getTokenInfos("jixin");
        if (tokenInfos.size() > 0) {
            authService.deleteToken(tokenInfos.get(0).getToken());
        }
    }

    @Test
    public void addAuth() {
        List<TokenInfo> tokenInfos = authService.getTokenInfos("jixin");
        if (tokenInfos.size() > 0) {
            ServiceAuth serviceAuth = new ServiceAuth();
            serviceAuth.setAuthTime(new Date());
            serviceAuth.setBucketName("testBucket");
            serviceAuth.setTargetToken(tokenInfos.get(0).getToken());
            authService.addAuth(serviceAuth);
        }
    }

    @Test
    public void deleteAuth() {
        List<TokenInfo> tokenInfos = authService.getTokenInfos("jixin");
        if (tokenInfos.size() > 0) {
            authService.deleteAuth("test1", tokenInfos.get(0).getToken());
        }
    }

    @Test
    public void deleteAuthByBucket() {
        authService.deleteAuthByBucket("test1");
    }
}
