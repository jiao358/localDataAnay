package com.neko.service;

import com.neko.entity.SysUser;
import com.neko.mapper.SysUserMapper;
import com.neko.model.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private SysUserMapper userMapper;

    public LoginResponse login(String username, String password) {
        // 保留admin用户的快速登录
        if ("admin".equals(username) && "123456".equals(password)) {
            return LoginResponse.success(
                "mock-jwt-token-" + System.currentTimeMillis(),
                username,
                new String[]{"admin"}
            );
        }

        // 数据库用户验证
        SysUser user = userMapper.findByUsername(username);
        if (user == null) {
            return LoginResponse.error("用户不存在");
        }

        // TODO: 这里应该对密码进行加密后再比较
        if (!password.equals(user.getPassword())) {
            return LoginResponse.error("密码错误");
        }

        if (user.getStatus() != 1) {
            return LoginResponse.error("用户已被禁用");
        }

        return LoginResponse.success(
            "jwt-token-" + System.currentTimeMillis(),
            user.getUsername(),
            new String[]{"user"}  // 这里可以根据实际角色表来设置
        );
    }
} 