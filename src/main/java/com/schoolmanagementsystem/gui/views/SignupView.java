package com.schoolmanagementsystem.gui.views;

import com.schoolmanagementsystem.utils.AlertUtil;
import com.schoolmanagementsystem.utils.InputValidator;
import com.schoolmanagementsystem.utils.ThemeUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Screen where a new admin types their details and gets written into the users file.
public class SignupView {
    private static final double CARD_W = 368;

    private final Stage stage;
    private final AppContext context;
    private final Runnable backAction;

    public SignupView(Stage stage, AppContext context, Runnable backAction) {
        this.stage = stage;
        this.context = context;
        this.backAction = backAction;
    }

    public void show() {
        double inner = Math.max(220, CARD_W - 44);

        TextField fullName = new TextField();
        fullName.setPromptText("Full name");
        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        TextField passwordVisible = new TextField();
        passwordVisible.setManaged(false);
        passwordVisible.setVisible(false);
        PasswordField confirm = new PasswordField();
        TextField confirmVisible = new TextField();
        confirmVisible.setManaged(false);
        confirmVisible.setVisible(false);
        TextField question = new TextField();
        question.setPromptText("Security question");
        TextField answer = new TextField();
        answer.setPromptText("Answer");
        passwordVisible.textProperty().bindBidirectional(password.textProperty());
        confirmVisible.textProperty().bindBidirectional(confirm.textProperty());
        CheckBox showPassword = new CheckBox("Show passwords");

        for (TextField f : new TextField[]{fullName, username, question, answer, passwordVisible, confirmVisible}) {
            f.setPrefWidth(inner);
            f.setMaxWidth(inner);
        }
        password.setPrefWidth(inner);
        password.setMaxWidth(inner);
        confirm.setPrefWidth(inner);
        confirm.setMaxWidth(inner);

        VBox pass1 = new VBox(6, password, passwordVisible);
        VBox pass2 = new VBox(6, confirm, confirmVisible, showPassword);
        pass1.setMaxWidth(inner);
        pass2.setMaxWidth(inner);
        password.setPromptText("Password");
        confirm.setPromptText("Confirm password");
        passwordVisible.setPromptText("Password");
        confirmVisible.setPromptText("Confirm password");
        showPassword.setOnAction(e -> {
            boolean show = showPassword.isSelected();
            password.setManaged(!show);
            password.setVisible(!show);
            passwordVisible.setManaged(show);
            passwordVisible.setVisible(show);
            confirm.setManaged(!show);
            confirm.setVisible(!show);
            confirmVisible.setManaged(show);
            confirmVisible.setVisible(show);
        });

        Button saveBtn = new Button("Create account");
        Button backBtn = new Button("Back");
        saveBtn.setStyle("-fx-background-color: " + ThemeUtil.Palette.ACTION_BTN + "; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14;");
        backBtn.setStyle("-fx-background-color: " + ThemeUtil.Palette.CARD + "; -fx-text-fill: " + ThemeUtil.Palette.TEXT_PRIMARY + "; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14; -fx-border-color: " + ThemeUtil.Palette.BORDER + "; -fx-border-radius: 3;");
        saveBtn.setPrefWidth(inner);
        saveBtn.setMaxWidth(inner);
        backBtn.setPrefWidth(inner);
        backBtn.setMaxWidth(inner);

        saveBtn.setOnAction(e -> {
            if (InputValidator.isEmpty(fullName.getText())
                    || InputValidator.isEmpty(username.getText())
                    || InputValidator.isEmpty(password.getText())) {
                AlertUtil.warning("Please fill name, username and password first.");
                return;
            }
            if (!password.getText().equals(confirm.getText())) {
                AlertUtil.warning("Both password boxes should match.");
                return;
            }
            boolean ok = context.authService().signup(
                    fullName.getText().trim(),
                    username.getText().trim(),
                    password.getText(),
                    question.getText().trim(),
                    answer.getText().trim());
            if (!ok) {
                AlertUtil.warning("That username is already taken. Pick another one.");
                return;
            }
            AlertUtil.success("New account is saved. You can log in now.");
            backAction.run();
        });

        backBtn.setOnAction(e -> backAction.run());

        VBox formBody = new VBox(10,
                new Label("Full name"), fullName,
                new Label("Username"), username,
                new Label("Password"), pass1,
                new Label("Confirm password"), pass2,
                new Label("Security question"), question,
                new Label("Security answer"), answer,
                saveBtn,
                backBtn
        );
        formBody.setMaxWidth(inner);
        formBody.setFillWidth(false);

        ScrollPane scroll = new ScrollPane(formBody);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPrefViewportHeight(280);
        scroll.setMaxHeight(300);
        scroll.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setMaxWidth(CARD_W);
        card.setStyle(
                "-fx-background-color: " + ThemeUtil.Palette.CARD + ";" +
                        "-fx-border-color: " + ThemeUtil.Palette.BORDER + ";" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;"
        );

        Label cardTitle = new Label("Create account");
        cardTitle.setStyle(
                "-fx-text-fill: " + ThemeUtil.Palette.TEXT_PRIMARY + ";" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );

        card.getChildren().addAll(cardTitle, scroll);

        StackPane root = new StackPane(card);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(24, 32, 24, 32));
        root.setStyle("-fx-background-color: " + ThemeUtil.Palette.SURFACE + ";");

        Scene scene = new Scene(root, 460, 520);
        stage.setScene(scene);
        stage.setTitle("School Management System - Create account");
        stage.setMinWidth(400);
        stage.setMinHeight(480);
        stage.setMaximized(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }
}
