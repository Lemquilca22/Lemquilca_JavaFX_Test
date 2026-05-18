package org.example.aplicacion_tiempo;

public class ServicioClima {

    public double extraerDato(String json, String clave) {
        try {
            int start = json.lastIndexOf(clave) + clave.length();
            int end = json.indexOf(",", start);
            if (end == -1 || (json.indexOf("}", start) != -1 && json.indexOf("}", start) < end)) {
                end = json.indexOf("}", start);
            }

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

    public String interpretarClima(int code) {
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