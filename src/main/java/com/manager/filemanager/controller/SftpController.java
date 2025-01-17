package com.manager.filemanager.controller;

import com.manager.filemanager.model.Document;
import com.manager.filemanager.service.SftpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sftp")
public class SftpController {

    private final SftpService sftpService;

    @GetMapping("/list-files")
    public List<Document> listFiles(@RequestParam String directory) {
        return sftpService.listFiles(directory);
    }
}