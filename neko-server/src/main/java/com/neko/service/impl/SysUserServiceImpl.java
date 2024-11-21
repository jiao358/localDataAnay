package com.neko.service.impl;

import com.neko.entity.SysRole;
import com.neko.entity.SysUser;
import com.neko.mapper.SysUserMapper;
import com.neko.mapper.SysUserRoleMapper;
import com.neko.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Override
    public List<SysUser> listUsers(String keyword) {
        return userMapper.selectList(keyword);
    }

    @Override
    public SysUser getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public void saveUser(SysUser user) {
        userMapper.insert(user);
    }

    @Override
    public void updateUser(SysUser user) {
        userMapper.update(user);
    }

    @Override
    public void updateUserStatus(Long id, Integer status) {
        userMapper.updateStatus(id, status);
    }

    @Override
    public List<SysRole> getUserRoles(Long userId) {
        return userRoleMapper.selectRolesByUserId(userId);
    }

    @Override
    @Transactional
    public void updateUserRoles(Long userId, List<Long> roleIds) {
        // 先删除用户原有角色
        userRoleMapper.deleteByUserId(userId);
        // 如果有新角色，则批量插入
        if (roleIds != null && !roleIds.isEmpty()) {
            userRoleMapper.batchInsert(userId, roleIds);
        }
    }
} 