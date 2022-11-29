package com.apu.example.springsecurityjwt.util;

import java.util.HashMap;
import java.util.Map;

public enum Role {
    ADMIN("ADMIN"),
    USER("USER");
    private final String role;

    Role(String role) {
        this.role = role;
    }

    private final static Map<String, Role> lookup = new HashMap<>();

    static {
        for(Role userRole: values()) {
            lookup.put(userRole.getValue(), userRole);
        }
    }

    public String getValue() {
        return this.role;
    }

    public static Role get(String role) {
        return lookup.get(role);
    }
}
