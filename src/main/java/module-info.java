module fp.assignments.assignment_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens fp.assignments.assignment_2 to javafx.fxml, javafx.graphics, javafx.base, javafx.controls;
    opens fp.assignments.assignment_2.controller to javafx.fxml, javafx.graphics, javafx.base, javafx.controls;
    opens fp.assignments.assignment_2.model to javafx.base, javafx.fxml;

    exports fp.assignments.assignment_2;
    exports fp.assignments.assignment_2.controller;
    exports fp.assignments.assignment_2.model;
    exports fp.assignments.assignment_2.service;
}