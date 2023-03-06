package com.spring.game.enums;

public enum BetStatus {
    S("Success"), C("Cancelled");

    private final String status;
    BetStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return this.status;
    }
}
