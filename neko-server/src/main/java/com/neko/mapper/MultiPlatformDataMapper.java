package com.neko.mapper;

import com.neko.entity.MultiPlatformData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MultiPlatformDataMapper {
    List<MultiPlatformData> selectList(
        @Param("accountName") String accountName,
        @Param("platformType") String platformType
    );
    MultiPlatformData selectById(@Param("id") Long id);
    MultiPlatformData selectByAccountName(@Param("accountName") String accountName);
    int insert(MultiPlatformData platform);
    int update(MultiPlatformData platform);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int delete(@Param("id") Long id);
} 