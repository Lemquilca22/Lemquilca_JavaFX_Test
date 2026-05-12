package org.example.aplicacion_tiempo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class AplicacionControlador {
    @FXML
    private TextField txtCiudad;

    @FXML
    private Label lblTemp, lblDescripcion, lblHumedad, lblViento;

    @FXML
    private void onConsultarClick() {
        System.out.println("Botón pulsado!");
    }
}
