package org.example.aplicacion_tiempo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServicioClimaTest {

    private final ServicioClima servicio = new ServicioClima();

    @Test
    public void testInterpretarClimaDespejado() {
        String resultado = servicio.interpretarClima(0);
        assertEquals("Cielo despejado", resultado);
    }

    @Test
    public void testInterpretarClimaDesconocido() {
        String resultado = servicio.interpretarClima(999);
        assertEquals("Clima desconocido", resultado);
    }

    @Test
    public void testExtraerDatoJsonSimulado() {
        String jsonSimulado = "{\"current\":{\"temperature_2m\":22.5,\"relative_humidity_2m\":60}}";

        double temp = servicio.extraerDato(jsonSimulado, "\"temperature_2m\":");

        assertEquals(22.5, temp, 0.001);
    }
}