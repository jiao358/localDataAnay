package com.neko.mapper;

import com.neko.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysPermissionMapper {
    List<SysPermission> selectList(@Param("keyword") String keyword);
    SysPermission selectById(@Param("id") Long id);
    SysPermission selectByCode(@Param("permissionCode") String permissionCode);
    int insert(SysPermission permission);
    int update(SysPermission permission);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
} 