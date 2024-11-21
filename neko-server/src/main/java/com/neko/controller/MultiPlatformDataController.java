package com.neko.controller;

import com.neko.entity.MultiPlatformData;
import com.neko.service.MultiPlatformDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/platforms")
public class MultiPlatformDataController {
    
    @Autowired
    private MultiPlatformDataService platformService;

    @GetMapping
    public ResponseEntity<?> list(
        @RequestParam(required = false) String accountName,
        @RequestParam(required = false) String platformType
    ) {
        try {
            List<MultiPlatformData> platforms = platformService.listPlatforms(accountName, platformType);
            Map<String, Object> response = new HashMap<>();
            response.put("records", platforms);
            response.put("total", platforms.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/types")
    public ResponseEntity<?> getPlatformTypes() {
        try {
            Map<String, String> types = platformService.getPlatformTypes();
            return ResponseEntity.ok(types);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            MultiPlatformData platform = platformService.getPlatformById(id);
            return ResponseEntity.ok(platform);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody MultiPlatformData platform) {
        try {
            platformService.savePlatform(platform);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody MultiPlatformData platform) {
        try {
            platform.setId(id);
            platformService.updatePlatform(platform);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            platformService.updatePlatformStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            platformService.deletePlatform(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
} 