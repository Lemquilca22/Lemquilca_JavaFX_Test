module org.example.paintchafa {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens org.example.paintchafa to javafx.fxml;
    exports org.example.paintchafa;
}