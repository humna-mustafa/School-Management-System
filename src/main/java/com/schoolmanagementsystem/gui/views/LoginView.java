package com.schoolmanagementsystem.gui.views;

import com.schoolmanagementsystem.models.User;
import com.schoolmanagementsystem.utils.AlertUtil;
import com.schoolmanagementsystem.utils.SessionManager;
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

// Login form for admins. On success it remembers the logged-in person and opens the main window.
public class LoginView {
    private static final double CARD_W = 368;

    private final Stage stage;
    private final AppContext context;
    private final Runnable onLoginSuccess;

    public LoginView(Stage stage, AppContext context, Runnable onLoginSuccess) {
        this.stage = stage;
        this.context = context;
        this.onLoginSuccess = onLoginSuccess;
    }

    public void show() {
        double inner = Math.max(220, CARD_W - 44);

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        TextField passwordVisible = new TextField();
        passwordVisible.setManaged(false);
        passwordVisible.setVisible(false);
        passwordVisible.textProperty().bindBidirectional(password.textProperty());
        passwordVisible.setPromptText("Password");
        CheckBox showPassword = new CheckBox("Show password");

        username.setPrefWidth(inner);
        username.setMaxWidth(inner);
        password.setPrefWidth(inner);
        password.setMaxWidth(inner);
        passwordVisible.setPrefWidth(inner);
        passwordVisible.setMaxWidth(inner);

        VBox passStack = new VBox(6, password, passwordVisible, showPassword);
        passStack.setMaxWidth(inner);
        showPassword.setOnAction(e -> {
            boolean show = showPassword.isSelected();
            passwordVisible.setManaged(show);
            passwordVisible.setVisible(show);
            password.setManaged(!show);
            password.setVisible(!show);
        });

        Button loginBtn = new Button("Login");
        Button signupBtn = new Button("Create account");
        Button forgotBtn = new Button("Forgot password");
        Button exitBtn = new Button("Exit");
        loginBtn.setStyle("-fx-background-color: " + ThemeUtil.Palette.ACTION_BTN + "; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14;");
        signupBtn.setStyle("-fx-background-color: " + ThemeUtil.Palette.CARD + "; -fx-text-fill: " + ThemeUtil.Palette.TEXT_PRIMARY + "; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14; -fx-border-color: " + ThemeUtil.Palette.BORDER + "; -fx-border-radius: 3;");
        forgotBtn.setStyle("-fx-background-color: " + ThemeUtil.Palette.CARD + "; -fx-text-fill: " + ThemeUtil.Palette.TEXT_PRIMARY + "; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14; -fx-border-color: " + ThemeUtil.Palette.BORDER + "; -fx-border-radius: 3;");
        exitBtn.setStyle("-fx-background-color: " + ThemeUtil.Palette.DANGER + "; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14;");

        loginBtn.setPrefWidth(inner);
        loginBtn.setMaxWidth(inner);
        signupBtn.setPrefWidth(inner);
        signupBtn.setMaxWidth(inner);
        forgotBtn.setPrefWidth(inner);
        forgotBtn.setMaxWidth(inner);
        exitBtn.setPrefWidth(inner);
        exitBtn.setMaxWidth(inner);

        loginBtn.setOnAction(e -> {
            User user = context.authService().login(username.getText().trim(), password.getText());
            if (user == null) {
                AlertUtil.error("Username or password looks wrong. Try again.");
                return;
            }
            SessionManager.setCurrentUser(user);
            onLoginSuccess.run();
        });

        signupBtn.setOnAction(e -> new SignupView(stage, context, this::show).show());
        forgotBtn.setOnAction(e -> new ForgotPasswordView(stage, context, this::show).show());
        exitBtn.setOnAction(e -> stage.close());

        VBox formBody = new VBox(10,
                new Label("Username"), username,
                new Label("Password"), passStack,
                loginBtn,
                signupBtn,
                forgotBtn,
                exitBtn
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

        Label cardTitle = new Label("Login");
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

        Scene scene = new Scene(root, 460, 500);
        stage.setScene(scene);
        stage.setTitle("School Management System - Login");
        stage.setMinWidth(400);
        stage.setMinHeight(420);
        stage.setMaximized(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }
}
