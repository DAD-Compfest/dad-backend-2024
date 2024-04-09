package com.dadcompfest.backend.common.enums;

import lombok.Getter;

@Getter
public enum RedisConstants {
    TOKEN("jwt:"),
    AUTH("auth:"),
    TEAM("team:"),
    ADMIN("admin:"),
    EMAIL("email:"),
    DATA("data:"),
    CONTEST("contest:");



    private final String prefix;

    RedisConstants(String prefix) {
        this.prefix = prefix;
    }

}

