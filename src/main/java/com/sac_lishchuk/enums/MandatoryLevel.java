package com.sac_lishchuk.enums;

import lombok.Getter;

@Getter
public enum MandatoryLevel {
    TOP_SECRET(7),
    SECRET(6),
    HIGHLY_CONFIDENTIAL(5),
    CONFIDENTIAL(4),
    RESTRICTED(3),
    INTERNAL_USE_ONLY(2),
    UNCLASSIFIED(1),
    PUBLIC(0);

    private final int priority;

    MandatoryLevel(int priority) {
        this.priority = priority;
    }

}
