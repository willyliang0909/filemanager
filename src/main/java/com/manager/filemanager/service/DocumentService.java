package com.manager.filemanager.service;

import com.manager.filemanager.model.Document;
import com.manager.filemanager.repository.DocumentRepository;
import org.springframework.stereotype.Service;

@Service
public class DocumentService extends CrudService<Document, String, DocumentRepository> {

}
