package com.company.supportagent.rag;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class KnowledgeRetriever {

    private final KnowledgeStore store;

    public KnowledgeRetriever(KnowledgeStore store) {
        this.store = store;
    }

    public List<KnowledgeDocument> retrieve(String query) {

        return store.getAll().stream()
                .filter(doc ->
                        doc.getContent()
                                .toLowerCase()
                                .contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}
