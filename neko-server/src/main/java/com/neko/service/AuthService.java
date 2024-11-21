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
            LoginResponse response = new LoginResponse();
            response.setCode(200);
            response.setMessage("登录成功");
            
            LoginResponse.Data data = new LoginResponse.Data();
            data.setToken("mock-jwt-token-" + System.currentTimeMillis());
            data.setUsername(username);
            data.setRoles(new String[]{"admin"});
            
            response.setData(data);
            return response;
        }
        throw new RuntimeException("用户名或密码错误");
    }

    public void logout() {
        // 这里可以添加清除用户会话的逻辑
        // 比如清除 token、清除缓存等
    }
} 