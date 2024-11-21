package com.neko.service;

import com.neko.entity.SysRole;
import com.neko.entity.SysUser;
import java.util.List;

public interface SysUserService {
    List<SysUser> listUsers(String keyword);
    SysUser getUserById(Long id);
    void saveUser(SysUser user);
    void updateUser(SysUser user);
    void updateUserStatus(Long id, Integer status);
    List<SysRole> getUserRoles(Long userId);
    void updateUserRoles(Long userId, List<Long> roleIds);
} 