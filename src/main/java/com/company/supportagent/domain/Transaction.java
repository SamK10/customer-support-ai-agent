package com.company.supportagent.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private String transactionId;

    private String accountId;
    private double amount;
    private boolean duplicate;
    private boolean refunded;

    public boolean isDuplicate() {
        return duplicate;
    }
}
