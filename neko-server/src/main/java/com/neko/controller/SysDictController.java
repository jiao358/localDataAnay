package com.neko.controller;

import com.neko.entity.SysDictType;
import com.neko.entity.SysDictData;
import com.neko.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dict")
public class SysDictController {
    
    @Autowired
    private SysDictService dictService;

    // 字典类型相关接口
    @GetMapping("/types")
    public ResponseEntity<?> listDictTypes(@RequestParam(required = false) String dictName) {
        try {
            List<SysDictType> types = dictService.listDictTypes(dictName);
            Map<String, Object> response = new HashMap<>();
            response.put("records", types);
            response.put("total", types.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/types/{id}")
    public ResponseEntity<?> getDictTypeById(@PathVariable Long id) {
        try {
            SysDictType type = dictService.getDictTypeById(id);
            return ResponseEntity.ok(type);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/types")
    public ResponseEntity<?> saveDictType(@RequestBody SysDictType dictType) {
        try {
            dictService.saveDictType(dictType);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/types/{id}")
    public ResponseEntity<?> updateDictType(@PathVariable Long id, @RequestBody SysDictType dictType) {
        try {
            dictType.setId(id);
            dictService.updateDictType(dictType);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/types/{id}/status")
    public ResponseEntity<?> updateDictTypeStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            dictService.updateDictTypeStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 字典数据相关接口
    @GetMapping("/data")
    public ResponseEntity<?> listDictData(@RequestParam String dictCode) {
        try {
            List<SysDictData> data = dictService.listDictData(dictCode);
            Map<String, Object> response = new HashMap<>();
            response.put("records", data);
            response.put("total", data.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/data/map")
    public ResponseEntity<?> getDictDataMap(@RequestParam String dictCode) {
        try {
            Map<String, String> dataMap = dictService.getDictDataMap(dictCode);
            return ResponseEntity.ok(dataMap);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/data/{id}")
    public ResponseEntity<?> getDictDataById(@PathVariable Long id) {
        try {
            SysDictData data = dictService.getDictDataById(id);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/data")
    public ResponseEntity<?> saveDictData(@RequestBody SysDictData dictData) {
        try {
            dictService.saveDictData(dictData);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/data/{id}")
    public ResponseEntity<?> updateDictData(@PathVariable Long id, @RequestBody SysDictData dictData) {
        try {
            dictData.setId(id);
            dictService.updateDictData(dictData);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/data/{id}/status")
    public ResponseEntity<?> updateDictDataStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            dictService.updateDictDataStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
} 