module org.example.aplicacion_tiempo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens org.example.aplicacion_tiempo to javafx.fxml;
    exports org.example.aplicacion_tiempo;
}