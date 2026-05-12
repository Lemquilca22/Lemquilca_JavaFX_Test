package org.example.aplicacion_tiempo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;


public class AplicacionControlador {
    @FXML
    private TextField txtCiudad;

    @FXML
    private Label lblTemp, lblDescripcion, lblHumedad, lblViento;
    private final java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();

    @FXML
    private void onConsultarClick() {
        String ciudad = txtCiudad.getText().trim();
        if (ciudad.isEmpty()) return;

        lblDescripcion.setText("Buscando...");

        CompletableFuture.runAsync(() -> {
            try {
                // 1. Obtener coordenadas
                String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name="
                        + ciudad.replace(" ", "%20") + "&count=1&language=es&format=json";

                String geoJson = enviarConsulta(geoUrl);

                // Imprime en consola los valores de la Api que mandaste
                System.out.println("DEBUG Geo JSON: " + geoJson);

                if (!geoJson.contains("\"results\":[")) {
                    actualizarUI("Ciudad no encontrada", "--", "--", "--");
                    return;
                }

                double lat = extraerDato(geoJson, "\"latitude\":");
                double lon = extraerDato(geoJson, "\"longitude\":");
                
                String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + lat
                        + "&longitude=" + lon
                        + "&current=temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code&timezone=auto";

                String weatherJson = enviarConsulta(weatherUrl);
                System.out.println("DEBUG Weather JSON: " + weatherJson);

                // 3. Extraer datos finales
                double temp = extraerDato(weatherJson, "\"temperature_2m\":");
                double hum = extraerDato(weatherJson, "\"relative_humidity_2m\":");
                double viento = extraerDato(weatherJson, "\"wind_speed_10m\":");
                int code = (int) extraerDato(weatherJson, "\"weather_code\":");

                javafx.application.Platform.runLater(() -> {
                    lblTemp.setText(temp + "°C");
                    lblHumedad.setText("Humedad: " + hum + "%");
                    lblViento.setText("Viento: " + viento + " km/h");
                    lblDescripcion.setText(interpretarClima(code));
                });

            } catch (Exception e) {
                // ESTO TE DIRÁ EL ERROR REAL EN LA CONSOLA ROJA
                e.printStackTrace();
                actualizarUI("Error de conexión o datos", "--", "--", "--");
            }
        });
    }

    private String enviarConsulta(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private double extraerDato(String json, String clave) {
        try {
            // Busca la ULTIMA aparición de la clave para saltar las unidades
            // e ir directos a la sección "current" del JSON.
            int start = json.lastIndexOf(clave) + clave.length();

            // Busca dónde termina el valor (ya sea en una coma o un cierre de llave)
            int end = json.indexOf(",", start);
            if (end == -1 || (json.indexOf("}", start) != -1 && json.indexOf("}", start) < end)) {
                end = json.indexOf("}", start);
            }

            // Limpia el texto de dos puntos, comillas y espacios para dejar solo el número
            String valor = json.substring(start, end)
                    .replace(":", "")
                    .replace("\"", "")
                    .trim();

            return Double.parseDouble(valor);
        } catch (Exception e) {
            System.err.println("Error procesando la clave: " + clave);
            throw e;
        }
    }

    private void actualizarUI(String desc, String temp, String hum, String vnt) {
        javafx.application.Platform.runLater(() -> {
            lblDescripcion.setText(desc);
            lblTemp.setText(temp);
            lblHumedad.setText(hum);
            lblViento.setText(vnt);
        });
    }

    private String interpretarClima(int code) {
        return switch (code) {
            case 0 -> "Cielo despejado";
            case 1, 2, 3 -> "Parcialmente nublado";
            case 45, 48 -> "Niebla";
            case 51, 53, 55 -> "Llovizna";
            case 61, 63, 65 -> "Lluvia";
            case 71, 73, 75 -> "Nieve";
            case 95 -> "Tormenta";
            default -> "Clima desconocido";
        };
    }
}
