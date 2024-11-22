package com.neko.model;

import lombok.Data;

@Data
public class LoginResponse {
    private int code;
    private String message;
    private Data data;

    @lombok.Data
    public static class Data {
        private String token;
        private String username;
        private String[] roles;
    }
    

    public static LoginResponse success(String token, String username, String[] roles) {
        LoginResponse response = new LoginResponse();
        response.setCode(200);
        response.setMessage("登录成功");
        
        Data data = new Data();
        data.setToken(token);
        data.setUsername(username);
        data.setRoles(roles);
        
        response.setData(data);
        return response;
    }

    public static LoginResponse error(String message) {
        LoginResponse response = new LoginResponse();
        response.setCode(500);
        response.setMessage(message);
        return response;
    }
} 