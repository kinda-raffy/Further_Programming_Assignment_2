module fp.assignments.assignment_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires transitive javafx.graphics;
    requires transitive java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens fp.assignments.assignment_2 to javafx.fxml, javafx.graphics, javafx.base, javafx.controls;
    opens fp.assignments.assignment_2.controller to javafx.fxml, javafx.graphics, javafx.base, javafx.controls;
    opens fp.assignments.assignment_2.controller.form to javafx.fxml;
    opens fp.assignments.assignment_2.controller.home to javafx.fxml;
    opens fp.assignments.assignment_2.controller.home.detail to javafx.fxml;
    opens fp.assignments.assignment_2.controller.home.pane to javafx.fxml;
    opens fp.assignments.assignment_2.controller.landing to javafx.fxml;
    opens fp.assignments.assignment_2.model.entity to javafx.base, javafx.fxml;
    opens fp.assignments.assignment_2.model.backup to javafx.base, javafx.fxml;

    exports fp.assignments.assignment_2;
    exports fp.assignments.assignment_2.controller;
    exports fp.assignments.assignment_2.controller.form;
    exports fp.assignments.assignment_2.controller.home;
    exports fp.assignments.assignment_2.controller.home.detail;
    exports fp.assignments.assignment_2.controller.home.pane;
    exports fp.assignments.assignment_2.controller.landing;
    exports fp.assignments.assignment_2.model.entity;
    exports fp.assignments.assignment_2.model.backup;
    exports fp.assignments.assignment_2.service;
}