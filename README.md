# Creciendo Juntxs - Sistema de Monitoreo

Sistema integral de gestión para la guardería de la Universidad Nacional de La Rioja (UNLaR). 

## 🚀 Descripción
Plataforma orientada a estudiantes y personal docente/nodocente que permite el seguimiento de infantes, control de estados y monitoreo de seguridad en tiempo real.

## 🛠 Tecnologías Utilizadas
- **Backend:** Java, Spring Boot
- **Frontend:** HTML5, CSS3, Thymeleaf, JavaScript
- **Gestión:** Git / GitHub

## 📝 Características
- [x] Gestión de infantes y tutores.
- [x] Control de estados en tiempo real.
- [x] Módulo de monitoreo seguro.
- [x] Historial diario de actividades del infante.
- [x] Registro de ingreso, cambios de estado, alertas y egresos.
- [x] Línea de tiempo visual para consultar la jornada del infante.

## Historial Diario de Actividades
El sistema registra eventos diarios del infante para reconstruir su jornada dentro de la guardería.

Eventos registrados:
- `INGRESO`
- `CAMBIO_ESTADO`
- `ALERTA`
- `EGRESO`
- `NOTA`

Cada evento guarda fecha/hora, estado anterior, estado nuevo, descripción, prioridad, canal de notificación y relación con el infante/asistencia.

La vista se consulta desde `/infantes/historial/{id}`. La bitácora anterior se mantiene por compatibilidad.

## Base de Datos
El historial diario agrega la tabla `actividades_infante`, relacionada con `infantes`, `asistencias` y `usuarios`.

## Cámara IP
El módulo de monitoreo usa la variable `CAMARA_STREAM_URL` para embeber una URL HTTP intermedia de MediaMTX, por ejemplo [http://localhost:8888/sala1](http://localhost:8888/sala1). No debe configurarse con una URL RTSP directa.

Opcionalmente se puede definir `CAMARA_NOMBRE`; si no se informa, se muestra `Sala principal`.

Opcionalmente se usa `CAMARA_SALA_ID` para asociar el monitoreo a una sala autorizada.

El historial diario no modifica la lógica de autorización de cámara ni consentimiento.
