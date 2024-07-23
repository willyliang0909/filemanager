package com.manager.filemanager.service;

import com.manager.filemanager.model.Document;
import com.manager.filemanager.repository.DocumentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DocumentService extends CrudService<Document, String, DocumentRepository> {

    public Page<Document> findPaginatedByType(Document.Type type, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return repository.findByType(type, pageRequest);
    }

}
