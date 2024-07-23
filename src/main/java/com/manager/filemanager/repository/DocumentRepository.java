package com.manager.filemanager.repository;

import com.couchbase.client.java.query.QueryScanConsistency;
import com.manager.filemanager.model.Document;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.data.couchbase.repository.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Scope("ecom")
@Collection("document")
@Repository
@ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
public interface DocumentRepository extends CouchbaseRepository<Document, String> {

    Page<Document> findByType(Document.Type type, Pageable pageable);
}
