package com.huachuan.bigdata.hos.server.dao;

import com.huachuan.bigdata.hos.common.BucketModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;

import java.util.List;

@Mapper
public interface BucketMapper {

    void addBucket(@Param("bucket") BucketModel bucketModel);

    void updateBucket(@Param("bucketName") String bucketName, @Param("detail") String detail);

    void deleteBucket(@Param("bucketName") String bucketName);

    @ResultMap("BucketResultMap")
    BucketModel getBucket(@Param("bucketId") String bucketId);

    @ResultMap("BucketResultMap")
    BucketModel getBucketByName(@Param("bucketName") String bucketName);

    @ResultMap("BucketResultMap")
    List<BucketModel> getBucketByCreator(@Param("creator") String creator);

    @ResultMap("BucketResultMap")
    List<BucketModel> getUserAuthorizedBuckets(@Param("token") String token);
}
