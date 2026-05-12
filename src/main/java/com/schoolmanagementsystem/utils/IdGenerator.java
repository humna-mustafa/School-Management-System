package com.schoolmanagementsystem.utils;

import java.util.Collection;

// Picks the next id string after the highest number already used for that prefix.
public final class IdGenerator {
    private IdGenerator() {
    }

    public static String nextId(String prefix, Collection<String> ids) {
        int max = 0;
        for (String id : ids) {
            if (id != null && id.startsWith(prefix)) {
                String n = id.substring(prefix.length());
                try {
                    max = Math.max(max, Integer.parseInt(n));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return prefix + String.format("%03d", max + 1);
    }
}
