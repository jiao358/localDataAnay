package com.neko.service.impl;

import com.neko.entity.SysPermission;
import com.neko.mapper.SysPermissionMapper;
import com.neko.mapper.SysRolePermissionMapper;
import com.neko.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    private SysPermissionMapper permissionMapper;

    @Autowired
    private SysRolePermissionMapper rolePermissionMapper;

    @Override
    public List<SysPermission> listPermissions(String keyword) {
        return permissionMapper.selectList(keyword);
    }

    @Override
    public SysPermission getPermissionById(Long id) {
        return permissionMapper.selectById(id);
    }

    @Override
    public void savePermission(SysPermission permission) {
        permissionMapper.insert(permission);
    }

    @Override
    public void updatePermission(SysPermission permission) {
        permissionMapper.update(permission);
    }

    @Override
    public void updatePermissionStatus(Long id, Integer status) {
        permissionMapper.updateStatus(id, status);
    }

    @Override
    public List<SysPermission> getPermissionsByRoleId(Long roleId) {
        return rolePermissionMapper.selectPermissionsByRoleId(roleId);
    }

    @Override
    @Transactional
    public void updateRolePermissions(Long roleId, List<Long> permissionIds) {
        rolePermissionMapper.deleteByRoleId(roleId);
        if (permissionIds != null && !permissionIds.isEmpty()) {
            rolePermissionMapper.batchInsert(roleId, permissionIds);
        }
    }
} 