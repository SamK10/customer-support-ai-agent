package com.company.supportagent.rag;

import com.company.supportagent.intent.Intent;

public class IntentQueryMapper {

    public static String toQuery(Intent intent) {
        return switch (intent) {
            case CARD_DISPUTE ->
                    "duplicate transaction card charged twice";
            case BALANCE_QUERY ->
                    "incorrect balance missing funds";
            case ACCOUNT_LOCK ->
                    "account login authentication failure";
            default ->
                    intent.name().toLowerCase();
        };
    }
}
