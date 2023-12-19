module org.jxch.copyplus.copyplus {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens org.jxch.copyplus.copyplus to javafx.fxml;
    exports org.jxch.copyplus.copyplus;
}