package com.huachuan.bigdata.hos.common.util;

import java.util.UUID;

public class IdUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
