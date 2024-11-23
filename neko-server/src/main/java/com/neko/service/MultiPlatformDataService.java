package com.neko.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neko.entity.MultiPlatformData;
import java.util.List;
import java.util.Map;

public interface MultiPlatformDataService {
    IPage<MultiPlatformData> listPlatforms(Page<MultiPlatformData> page, String accountName, String platformType, String securityInfo);
    MultiPlatformData getPlatformById(Long id);
    void savePlatform(MultiPlatformData platform);
    void updatePlatform(MultiPlatformData platform);
    void updatePlatformStatus(Long id, Integer status);
    Map<String, String> getPlatformTypes();
    void deletePlatform(Long id);
} 