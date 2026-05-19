package org.example.aplicacion_tiempo;

public class ServicioClima {

    public double extraerDato(String json, String clave) {
        try {
            int start = json.lastIndexOf(clave) + clave.length();
            int end = json.indexOf(",", start);
            if (end == -1 || (json.indexOf("}", start) != -1 && json.indexOf("}", start) < end)) {
                end = json.indexOf("}", start);
            }
            //El LastindexOf lee de derecha a izquierda, hasta encontrar la primera coma que aparece desde nuestro indice Start
            //Mientras que el indexOf recorre de izquierda a derecha busca el valor ","
            //El if que le sigue sirve por si el valor es el ultimo que aparece y en lugar de una coma, aparezca una comilla.

            String valor = json.substring(start, end)
                    .replace(":", "")
                    .replace("\"", "")
                    .trim();
            //Esto sirve para reemplazar los ":" o "\"" que podrian aparecer y hacer que el codigo no esté limpio.
            //El metodo sub String corta un fragmento especifico entre dos posiciones.
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