module rsprojekat.rsprojekat {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires jbcrypt;
    requires java.desktop;
    requires java.naming;
    requires javafx.graphics;
    requires openhtmltopdf.pdfbox;
    opens controllers to javafx.fxml;
    opens models to javafx.fxml, javafx.base;
    opens dao to javafx.fxml;
    opens utils to javafx.fxml;
    opens views to javafx.fxml;
    opens rsprojekat.rsprojekat to javafx.fxml;
    exports rsprojekat.rsprojekat;
}