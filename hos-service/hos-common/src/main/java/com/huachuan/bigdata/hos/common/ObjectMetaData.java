package com.huachuan.bigdata.hos.common;

import java.util.Map;

public class ObjectMetaData {

    private String bucket;
    private String key;
    private String mediaType;
    private long length;
    private long lastModifyTime;
    private Map<String, String> attrs;

    public String getContentEncoding() {
        return attrs != null ? attrs.get("content-encoding") : null;
    }


    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }
}
