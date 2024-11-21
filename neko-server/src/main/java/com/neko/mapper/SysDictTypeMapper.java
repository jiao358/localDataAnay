package com.neko.mapper;

import com.neko.entity.SysDictType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysDictTypeMapper {
    List<SysDictType> selectList(@Param("dictName") String dictName);
    SysDictType selectById(@Param("id") Long id);
    SysDictType selectByCode(@Param("dictCode") String dictCode);
    int insert(SysDictType dictType);
    int update(SysDictType dictType);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
} 