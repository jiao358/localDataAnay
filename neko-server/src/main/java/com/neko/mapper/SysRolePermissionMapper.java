package com.neko.mapper;

import com.neko.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysRolePermissionMapper {
    List<SysPermission> selectPermissionsByRoleId(@Param("roleId") Long roleId);
    int deleteByRoleId(@Param("roleId") Long roleId);
    int batchInsert(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);
} 