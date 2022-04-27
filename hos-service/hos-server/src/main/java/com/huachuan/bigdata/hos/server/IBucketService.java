package com.huachuan.bigdata.hos.server;

import com.huachuan.bigdata.hos.common.BucketModel;
import com.huachuan.bigdata.hos.core.usermgr.model.UserInfo;

import java.util.List;

public interface IBucketService {

    public boolean addBucket(UserInfo userInfo, String bucketName, String detail);

    public boolean deleteBucket(String bucketName);

    public boolean updateBucket(String bucketName, String detail);

    public BucketModel getBucketById(String bucketId);

    public BucketModel getBucketByName(String bucketName);

    public List<BucketModel> getUserBuckets(String token);
}
