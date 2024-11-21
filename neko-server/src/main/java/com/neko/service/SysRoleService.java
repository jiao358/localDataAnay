package com.neko.service;

import com.neko.entity.SysRole;
import java.util.List;

public interface SysRoleService {
    List<SysRole> listRoles(String keyword);
    SysRole getRoleById(Long id);
    void saveRole(SysRole role);
    void updateRole(SysRole role);
    void updateRoleStatus(Long id, Integer status);
} 