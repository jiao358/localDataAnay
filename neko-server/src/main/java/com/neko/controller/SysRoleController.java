package com.neko.controller;

import com.neko.entity.SysRole;
import com.neko.entity.SysPermission;
import com.neko.service.SysRoleService;
import com.neko.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
public class SysRoleController {
    
    @Autowired
    private SysRoleService roleService;
    
    @Autowired
    private SysPermissionService permissionService;

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(required = false) String keyword) {
        try {
            List<SysRole> roles = roleService.listRoles(keyword);
            Map<String, Object> response = new HashMap<>();
            response.put("records", roles);
            response.put("total", roles.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            SysRole role = roleService.getRoleById(id);
            return ResponseEntity.ok(role);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody SysRole role) {
        try {
            roleService.saveRole(role);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SysRole role) {
        try {
            role.setId(id);
            roleService.updateRole(role);
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
            roleService.updateRoleStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}/permissions")
    public ResponseEntity<?> getRolePermissions(@PathVariable Long id) {
        try {
            List<SysPermission> permissions = permissionService.getPermissionsByRoleId(id);
            return ResponseEntity.ok(permissions);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}/permissions")
    public ResponseEntity<?> updateRolePermissions(
        @PathVariable Long id,
        @RequestBody Map<String, List<Long>> request
    ) {
        try {
            List<Long> permissionIds = request.get("permissionIds");
            permissionService.updateRolePermissions(id, permissionIds);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
} 