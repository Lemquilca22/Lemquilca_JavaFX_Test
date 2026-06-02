# Weather Desktop App 🌍🌦️

¡Bienvenido a la **Weather Desktop App**! Esta es una aplicación de escritorio moderna y reactiva diseñada para la consulta del clima en tiempo real, desarrollada en **Java** utilizando **JavaFX** para la interfaz gráfica y estructurada bajo el patrón arquitectónico **MVC (Modelo-Vista-Controlador)**.

La aplicación se conecta de forma dinámica a la API pública de **Open-Meteo** para obtener datos meteorológicos precisos y actualizados sin necesidad de claves de API complejas, procesando las respuestas e integrando elementos visuales dinámicos basados en el estado del tiempo.

---

## Características

* **Arquitectura MVC Limpia:** Separación estricta entre la lógica de conexión/parseo de datos (Model), el diseño visual en FXML (View) y la gestión de eventos y actualización de componentes (Controller).
* **Consumo de API REST:** Integración nativa para realizar peticiones HTTP a la API de **Open-Meteo** y procesar respuestas en formato JSON.
* **Interfaz Gráfica Reactiva con JavaFX:** Uso de componentes visuales optimizados para mostrar temperaturas, condiciones climáticas y pronósticos de forma clara.
* **Iconografía Dinámica:** Integración de componentes visuales e iconos que cambian según el estado del tiempo actual (sol, lluvia, nubes, etc.).
* **Optimización de Recursos:** Enfoque en la lógica fundamental de Java y JavaFX, evitando sobrecargar el proyecto con librerías externas innecesarias para mantener un rendimiento ágil.

---

## Tecnologías y Herramientas

* **Lenguaje:** Java SE (JDK 11 o superior)
* **Framework UI:** JavaFX (con FXML para las vistas)
* **Arquitectura:** Modelo-Vista-Controller (MVC)
* **API de Datos:** Open-Meteo API
* **Entorno de Desarrollo:** IntelliJ IDEA / Eclipse
* **Control de Versiones:** Git & GitHub

---

## Estructura General del Proyecto

El proyecto está organizado bajo la estructura estándar modular de JavaFX, separando limpiamente las clases lógicas de los recursos de la interfaz gráfica:

```text
src/main/
├── java/
│   └── org/example/aplicacion_tiempo/
│       ├── AplicacionControlador.java  # Controlador encargado de la lógica de la UI y eventos
│       ├── HelloApplication.java       # Configuración inicial de la Scene y Stage de JavaFX
│       ├── Launcher.java               # Punto de entrada (Main) para inicializar la app sin conflictos
│       └── ServicioClima.java          # Modelo encargado de la lógica y conexión con la API del clima
│   └── module-info.java                # Configuración de módulos y dependencias de JavaFX
│
└── resources/org/example/aplicacion_tiempo/
    ├── aplicacionTiempo.fxml           # Diseño de la interfaz gráfica en formato XML
    └── images/                         # Iconografía dinámica del estado del tiempo
        ├── humedad.png
        ├── lluvia.png
        ├── nieve.png
        ├── parcialnublado.png
        └── soleado.png
