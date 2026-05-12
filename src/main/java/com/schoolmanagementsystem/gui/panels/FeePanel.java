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

// Fee screen. Extra fee text is packed into one long string field so one row stays one line in the file.
public class FeePanel extends BorderPane {
    private static final double PANEL_MAX_WIDTH = 920;
    private final CrudService service;
    private final TextField id = new TextField();
    private final TextField studentId = new TextField();
    private final TextField studentName = new TextField();
    private final TextField className = new TextField();
    private final TextField month = new TextField();
    private final TextField tuition = new TextField("0");
    private final TextField total = new TextField();
    private final TextField paid = new TextField("0");
    private final TextField remaining = new TextField();
    private final DatePicker dueDate = new DatePicker();
    private final DatePicker paymentDate = new DatePicker();
    private final ComboBox<String> status = new ComboBox<>(
            FXCollections.observableArrayList("PAID", "UNPAID", "PARTIALLY_PAID", "LATE"));
    private final TableView<EntityRecord> table = new TableView<>();

    public FeePanel(CrudService service) {
        this.service = service;
        setPadding(new Insets(12));
        setMaxWidth(PANEL_MAX_WIDTH);
        id.setEditable(false);
        total.setEditable(false);
        remaining.setEditable(false);
        status.setValue("UNPAID");

        GridPane g = new GridPane();
        g.setHgap(8);
        g.setVgap(8);
        g.addRow(0, new Label("Fee ID"), id, new Label("Student ID"), studentId);
        g.addRow(1, new Label("Student name"), studentName, new Label("Class"), className);
        g.addRow(2, new Label("Month"), month, new Label("Tuition"), tuition);
        g.addRow(3, new Label("Total"), total, new Label("Paid"), paid);
        g.addRow(4, new Label("Remaining"), remaining, new Label("Due date"), dueDate);
        g.addRow(5, new Label("Payment date"), paymentDate, new Label("Status"), status);

        Button add = new Button("Add");
        Button calc = new Button("Calculate");
        Button mark = new Button("Mark paid");
        Button upd = new Button("Update");
        Button del = new Button("Delete");
        Button tbl = new Button("Show table");
        HBox row = new HBox(8, add, calc, mark, upd, del, tbl);
        add.setStyle("-fx-background-color: " + ThemeUtil.Palette.ACTION_BTN + "; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14;");
        calc.setStyle("-fx-background-color: " + ThemeUtil.Palette.CARD + "; -fx-text-fill: " + ThemeUtil.Palette.TEXT_PRIMARY + "; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14; -fx-border-color: " + ThemeUtil.Palette.BORDER + "; -fx-border-radius: 3;");
        mark.setStyle("-fx-background-color: " + ThemeUtil.Palette.CARD + "; -fx-text-fill: " + ThemeUtil.Palette.TEXT_PRIMARY + "; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14; -fx-border-color: " + ThemeUtil.Palette.BORDER + "; -fx-border-radius: 3;");
        upd.setStyle("-fx-background-color: " + ThemeUtil.Palette.ACTION_BTN + "; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14;");
        del.setStyle("-fx-background-color: " + ThemeUtil.Palette.DANGER + "; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14;");
        tbl.setStyle("-fx-background-color: " + ThemeUtil.Palette.CARD + "; -fx-text-fill: " + ThemeUtil.Palette.TEXT_PRIMARY + "; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 8 14; -fx-border-color: " + ThemeUtil.Palette.BORDER + "; -fx-border-radius: 3;");
        row.setSpacing(8);
        row.setPadding(new Insets(8, 0, 0, 0));

        var c0 = new TableColumn<EntityRecord, String>("ID");
        c0.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getId()));
        var c1 = new TableColumn<EntityRecord, String>("Student ID");
        c1.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getName()));
        var c2 = new TableColumn<EntityRecord, String>("Month");
        c2.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getDetail1()));
        var c3 = new TableColumn<EntityRecord, String>("Status");
        c3.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getStatus()));
        table.getColumns().addAll(List.of(c0, c1, c2, c3));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setVisible(false);
        table.setManaged(false);

        calc.setOnAction(e -> calculate());
        mark.setOnAction(e -> {
            paid.setText(total.getText().isBlank() ? "0" : total.getText());
            calculate();
        });
        add.setOnAction(e -> addDo());
        upd.setOnAction(e -> updDo());
        del.setOnAction(e -> delDo());
        tbl.setOnAction(e -> toggle(tbl));

        table.getSelectionModel().selectedItemProperty().addListener((o, a, r) -> {
            if (r == null) return;
            id.setText(r.getId());
            studentId.setText(r.getName());
            month.setText(r.getDetail1());
            status.setValue(r.getStatus());
            loadBlob(r.getDetail2());
        });

        setTop(new VBox(10, sectionCard("Fees", g), sectionCard("Actions", row)));
        setCenter(table);
        resetForm();
    }

    private static VBox sectionCard(String title, Node content) {
        Label heading = new Label(title);
        heading.setStyle("-fx-text-fill: " + ThemeUtil.Palette.TEXT_PRIMARY + "; -fx-font-size: 15px; -fx-font-weight: bold;");
        VBox box = new VBox(10, heading, content);
        box.setStyle("-fx-background-color: " + ThemeUtil.Palette.CARD + "; -fx-border-color: " + ThemeUtil.Palette.BORDER
                + "; -fx-border-radius: 3; -fx-background-radius: 3; -fx-padding: 12;");
        return box;
    }

    private void loadBlob(String raw) {
        String[] p = raw == null ? new String[0] : raw.split("~", -1);
        if (p.length >= 13) {
            fillFrom13(p);
        } else if (p.length >= 11) {
            fillFrom11(p);
        } else if (p.length >= 8) {
            fillShort(p);
        } else {
            studentName.clear();
            className.clear();
            tuition.setText("0");
        }
    }

    private void fillShort(String[] p) {
        studentName.setText(ix(p, 0));
        className.setText(ix(p, 1));
        tuition.setText(ix(p, 2));
        total.setText(ix(p, 3));
        paid.setText(ix(p, 4));
        remaining.setText(ix(p, 5));
        dueDate.setValue(ix(p, 6).isBlank() ? null : java.time.LocalDate.parse(ix(p, 6)));
        paymentDate.setValue(ix(p, 7).isBlank() ? null : java.time.LocalDate.parse(ix(p, 7)));
    }

    private void fillFrom11(String[] p) {
        studentName.setText(ix(p, 0));
        className.setText(ix(p, 1));
        tuition.setText(ix(p, 2));
        total.setText(ix(p, 6));
        paid.setText(ix(p, 7));
        remaining.setText(ix(p, 8));
        dueDate.setValue(ix(p, 9).isBlank() ? null : java.time.LocalDate.parse(ix(p, 9)));
        paymentDate.setValue(ix(p, 10).isBlank() ? null : java.time.LocalDate.parse(ix(p, 10)));
    }

    private void fillFrom13(String[] p) {
        studentName.setText(ix(p, 0));
        className.setText(ix(p, 1));
        tuition.setText(ix(p, 2));
        total.setText(ix(p, 8));
        paid.setText(ix(p, 9));
        remaining.setText(ix(p, 10));
        dueDate.setValue(ix(p, 11).isBlank() ? null : java.time.LocalDate.parse(ix(p, 11)));
        paymentDate.setValue(ix(p, 12).isBlank() ? null : java.time.LocalDate.parse(ix(p, 12)));
    }

    private static String ix(String[] p, int i) {
        return i < p.length ? p[i] : "";
    }

    private void calculate() {
        try {
            double tu = d(tuition);
            double p = d(paid);
            total.setText(String.format("%.2f", tu));
            double rem = tu - p;
            remaining.setText(String.format("%.2f", rem));
            status.setValue(feeStatus(tu, p, rem));
        } catch (Exception ex) {
            AlertUtil.error("Check amounts: " + ex.getMessage());
        }
    }

    private boolean valid() {
        if (InputValidator.isEmpty(studentId.getText()) || InputValidator.isEmpty(month.getText())) {
            AlertUtil.warning("Student ID and month are required.");
            return false;
        }
        if (total.getText().isBlank()) calculate();
        return !total.getText().isBlank();
    }

    private EntityRecord build() {
        calculate();
        String blob = String.join("~",
                studentName.getText().trim(),
                className.getText().trim(),
                tuition.getText().trim(),
                total.getText().trim(),
                paid.getText().trim(),
                remaining.getText().trim(),
                dueDate.getValue() == null ? "" : dueDate.getValue().toString(),
                paymentDate.getValue() == null ? "" : paymentDate.getValue().toString());
        return new EntityRecord(
                id.getText(),
                studentId.getText().trim(),
                month.getText().trim(),
                blob,
                status.getValue());
    }

    private void addDo() {
        if (!valid()) return;
        var l = new ArrayList<>(service.getAll());
        l.add(build());
        service.saveAll(l);
        AlertUtil.success("Fee saved.");
        resetForm();
        ref();
    }

    private void updDo() {
        if (!valid()) return;
        var l = new ArrayList<>(service.getAll());
        for (int i = 0; i < l.size(); i++) {
            if (l.get(i).getId().equals(id.getText())) {
                l.set(i, build());
                service.saveAll(l);
                AlertUtil.success("Fee updated.");
                ref();
                return;
            }
        }
    }

    private void delDo() {
        if (!AlertUtil.confirm("Remove this fee row?")) return;
        var l = new ArrayList<>(service.getAll());
        l.removeIf(r -> r.getId().equals(id.getText()));
        service.saveAll(l);
        resetForm();
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

    private void resetForm() {
        id.setText(IdGenerator.nextId(
                "FEE",
                service.getAll().stream().map(EntityRecord::getId).collect(Collectors.toSet())));
        for (TextField f : List.of(studentId, studentName, className, month)) {
            f.clear();
        }
        dueDate.setValue(null);
        paymentDate.setValue(null);
        tuition.setText("0");
        paid.setText("0");
        total.clear();
        remaining.clear();
        status.setValue("UNPAID");
    }

    private double d(TextField f) {
        if (!InputValidator.isNumber(f.getText())) throw new IllegalArgumentException("numeric");
        return Double.parseDouble(f.getText());
    }

    private static String feeStatus(double total, double paid, double remaining) {
        if (paid <= 0) return "UNPAID";
        if (remaining <= 0 || paid >= total) return "PAID";
        return "PARTIALLY_PAID";
    }
}
