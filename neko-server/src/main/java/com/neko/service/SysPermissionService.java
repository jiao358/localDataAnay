package com.neko.service;

import com.neko.entity.SysPermission;
import java.util.List;

public interface SysPermissionService {
    List<SysPermission> listPermissions(String keyword);
    SysPermission getPermissionById(Long id);
    void savePermission(SysPermission permission);
    void updatePermission(SysPermission permission);
    void updatePermissionStatus(Long id, Integer status);
    List<SysPermission> getPermissionsByRoleId(Long roleId);
    void updateRolePermissions(Long roleId, List<Long> permissionIds);
} 