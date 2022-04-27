package com.huachuan.bigdata.hos.server.test;



import com.huachuan.bigdata.hos.common.BucketModel;
import com.huachuan.bigdata.hos.core.authmgr.IAuthService;
import com.huachuan.bigdata.hos.core.authmgr.model.ServiceAuth;
import com.huachuan.bigdata.hos.core.usermgr.IUserService;
import com.huachuan.bigdata.hos.core.usermgr.model.SystemRole;
import com.huachuan.bigdata.hos.core.usermgr.model.UserInfo;
import com.huachuan.bigdata.hos.mybatis.test.BaseTest;
import com.huachuan.bigdata.hos.server.IBucketService;
import com.huachuan.bigdata.hos.server.dao.BucketMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class BucketMapperTest extends BaseTest {

    @Autowired
    BucketMapper bucketMapper;

    @Autowired
    @Qualifier("bucketServiceImpl")
    IBucketService bucketService;
    @Autowired
    @Qualifier("authServiceImpl")
    IAuthService authService;

    @Autowired
    @Qualifier("userServiceImpl")
    IUserService userService;

    @Test
    public void addBucket() {
        BucketModel bucketModel = new BucketModel("test1", "jixin", "");
        bucketMapper.addBucket(bucketModel);
        UserInfo userInfo = new UserInfo("jixin", "123456", SystemRole.ADMIN, "");
        userService.addUser(userInfo);
        ServiceAuth serviceAuth = new ServiceAuth();
        serviceAuth.setTargetToken(userInfo.getUserId());
        serviceAuth.setBucketName(bucketModel.getBucketName());
        authService.addAuth(serviceAuth);
    }

    @Test
    public void getBucket() {
        BucketModel bucketModel = bucketMapper.getBucketByName("test1");
        System.out.println(bucketModel.getBucketId() + "|" + bucketModel.getBucketName());
    }

    @Test
    public void getUserAuthorizedBuckets() {
        UserInfo userInfo = userService.getUserInfoByName("jixin");
        List<BucketModel> bucketModels = bucketMapper.getUserAuthorizedBuckets(userInfo.getUserId());
        bucketModels.forEach(bucketModel -> {
            System.out.println(bucketModel.getBucketId() + "|" + bucketModel.getBucketName());
        });
    }

    @Test
    public void deleteBucket() {
        UserInfo userInfo = userService.getUserInfoByName("jixin");
        List<BucketModel> bucketModels = bucketMapper.getUserAuthorizedBuckets(userInfo.getUserId());
        bucketModels.forEach(bucketModel -> {
            bucketService.deleteBucket(bucketModel.getBucketName());
        });
    }
}
