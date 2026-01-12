package com.company.supportagent.service;

import com.company.supportagent.domain.Transaction;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    public List<Transaction> fetchTransactions(String customerId) {
        return List.of(); // mocked for Week 1
    }

    public boolean hasDuplicateTransaction(List<Transaction> txns) {
        return txns.stream().anyMatch(Transaction::isDuplicate);
    }
}
