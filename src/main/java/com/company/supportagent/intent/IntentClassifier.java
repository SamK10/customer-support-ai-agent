package com.company.supportagent.intent;

import org.springframework.stereotype.Component;

@Component
public class IntentClassifier {

    public Intent classify(String message) {

        String msg = message.toLowerCase();

        if (msg.contains("charged twice") || msg.contains("duplicate")) {
            return Intent.CARD_DISPUTE;
        }
        if (msg.contains("failed") || msg.contains("declined")) {
            return Intent.FAILED_TRANSACTION;
        }
        if (msg.contains("blocked") || msg.contains("locked")) {
            return Intent.ACCOUNT_LOCK;
        }
        if (msg.contains("fraud")) {
            return Intent.FRAUD_ALERT;
        }
        if (msg.contains("balance")) {
            return Intent.BALANCE_QUERY;
        }
        return Intent.UNKNOWN;
    }
}
