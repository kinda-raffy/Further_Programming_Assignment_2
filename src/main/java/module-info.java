module fp.assignments.assignment_2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens fp.assignments.assignment_2 to javafx.fxml;

    exports fp.assignments.assignment_2;

    opens fp.assignments.assignment_2.controller to javafx.fxml;
    opens fp.assignments.assignment_2.model to javafx.fxml;

    exports fp.assignments.assignment_2.controller;
    exports fp.assignments.assignment_2.model;
    exports fp.assignments.assignment_2.service;
}