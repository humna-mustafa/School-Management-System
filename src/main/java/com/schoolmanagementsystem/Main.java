package com.schoolmanagementsystem;

import com.schoolmanagementsystem.gui.views.AppContext;
import com.schoolmanagementsystem.gui.views.LoginView;
import com.schoolmanagementsystem.gui.panels.FeePanel;
import com.schoolmanagementsystem.gui.panels.StudentPanel;
import com.schoolmanagementsystem.gui.panels.TeacherPanel;
import com.schoolmanagementsystem.services.AuthService;
import com.schoolmanagementsystem.services.CrudService;
import com.schoolmanagementsystem.storage.DataStore;
import com.schoolmanagementsystem.storage.FileManager;
import com.schoolmanagementsystem.utils.Constants;
import com.schoolmanagementsystem.utils.SessionManager;
import com.schoolmanagementsystem.utils.ThemeUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Program entry point. Builds services, then opens the login screen.
public class Main extends Application {
    @Override
    public void start(Stage stage) {
        FileManager fm = new FileManager();
        fm.initialize();

        AuthService authService = new AuthService(fm);
        DataStore store = new DataStore(fm);

        AppContext ctx = new AppContext(
                authService,
                new CrudService(store, Constants.STUDENTS_FILE),
                new CrudService(store, Constants.TEACHERS_FILE),
                new CrudService(store, Constants.FEES_FILE));

        new LoginView(stage, ctx, () -> showMain(stage, ctx)).show();
    }

    private static void showMain(Stage stage, AppContext ctx) {
        BorderPane root = new BorderPane();
        
        // Header
        Label title = new Label("The Smart School · 2026-27");
        title.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16px; -fx-font-weight: bold;");
        
        Button logout = new Button("Logout");
        logout.setStyle("-fx-background-color: #00BCD4; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-padding: 8 16; -fx-border-radius: 3; -fx-background-radius: 3;");
        logout.setOnAction(e -> {
            SessionManager.clear();
            new LoginView(stage, ctx, () -> showMain(stage, ctx)).show();
        });
        
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        
        HBox header = new HBox(20, title, headerSpacer, logout);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(12, 20, 12, 20));
        header.setStyle("-fx-background-color: #4F6FD8;");
        
        // Sidebar
        // Content area
        StackPane content = new StackPane();
        content.setStyle("-fx-background-color: " + ThemeUtil.Palette.CONTENT_CREAM + ";");
        
        VBox sidebar = createSidebar(content, ctx);
        
        root.setTop(header);
        root.setLeft(sidebar);
        root.setCenter(content);
        
        // Show Dashboard (Students) by default
        showContent(content, new StudentPanel(ctx.studentService()));
        
        Scene scene = new Scene(root, 1200, 720);
        stage.setScene(scene);
        stage.setTitle("School Management System");
        stage.setMinWidth(960);
        stage.setMinHeight(640);
        stage.centerOnScreen();
        stage.show();
    }
    
    private static VBox createSidebar(StackPane content, AppContext ctx) {
        VBox sidebar = new VBox(8);
        sidebar.setStyle("-fx-background-color: #9BB7F5; -fx-padding: 15;");
        sidebar.setPrefWidth(180);
        
        String btnStyle = "-fx-padding: 12 16; -fx-font-size: 13px; -fx-font-weight: bold; -fx-text-alignment: LEFT; -fx-cursor: hand;";
        String activeBtnStyle = btnStyle + " -fx-background-color: #FFFFFF; -fx-text-fill: #0F172A;";
        String inactiveBtnStyle = btnStyle + " -fx-background-color: #9BB7F5; -fx-text-fill: #FFFFFF; -fx-border-color: transparent;";
        
        // Create buttons
        Button students = new Button("Students");
        students.setStyle(activeBtnStyle);
        students.setMaxWidth(Double.MAX_VALUE);
        
        Button teachers = new Button("Teachers");
        teachers.setStyle(inactiveBtnStyle);
        teachers.setMaxWidth(Double.MAX_VALUE);
        
        Button fees = new Button("Fees");
        fees.setStyle(inactiveBtnStyle);
        fees.setMaxWidth(Double.MAX_VALUE);
        
        // Track active button
        Button[] activeButton = {students};
        
        // Students action
        students.setOnAction(e -> {
            activeButton[0].setStyle(inactiveBtnStyle);
            students.setStyle(activeBtnStyle);
            activeButton[0] = students;
            showContent(content, new StudentPanel(ctx.studentService()));
        });
        
        // Teachers action
        teachers.setOnAction(e -> {
            activeButton[0].setStyle(inactiveBtnStyle);
            teachers.setStyle(activeBtnStyle);
            activeButton[0] = teachers;
            showContent(content, new TeacherPanel(ctx.teacherService()));
        });
        
        // Fees action
        fees.setOnAction(e -> {
            activeButton[0].setStyle(inactiveBtnStyle);
            fees.setStyle(activeBtnStyle);
            activeButton[0] = fees;
            showContent(content, new FeePanel(ctx.feeService()));
        });
        
        sidebar.getChildren().addAll(students, teachers, fees);
        
        return sidebar;
    }
    
    private static void showContent(StackPane content, Node panel) {
        content.getChildren().clear();
        
        ScrollPane scroll = new ScrollPane(panel);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + ThemeUtil.Palette.CONTENT_CREAM + ";");
        
        content.getChildren().add(scroll);
    }

    public static void main(String[] args) {
        launch();
    }
}
