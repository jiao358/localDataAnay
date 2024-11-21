package com.neko.controller;

import com.neko.entity.KnowledgeData;
import com.neko.service.KnowledgeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeDataController {
    
    @Autowired
    private KnowledgeDataService knowledgeService;

    @GetMapping
    public ResponseEntity<?> list(
        @RequestParam(required = false) String contentName,
        @RequestParam(required = false) String contentCategory
    ) {
        try {
            List<KnowledgeData> knowledge = knowledgeService.listKnowledge(contentName, contentCategory);
            Map<String, Object> response = new HashMap<>();
            response.put("records", knowledge);
            response.put("total", knowledge.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            KnowledgeData knowledge = knowledgeService.getKnowledgeById(id);
            return ResponseEntity.ok(knowledge);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody KnowledgeData knowledge) {
        try {
            knowledgeService.saveKnowledge(knowledge);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody KnowledgeData knowledge) {
        try {
            knowledge.setId(id);
            knowledgeService.updateKnowledge(knowledge);
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
            knowledgeService.deleteKnowledge(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            String url = knowledgeService.uploadImage(file.getBytes(), file.getOriginalFilename());
            Map<String, String> response = new HashMap<>();
            response.put("url", url);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
} 