package com.schoolmanagementsystem.utils;

import javafx.scene.Parent;
import javafx.scene.paint.Color;

// Colours and a small amount of theme logic used by the JavaFX screens.
// Most code only reads colour constants from the Palette type below.
public final class ThemeUtil {
    private ThemeUtil() {
    }

    // Named colours so the rest of the program does not scatter raw hex codes everywhere.
    public static final class Palette {
        // Main surfaces
        public static final String HEADER = "#4F6FD8";
        public static final String SIDEBAR = "#9BB7F5";
        public static final String CONTENT_CREAM = "#FFFCF6";
        public static final String ACTIVE_SIDEBAR_ITEM = "#FFF9EF";
        public static final String CARD = "#FFFFFF";

        // Text and borders
        public static final String BORDER = "#D8E0EA";
        public static final String BORDER2 = "#D8E0EA";
        public static final String TEXT_PRIMARY = "#0F172A";
        public static final String TEXT_MUTED = "#64748B";

        // Buttons and accents
        public static final String ACTION_BTN = "#1F4E79";
        public static final String DANGER = "#B91C1C";
        public static final String ACCENT_CYAN = "#0EA5E9";
        public static final String METRIC_MINT = "#EEFBF8";
        public static final String INPUT_BORDER = BORDER;
        public static final String SURFACE = CONTENT_CREAM;

        private Palette() {
        }

        public static String encodeDefaultTheme() {
            return ThemeUtil.encodeTheme(ACTION_BTN, HEADER, CONTENT_CREAM);
        }
    }

    public static void applyBackground(Parent node) {
        node.setStyle("-fx-background-color: " + Palette.CONTENT_CREAM + ";");
    }

    public static String encodeTheme(String primaryHex, String secondaryHex, String tertiaryHex) {
        return String.join(",", normalize(primaryHex), normalize(secondaryHex), normalize(tertiaryHex));
    }

    private static String normalize(String hex) {
        try {
            return Color.web(hex).toString().replace("0x", "#").substring(0, 7);
        } catch (Exception e) {
            return hex;
        }
    }
}
