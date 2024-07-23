package com.manager.filemanager.controller;

import com.manager.filemanager.model.Document;
import com.manager.filemanager.service.DocumentService;
import com.manager.filemanager.service.SftpService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docs")
public class DocumentController {

    private final DocumentService documentService;

    private final SftpService sftpService;

    @PostMapping
    public Iterable<Document> saveAll(@RequestBody List<Document> documents) {
        return documentService.saveAll(documents);
    }

    @GetMapping
    public List<Document> findAll() {
        return documentService.findAll();
    }

    @GetMapping("/refresh")
    public void refresh(@RequestParam String path) {
        List<Document> documents = sftpService.listFiles(path);
        documentService.saveAll(documents);
    }

    @GetMapping("/page")
    public Page<Document> page(
            @RequestParam Document.Type type,
            @RequestParam int page,
            @RequestParam int limit
    ) {
        return documentService.findPaginatedByType(type, page, limit);
    }
}
