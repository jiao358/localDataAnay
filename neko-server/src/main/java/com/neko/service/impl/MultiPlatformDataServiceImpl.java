package com.neko.service.impl;

import com.neko.constant.PlatformTypeDict;
import com.neko.entity.MultiPlatformData;
import com.neko.mapper.MultiPlatformDataMapper;
import com.neko.service.MultiPlatformDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MultiPlatformDataServiceImpl implements MultiPlatformDataService {

    @Autowired
    private MultiPlatformDataMapper platformMapper;

    @Override
    public List<MultiPlatformData> listPlatforms(String accountName, String platformType) {
        return platformMapper.selectList(accountName, platformType);
    }

    @Override
    public MultiPlatformData getPlatformById(Long id) {
        return platformMapper.selectById(id);
    }

    @Override
    public void savePlatform(MultiPlatformData platform) {
        platformMapper.insert(platform);
    }

    @Override
    public void updatePlatform(MultiPlatformData platform) {
        platformMapper.update(platform);
    }

    @Override
    public void updatePlatformStatus(Long id, Integer status) {
        platformMapper.updateStatus(id, status);
    }

    @Override
    public Map<String, String> getPlatformTypes() {
        return PlatformTypeDict.getAllTypes();
    }

    @Override
    public void deletePlatform(Long id) {
        platformMapper.delete(id);
    }
} 