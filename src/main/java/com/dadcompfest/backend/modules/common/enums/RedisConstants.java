package com.dadcompfest.backend.modules.common.enums;

import lombok.Getter;

@Getter
public enum RedisConstants {
    TOKEN("jwt:"),
    AUTH("auth:"),
    TEAM("team:"),
    ADMIN("admin:"),
    EMAIL("email:"),
    DATA("data:");



    private final String prefix;

    RedisConstants(String prefix) {
        this.prefix = prefix;
    }

}

