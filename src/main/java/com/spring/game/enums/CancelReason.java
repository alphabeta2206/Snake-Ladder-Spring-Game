package com.spring.game.enums;

public enum CancelReason {
    NO_REASON("NO_REASON"), PLAYERS_ARE_NOT_GOOD("PLAYERS_ARE_NOT_GOOD");
    private final String reason;
    CancelReason(String reason) {
        this.reason = reason;
    }
    public String getReason() {
        return reason;
    }
}
