package com.neko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neko.entity.MultiPlatformData;
import com.neko.entity.SysDictData;
import com.neko.mapper.MultiPlatformDataMapper;
import com.neko.service.MultiPlatformDataService;
import com.neko.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MultiPlatformDataServiceImpl implements MultiPlatformDataService {

    @Autowired
    private MultiPlatformDataMapper platformMapper;

    @Autowired
    private SysDictService dictService;

    @Override
    public IPage<MultiPlatformData> listPlatforms(Page<MultiPlatformData> page, String accountName, String platformType, String securityInfo) {
        LambdaQueryWrapper<MultiPlatformData> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(accountName)) {
            queryWrapper.like(MultiPlatformData::getAccountName, accountName);
        }
        if (StringUtils.hasText(platformType)) {
            queryWrapper.eq(MultiPlatformData::getPlatformType, platformType);
        }
        if (StringUtils.hasText(securityInfo)) {
            queryWrapper.like(MultiPlatformData::getSecurityInfo, securityInfo);
        }
        queryWrapper.orderByDesc(MultiPlatformData::getCreateTime);
        return platformMapper.selectPage(page, queryWrapper);
    }

    @Override
    public MultiPlatformData getPlatformById(Long id) {
        return platformMapper.selectById(id);
    }

    @Override
    @Transactional
    public void savePlatform(MultiPlatformData platform) {
        platform.setStatus(1);
        platformMapper.insert(platform);
    }

    @Override
    @Transactional
    public void updatePlatform(MultiPlatformData platform) {
        platformMapper.updateById(platform);
    }

    @Override
    @Transactional
    public void updatePlatformStatus(Long id, Integer status) {
        MultiPlatformData platform = new MultiPlatformData();
        platform.setId(id);
        platform.setStatus(status);
        platformMapper.updateById(platform);
    }

    @Override
    public Map<String, String> getPlatformTypes() {
        // 获取平台类型字典数据
        List<SysDictData> dictList = dictService.listDictData("account_platform");
        return dictList.stream()
            .filter(dict -> dict.getStatus() == 1)  // 只返回启用状态的字典项
            .collect(Collectors.toMap(
                SysDictData::getDictValue,
                SysDictData::getDictLabel,
                (existing, replacement) -> existing  // 处理重复键
            ));
    }

    @Override
    @Transactional
    public void deletePlatform(Long id) {
        platformMapper.deleteById(id);
    }
} 