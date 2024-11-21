package com.neko.service.impl;

import com.neko.entity.SysDictType;
import com.neko.entity.SysDictData;
import com.neko.mapper.SysDictTypeMapper;
import com.neko.mapper.SysDictDataMapper;
import com.neko.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictTypeMapper dictTypeMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;

    @Override
    public List<SysDictType> listDictTypes(String dictName) {
        return dictTypeMapper.selectList(dictName);
    }

    @Override
    public SysDictType getDictTypeById(Long id) {
        return dictTypeMapper.selectById(id);
    }

    @Override
    public void saveDictType(SysDictType dictType) {
        dictTypeMapper.insert(dictType);
    }

    @Override
    public void updateDictType(SysDictType dictType) {
        dictTypeMapper.update(dictType);
    }

    @Override
    public void updateDictTypeStatus(Long id, Integer status) {
        dictTypeMapper.updateStatus(id, status);
    }

    @Override
    public List<SysDictData> listDictData(String dictCode) {
        return dictDataMapper.selectList(dictCode);
    }

    @Override
    public SysDictData getDictDataById(Long id) {
        return dictDataMapper.selectById(id);
    }

    @Override
    public void saveDictData(SysDictData dictData) {
        dictDataMapper.insert(dictData);
    }

    @Override
    public void updateDictData(SysDictData dictData) {
        dictDataMapper.update(dictData);
    }

    @Override
    public void updateDictDataStatus(Long id, Integer status) {
        dictDataMapper.updateStatus(id, status);
    }

    @Override
    public Map<String, String> getDictDataMap(String dictCode) {
        List<SysDictData> dictDataList = dictDataMapper.selectList(dictCode);
        return dictDataList.stream()
            .collect(Collectors.toMap(
                SysDictData::getDictValue,
                SysDictData::getDictLabel,
                (v1, v2) -> v2
            ));
    }
} 