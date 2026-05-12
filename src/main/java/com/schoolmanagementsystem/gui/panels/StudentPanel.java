package com.schoolmanagementsystem.gui.panels;

import com.schoolmanagementsystem.models.EntityRecord;
import com.schoolmanagementsystem.services.CrudService;
import com.schoolmanagementsystem.utils.AlertUtil;
import com.schoolmanagementsystem.utils.IdGenerator;
import com.schoolmanagementsystem.utils.InputValidator;
import com.schoolmanagementsystem.utils.ThemeUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Table and form for adding, editing, and deleting student rows in the student file.
public class StudentPanel extends BorderPane {
    private static final double PANEL_MAX_WIDTH = 920;
    private final CrudService service;
    private final TextField id = new TextField();
    private final TextField name = new TextField();
    private final TextField className = new TextField();
    private final TextField section = new TextField();
    private final TextField parentName = new TextField();
    private final TextField parentPhone = new TextField();
    private final ComboBox<String> status = new ComboBox<>(FXCollections.observableArrayList("ACTIVE", "INACTIVE"));
    private final TableView<EntityRecord> table = new TableView<>();

    public StudentPanel(CrudService service) {
        this.service = service;
        id.setEditable(false);
        status.setValue("ACTIVE");
        setPadding(new Insets(12));
        setMaxWidth(PANEL_MAX_WIDTH);

        // Welcome header
        Label welcome = new Label("Welcome, SuperAdmin");
        welcome.setStyle("-fx-text-fill: " + ThemeUtil.Palette.TEXT_PRIMARY + "; -fx-font-size: 24px; -fx-font-weight: bold;");
        VBox welcomeBox = new VBox(welcome);
        welcomeBox.setPadding(new Insets(12, 0, 12, 0));

        GridPane g = new GridPane();
        g.setHgap(8);
        g.setVgap(8);
        g.addRow(0, new Label("Student ID"), id, new Label("Student name"), name);
        g.addRow(1, new Label("Class"), className, new Label("Section"), section);
        g.addRow(2, new Label("Parent / guardian"), parentName, new Label("Parent phone"), parentPhone);
        g.addRow(3, new Label("Status"), status);

        Button add = new Button("Add");
        Button upd = new Button("Update");
        Button del = new Button("Delete");
        Button tbl = new Button("Show table");
        HBox row = new HBox(8, add, upd, del, tbl);
        add.setStyle("-fx-background-color: " + ThemeUtil.Palette.ACTION_BTN + "; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14;");
        upd.setStyle("-fx-background-color: " + ThemeUtil.Palette.ACTION_BTN + "; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14;");
        del.setStyle("-fx-background-color: " + ThemeUtil.Palette.DANGER + "; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14;");
        tbl.setStyle("-fx-background-color: " + ThemeUtil.Palette.CARD + "; -fx-text-fill: " + ThemeUtil.Palette.TEXT_PRIMARY + "; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14; -fx-border-color: " + ThemeUtil.Palette.BORDER + "; -fx-border-radius: 3;");
        row.setSpacing(8);
        row.setPadding(new Insets(8, 0, 0, 0));

        var c0 = new TableColumn<EntityRecord, String>("ID");
        c0.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getId()));
        var c1 = new TableColumn<EntityRecord, String>("Student");
        c1.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getName()));
        var c2 = new TableColumn<EntityRecord, String>("Parent");
        c2.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getDetail1()));
        var c3 = new TableColumn<EntityRecord, String>("Class");
        c3.setCellValueFactory(v -> new SimpleStringProperty(part(v.getValue().getDetail2(), 0)));
        var c4 = new TableColumn<EntityRecord, String>("Sec");
        c4.setCellValueFactory(v -> new SimpleStringProperty(part(v.getValue().getDetail2(), 1)));
        var c5 = new TableColumn<EntityRecord, String>("Phone");
        c5.setCellValueFactory(v -> new SimpleStringProperty(part(v.getValue().getDetail2(), 2)));
        var c6 = new TableColumn<EntityRecord, String>("Status");
        c6.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getStatus()));
        table.getColumns().addAll(List.of(c0, c1, c2, c3, c4, c5, c6));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setVisible(false);
        table.setManaged(false);

        table.getSelectionModel().selectedItemProperty().addListener((o, a, r) -> {
            if (r == null) return;
            id.setText(r.getId());
            name.setText(r.getName());
            parentName.setText(r.getDetail1());
            status.setValue(r.getStatus());
            String d2 = r.getDetail2();
            String[] p = d2 == null ? new String[0] : d2.split("~", -1);
            if (p.length >= 3) {
                className.setText(p[0]);
                section.setText(p[1]);
                parentPhone.setText(p[2]);
            } else {
                className.clear();
                section.clear();
                parentPhone.setText(d2 == null ? "" : d2);
            }
        });

        add.setOnAction(e -> addDo());
        upd.setOnAction(e -> updDo());
        del.setOnAction(e -> delDo());
        tbl.setOnAction(e -> toggle(tbl));

        setTop(new VBox(10, welcomeBox, sectionCard("Students", g), sectionCard("Actions", row)));
        setCenter(table);
        clear();
    }

    private static VBox sectionCard(String title, Node content) {
        Label heading = new Label(title);
        heading.setStyle("-fx-text-fill: " + ThemeUtil.Palette.TEXT_PRIMARY + "; -fx-font-size: 15px; -fx-font-weight: bold;");
        VBox box = new VBox(10, heading, content);
        box.setStyle("-fx-background-color: " + ThemeUtil.Palette.CARD + "; -fx-border-color: " + ThemeUtil.Palette.BORDER
                + "; -fx-border-radius: 3; -fx-background-radius: 3; -fx-padding: 12;");
        return box;
    }

    private static String part(String raw, int i) {
        if (raw == null) return "";
        String[] p = raw.split("~", -1);
        return i < p.length ? p[i] : "";
    }

    private boolean ok() {
        if (InputValidator.isEmpty(name.getText())) {
            AlertUtil.warning("Student name is required.");
            return false;
        }
        if (InputValidator.isEmpty(parentName.getText())) {
            AlertUtil.warning("Parent name is required.");
            return false;
        }
        if (InputValidator.isEmpty(parentPhone.getText())) {
            AlertUtil.warning("Parent phone is required.");
            return false;
        }
        return true;
    }

    private EntityRecord rec() {
        String d2 = String.join("~",
                className.getText().trim(),
                section.getText().trim(),
                parentPhone.getText().trim());
        return new EntityRecord(id.getText(), name.getText().trim(), parentName.getText().trim(), d2, status.getValue());
    }

    private void addDo() {
        if (!ok()) return;
        var list = new ArrayList<>(service.getAll());
        list.add(rec());
        service.saveAll(list);
        AlertUtil.success("Student saved.");
        clear();
        ref();
    }

    private void updDo() {
        if (!ok()) return;
        var list = new ArrayList<>(service.getAll());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id.getText())) {
                list.set(i, rec());
                service.saveAll(list);
                AlertUtil.success("Student updated.");
                ref();
                return;
            }
        }
    }

    private void delDo() {
        if (!AlertUtil.confirm("Delete this student?")) return;
        var list = new ArrayList<>(service.getAll());
        list.removeIf(r -> r.getId().equals(id.getText()));
        service.saveAll(list);
        AlertUtil.success("Removed.");
        clear();
        ref();
    }

    private void ref() {
        if (table.isVisible()) table.setItems(FXCollections.observableArrayList(service.getAll()));
    }

    private void toggle(Button tbl) {
        boolean v = !table.isVisible();
        table.setVisible(v);
        table.setManaged(v);
        tbl.setText(v ? "Hide table" : "Show table");
        if (v) ref();
    }

    private void clear() {
        id.setText(IdGenerator.nextId(
                "STU",
                service.getAll().stream().map(EntityRecord::getId).collect(Collectors.toSet())));
        name.clear();
        className.clear();
        section.clear();
        parentName.clear();
        parentPhone.clear();
        status.setValue("ACTIVE");
    }
}
