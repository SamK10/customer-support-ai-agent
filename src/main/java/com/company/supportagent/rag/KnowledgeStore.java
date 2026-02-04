package com.company.supportagent.rag;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KnowledgeStore {

    private final List<KnowledgeDocument> documents = new ArrayList<>();

    public void add(KnowledgeDocument doc) {
        documents.add(doc);
    }

    public List<KnowledgeDocument> getAll() {
        return documents;
    }
}
