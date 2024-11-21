package com.neko.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BasicErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        String message = (String) request.getAttribute("javax.servlet.error.message");
        String path = (String) request.getAttribute("javax.servlet.error.request_uri");

        errorDetails.put("path", path);
        errorDetails.put("status", statusCode != null ? statusCode : 500);
        errorDetails.put("error", HttpStatus.valueOf(statusCode != null ? statusCode : 500).getReasonPhrase());
        errorDetails.put("message", message != null ? message : (exception != null ? exception.getMessage() : "未知错误"));
        
        return ResponseEntity
                .status(statusCode != null ? statusCode : 500)
                .body(errorDetails);
    }
} 