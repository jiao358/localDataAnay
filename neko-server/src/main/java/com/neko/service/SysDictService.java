package com.neko.service;

import com.neko.entity.SysDictType;
import com.neko.entity.SysDictData;
import java.util.List;
import java.util.Map;

public interface SysDictService {
    // 字典类型相关
    List<SysDictType> listDictTypes(String dictName);
    SysDictType getDictTypeById(Long id);
    void saveDictType(SysDictType dictType);
    void updateDictType(SysDictType dictType);
    void updateDictTypeStatus(Long id, Integer status);

    // 字典数据相关
    List<SysDictData> listDictData(String dictCode);
    SysDictData getDictDataById(Long id);
    void saveDictData(SysDictData dictData);
    void updateDictData(SysDictData dictData);
    void updateDictDataStatus(Long id, Integer status);
    
    // 获取字典数据Map
    Map<String, String> getDictDataMap(String dictCode);
} 