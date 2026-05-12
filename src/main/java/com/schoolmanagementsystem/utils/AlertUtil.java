package com.schoolmanagementsystem.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

// Wraps JavaFX alert boxes so other classes only pass a short message string.
public final class AlertUtil {
    private AlertUtil() {
    }

    public static void success(String message) {
        show(Alert.AlertType.INFORMATION, "OK", message);
    }

    public static void warning(String message) {
        show(Alert.AlertType.WARNING, "Warning", message);
    }

    public static void error(String message) {
        show(Alert.AlertType.ERROR, "Error", message);
    }

    public static boolean confirm(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private static void show(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
