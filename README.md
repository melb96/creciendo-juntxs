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

## Camara IP
El modulo de monitoreo usa la variable `CAMARA_STREAM_URL` para embeber una URL HTTP intermedia de MediaMTX, por ejemplo [http://localhost:8888/sala1](http://localhost:8888/sala1). No debe configurarse con una URL RTSP directa.

Opcionalmente se puede definir `CAMARA_NOMBRE`; si no se informa, se muestra `Sala principal`.
