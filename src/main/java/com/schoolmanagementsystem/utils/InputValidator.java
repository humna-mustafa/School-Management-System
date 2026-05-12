package com.schoolmanagementsystem.utils;

// Tiny checks used by forms before we save data or show an error popup.
public final class InputValidator {
    private InputValidator() {
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isEmailValid(String email) {
        return isEmpty(email) || email.contains("@");
    }

    public static boolean isNumber(String value) {
        if (isEmpty(value)) return false;
        try {
            Double.parseDouble(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
