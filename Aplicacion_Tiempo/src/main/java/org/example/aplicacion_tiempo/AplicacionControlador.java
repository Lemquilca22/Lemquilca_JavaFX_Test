package org.example.aplicacion_tiempo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image; // <-- Añadir esta importación
import javafx.scene.image.ImageView; // <-- Añadir esta importación

import java.io.InputStream; // <-- Añadir esta importación
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class AplicacionControlador {
    @FXML
    private TextField txtCiudad;

    @FXML
    private Label lblTemp, lblDescripcion, lblHumedad, lblViento;

    @FXML
    private ImageView imgClima;

    private final java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
    private final ServicioClima servicioClima = new ServicioClima();

    @FXML
    private void onConsultarClick() {
        String ciudad = txtCiudad.getText().trim();
        if (ciudad.isEmpty()) return;

        lblDescripcion.setText("Buscando...");

        CompletableFuture.runAsync(() -> {
            try {
                // Obtener coordenadas
                String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name="
                        + ciudad.replace(" ", "%20") + "&count=1&language=es&format=json";

                String geoJson = enviarConsulta(geoUrl);

                System.out.println("DEBUG Geo JSON: " + geoJson);

                if (!geoJson.contains("\"results\":[")) {
                    actualizarUI("Ciudad no encontrada", "--", "--", "--");
                    return;
                }

                // 2. Usamos el servicio para extraer los datos de geolocalización
                double lat = servicioClima.extraerDato(geoJson, "\"latitude\":");
                double lon = servicioClima.extraerDato(geoJson, "\"longitude\":");

                String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + lat
                        + "&longitude=" + lon
                        + "&current=temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code&timezone=auto";

                String weatherJson = enviarConsulta(weatherUrl);
                System.out.println("DEBUG Weather JSON: " + weatherJson);

                // 3. Usamos el servicio para extraer los datos del clima
                double temp = servicioClima.extraerDato(weatherJson, "\"temperature_2m\":");
                double hum = servicioClima.extraerDato(weatherJson, "\"relative_humidity_2m\":");
                double viento = servicioClima.extraerDato(weatherJson, "\"wind_speed_10m\":");
                int code = (int) servicioClima.extraerDato(weatherJson, "\"weather_code\":");

                javafx.application.Platform.runLater(() -> {
                    lblTemp.setText(temp + "°C");
                    lblHumedad.setText("Humedad: " + hum + "%");
                    lblViento.setText("Viento: " + viento + " km/h");
                    // 4. Usamos el servicio para interpretar el código del clima
                    lblDescripcion.setText(servicioClima.interpretarClima(code));

                    actualizarIconoClima(code);
                });

            } catch (Exception e) {
                e.printStackTrace();
                actualizarUI("Error de conexión o datos", "--", "--", "--");
            }
        });
    }

    private String enviarConsulta(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private void actualizarUI(String desc, String temp, String hum, String vnt) {
        javafx.application.Platform.runLater(() -> {
            lblDescripcion.setText(desc);
            lblTemp.setText(temp);
            lblHumedad.setText(hum);
            lblViento.setText(vnt);
            // Si hay error, podemos limpiar la imagen o poner una por defecto
            actualizarIconoClima(-1);
        });
    }

    private void actualizarIconoClima(int codigoClima) {
        String rutaImagen = "";

        // Mapeo según la documentación oficial de Open-Meteo WMO Code
        switch (codigoClima) {
            case 0: // Cielo despejado
                rutaImagen = "images/soleado.png";
                break;

            case 1: case 2: case 3: // Principalmente despejado, parcialmente nublado y nublado
                rutaImagen = "images/parcialnublado.png";
                break;

            case 51: case 53: case 55: // Llovizna
            case 61: case 63: case 65: // Lluvia débil, moderada y fuerte
            case 80: case 81: case 82: // Chubascos de lluvia
                rutaImagen = "images/lluvia.png";
                break;

            case 71: case 73: case 75: // Nieve
            case 77: // Granos de nieve
            case 85: case 86: // Chubascos de nieve
                rutaImagen = "images/nieve.png";
                break;

            case 95: // Tormenta eléctrica
            case 96: case 99: // Tormenta con granizo
                rutaImagen = "images/tormenta.png";
                break;

            default:
                rutaImagen = "images/soleado.png";
                break;
        }

        try {
            InputStream input = getClass().getResourceAsStream(rutaImagen);
            if (input != null) {
                Image imagenNueva = new Image(input);
                imgClima.setImage(imagenNueva);
            } else {
                System.err.println("No se encontró el archivo en: resources/" + getClass().getPackageName().replace(".", "/") + "/" + rutaImagen);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }
    }
}