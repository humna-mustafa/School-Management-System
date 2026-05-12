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

// Table and form for teacher rows including the packed extra details string used in the file.
public class TeacherPanel extends BorderPane {
    private static final double PANEL_MAX_WIDTH = 920;
    private final CrudService service;
    private final TextField id = new TextField();
    private final TextField name = new TextField();
    private final TextField qualification = new TextField();
    private final TextField specialization = new TextField();
    private final TextField phone = new TextField();
    private final TextField email = new TextField();
    private final TextField address = new TextField();
    private final TextField salary = new TextField();
    private final TextField assignedSubject = new TextField();
    private final DatePicker joiningDate = new DatePicker();
    private final ComboBox<String> status = new ComboBox<>(FXCollections.observableArrayList("ACTIVE", "INACTIVE", "LEFT"));
    private final TableView<EntityRecord> table = new TableView<>();

    public TeacherPanel(CrudService service) {
        this.service = service;
        setPadding(new Insets(12));
        setMaxWidth(PANEL_MAX_WIDTH);
        id.setEditable(false);
        status.setValue("ACTIVE");

        GridPane g = new GridPane();
        g.setHgap(8);
        g.setVgap(8);
        g.addRow(0, new Label("Teacher ID"), id, new Label("Name"), name);
        g.addRow(1, new Label("Qualification"), qualification, new Label("Specialization"), specialization);
        g.addRow(2, new Label("Phone"), phone, new Label("Email"), email);
        g.addRow(3, new Label("Address"), address, new Label("Salary"), salary);
        g.addRow(4, new Label("Joining date"), joiningDate, new Label("Subject"), assignedSubject);
        g.addRow(5, new Label("Status"), status);

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
        var c1 = new TableColumn<EntityRecord, String>("Name");
        c1.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getName()));
        var c2 = new TableColumn<EntityRecord, String>("Subject");
        c2.setCellValueFactory(v -> new SimpleStringProperty(part(v.getValue().getDetail2(), 0)));
        var c3 = new TableColumn<EntityRecord, String>("Phone");
        c3.setCellValueFactory(v -> new SimpleStringProperty(part(v.getValue().getDetail2(), 1)));
        var c4 = new TableColumn<EntityRecord, String>("Email");
        c4.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getDetail1()));
        var c5 = new TableColumn<EntityRecord, String>("Status");
        c5.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getStatus()));
        table.getColumns().addAll(List.of(c0, c1, c2, c3, c4, c5));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setVisible(false);
        table.setManaged(false);

        table.getSelectionModel().selectedItemProperty().addListener((o, a, r) -> {
            if (r == null) return;
            id.setText(r.getId());
            name.setText(r.getName());
            email.setText(r.getDetail1());
            status.setValue(r.getStatus());
            String[] p = r.getDetail2().split("~", -1);
            assignedSubject.setText(ix(p, 0));
            phone.setText(ix(p, 1));
            qualification.setText(ix(p, 2));
            specialization.setText(ix(p, 3));
            address.setText(ix(p, 4));
            salary.setText(ix(p, 5));
            String jd = ix(p, 6);
            joiningDate.setValue(jd.isBlank() ? null : java.time.LocalDate.parse(jd));
        });

        add.setOnAction(e -> addDo());
        upd.setOnAction(e -> updDo());
        del.setOnAction(e -> delDo());
        tbl.setOnAction(e -> toggle(tbl));

        setTop(new VBox(10, sectionCard("Teachers", g), sectionCard("Actions", row)));
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

    private static String ix(String[] p, int i) {
        return i < p.length ? p[i] : "";
    }

    private boolean valid() {
        if (InputValidator.isEmpty(name.getText())) {
            AlertUtil.warning("Teacher name is required.");
            return false;
        }
        if (!InputValidator.isEmailValid(email.getText())) {
            AlertUtil.warning("Email should contain @.");
            return false;
        }
        if (!InputValidator.isEmpty(salary.getText()) && !InputValidator.isNumber(salary.getText())) {
            AlertUtil.warning("Salary must be numeric.");
            return false;
        }
        return true;
    }

    private EntityRecord build() {
        String joinDate = joiningDate.getValue() == null ? "" : joiningDate.getValue().toString();
        String d2 = String.join(
                "~",
                assignedSubject.getText().trim(),
                phone.getText().trim(),
                qualification.getText().trim(),
                specialization.getText().trim(),
                address.getText().trim(),
                salary.getText().trim(),
                joinDate);
        return new EntityRecord(
                id.getText(),
                name.getText().trim(),
                email.getText().trim(),
                d2,
                status.getValue());
    }

    private void addDo() {
        if (!valid()) return;
        var l = new ArrayList<>(service.getAll());
        l.add(build());
        service.saveAll(l);
        AlertUtil.success("Teacher saved.");
        clear();
        ref();
    }

    private void updDo() {
        if (!valid()) return;
        var l = new ArrayList<>(service.getAll());
        for (int i = 0; i < l.size(); i++) {
            if (l.get(i).getId().equals(id.getText())) {
                l.set(i, build());
                service.saveAll(l);
                AlertUtil.success("Teacher updated.");
                ref();
                return;
            }
        }
    }

    private void delDo() {
        if (!AlertUtil.confirm("Remove this teacher?")) return;
        var l = new ArrayList<>(service.getAll());
        l.removeIf(r -> r.getId().equals(id.getText()));
        service.saveAll(l);
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
                "TCH",
                service.getAll().stream().map(EntityRecord::getId).collect(Collectors.toSet())));
        for (TextField f : List.of(name, qualification, specialization, phone, email, address, salary, assignedSubject)) {
            f.clear();
        }
        joiningDate.setValue(null);
        status.setValue("ACTIVE");
    }
}
