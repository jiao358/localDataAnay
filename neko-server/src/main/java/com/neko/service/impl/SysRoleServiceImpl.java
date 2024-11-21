package com.neko.service.impl;

import com.neko.entity.SysRole;
import com.neko.mapper.SysRoleMapper;
import com.neko.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Override
    public List<SysRole> listRoles(String keyword) {
        return roleMapper.selectList(keyword);
    }

    @Override
    public SysRole getRoleById(Long id) {
        return roleMapper.selectById(id);
    }

    @Override
    public void saveRole(SysRole role) {
        roleMapper.insert(role);
    }

    @Override
    public void updateRole(SysRole role) {
        roleMapper.update(role);
    }

    @Override
    public void updateRoleStatus(Long id, Integer status) {
        roleMapper.updateStatus(id, status);
    }
} 