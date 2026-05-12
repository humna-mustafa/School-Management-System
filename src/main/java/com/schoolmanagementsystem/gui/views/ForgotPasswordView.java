package com.schoolmanagementsystem.gui.views;

import com.schoolmanagementsystem.utils.AlertUtil;
import com.schoolmanagementsystem.utils.ThemeUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Lets an admin reset a password using the saved security answer checked against the users file.
public class ForgotPasswordView {
    private static final double CARD_W = 368;

    private final Stage stage;
    private final AppContext context;
    private final Runnable backAction;

    public ForgotPasswordView(Stage stage, AppContext context, Runnable backAction) {
        this.stage = stage;
        this.context = context;
        this.backAction = backAction;
    }

    public void show() {
        double inner = Math.max(220, CARD_W - 44);

        TextField username = new TextField();
        username.setPromptText("Username");
        TextField answer = new TextField();
        answer.setPromptText("Security answer");
        PasswordField newPassword = new PasswordField();
        PasswordField confirm = new PasswordField();
        TextField visibleNew = new TextField();
        TextField visibleConfirm = new TextField();
        visibleNew.textProperty().bindBidirectional(newPassword.textProperty());
        visibleConfirm.textProperty().bindBidirectional(confirm.textProperty());
        visibleNew.setManaged(false);
        visibleNew.setVisible(false);
        visibleConfirm.setManaged(false);
        visibleConfirm.setVisible(false);
        newPassword.setPromptText("New password");
        confirm.setPromptText("Confirm new password");
        visibleNew.setPromptText("New password");
        visibleConfirm.setPromptText("Confirm new password");
        CheckBox show = new CheckBox("Show passwords");

        username.setPrefWidth(inner);
        username.setMaxWidth(inner);
        answer.setPrefWidth(inner);
        answer.setMaxWidth(inner);
        newPassword.setPrefWidth(inner);
        newPassword.setMaxWidth(inner);
        confirm.setPrefWidth(inner);
        confirm.setMaxWidth(inner);
        visibleNew.setPrefWidth(inner);
        visibleNew.setMaxWidth(inner);
        visibleConfirm.setPrefWidth(inner);
        visibleConfirm.setMaxWidth(inner);

        VBox rowNew = new VBox(6, newPassword, visibleNew);
        VBox rowConfirm = new VBox(6, confirm, visibleConfirm, show);
        rowNew.setMaxWidth(inner);
        rowConfirm.setMaxWidth(inner);
        show.setOnAction(e -> {
            boolean on = show.isSelected();
            newPassword.setManaged(!on);
            newPassword.setVisible(!on);
            confirm.setManaged(!on);
            confirm.setVisible(!on);
            visibleNew.setManaged(on);
            visibleNew.setVisible(on);
            visibleConfirm.setManaged(on);
            visibleConfirm.setVisible(on);
        });

        Button resetBtn = new Button("Reset password");
        Button backBtn = new Button("Back");
        resetBtn.setStyle("-fx-background-color: " + ThemeUtil.Palette.ACTION_BTN + "; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14;");
        backBtn.setStyle("-fx-background-color: " + ThemeUtil.Palette.CARD + "; -fx-text-fill: " + ThemeUtil.Palette.TEXT_PRIMARY + "; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14; -fx-border-color: " + ThemeUtil.Palette.BORDER + "; -fx-border-radius: 3;");
        resetBtn.setPrefWidth(inner);
        resetBtn.setMaxWidth(inner);
        backBtn.setPrefWidth(inner);
        backBtn.setMaxWidth(inner);

        resetBtn.setOnAction(e -> {
            if (!newPassword.getText().equals(confirm.getText())) {
                AlertUtil.warning("Both new password boxes should match.");
                return;
            }
            boolean ok = context.authService().resetPassword(
                    username.getText().trim(),
                    answer.getText().trim(),
                    newPassword.getText());
            if (!ok) {
                AlertUtil.error("Username or security answer does not match our file.");
                return;
            }
            AlertUtil.success("Password is updated. Log in with the new one.");
            backAction.run();
        });
        backBtn.setOnAction(e -> backAction.run());

        VBox formBody = new VBox(10,
                new Label("Username"), username,
                new Label("Security answer"), answer,
                new Label("New password"), rowNew,
                new Label("Confirm password"), rowConfirm,
                resetBtn,
                backBtn
        );
        formBody.setMaxWidth(inner);
        formBody.setFillWidth(false);

        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setMaxWidth(CARD_W);
        card.setStyle(
                "-fx-background-color: " + ThemeUtil.Palette.CARD + ";" +
                        "-fx-border-color: " + ThemeUtil.Palette.BORDER + ";" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;"
        );

        Label cardTitle = new Label("Forgot password");
        cardTitle.setStyle(
                "-fx-text-fill: " + ThemeUtil.Palette.TEXT_PRIMARY + ";" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );

        card.getChildren().addAll(cardTitle, formBody);

        StackPane root = new StackPane(card);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(24, 32, 24, 32));
        root.setStyle("-fx-background-color: " + ThemeUtil.Palette.SURFACE + ";");

        Scene scene = new Scene(root, 460, 520);
        stage.setScene(scene);
        stage.setTitle("School Management System - Forgot password");
        stage.setMinWidth(400);
        stage.setMinHeight(420);
        stage.setMaximized(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }
}
