package com.neko.constant;

import java.util.HashMap;
import java.util.Map;

public class PlatformTypeDict {
    public static final String E_COMMERCE = "1";    // 电商
    public static final String BUSINESS = "2";      // 工商
    public static final String SOCIAL = "3";        // 社交
    public static final String CONTENT = "4";       // 内容

    private static final Map<String, String> PLATFORM_TYPE_MAP = new HashMap<>();

    static {
        PLATFORM_TYPE_MAP.put(E_COMMERCE, "电商平台");
        PLATFORM_TYPE_MAP.put(BUSINESS, "工商平台");
        PLATFORM_TYPE_MAP.put(SOCIAL, "社交平台");
        PLATFORM_TYPE_MAP.put(CONTENT, "内容平台");
    }

    public static String getTypeName(String typeCode) {
        return PLATFORM_TYPE_MAP.getOrDefault(typeCode, "未知类型");
    }

    public static Map<String, String> getAllTypes() {
        return new HashMap<>(PLATFORM_TYPE_MAP);
    }
} 