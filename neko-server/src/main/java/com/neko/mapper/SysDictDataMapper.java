package com.neko.mapper;

import com.neko.entity.SysDictData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysDictDataMapper {
    List<SysDictData> selectList(@Param("dictCode") String dictCode);
    SysDictData selectById(@Param("id") Long id);
    int insert(SysDictData dictData);
    int update(SysDictData dictData);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
} 