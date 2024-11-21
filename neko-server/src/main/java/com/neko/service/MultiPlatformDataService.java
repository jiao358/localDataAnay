package com.neko.service;

import com.neko.entity.MultiPlatformData;
import java.util.List;
import java.util.Map;

public interface MultiPlatformDataService {
    List<MultiPlatformData> listPlatforms(String accountName, String platformType);
    MultiPlatformData getPlatformById(Long id);
    void savePlatform(MultiPlatformData platform);
    void updatePlatform(MultiPlatformData platform);
    void updatePlatformStatus(Long id, Integer status);
    Map<String, String> getPlatformTypes();
    void deletePlatform(Long id);
} 