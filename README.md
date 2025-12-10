# DeskFlow API – Sistema de Gestión de Tickets e Incidentes

DeskFlow API es un servicio backend RESTful robusto diseñado para la gestión integral de tickets de soporte e incidentes. Proporciona autenticación, control de acceso basado en roles (RBAC), datos de demostración públicos y manejo completo del ciclo de vida de los tickets. El objetivo del proyecto es simular un backend de mesa de ayuda del mundo real con una arquitectura escalable y características listas para entornos empresariales.

---

## ✨ Características Principales

### Funcionalidad Base

* **Autenticación Personalizada:** Registro de usuarios y autenticación segura mediante tokens personalizados (no JWT).
* **Acceso Público:** Los datos de tickets de demostración son accesibles públicamente sin necesidad de autenticación.
* **Gestión del Ciclo de Vida:** Los usuarios autenticados pueden crear tickets y gestionar su estado (ej., `OPEN`, `IN_PROGRESS`, `CLOSED`).
* **Control de Acceso (RBAC):** Implementación de roles (`USER`, `PREMIUM`, `AGENT`, `ADMIN`) para asignar diferentes permisos.

### Características Avanzadas

* **Categorización:** Organización de tickets por categorías específicas.
* **Almacenamiento en la Nube:** Manejo de archivos adjuntos de tickets a través de **Cloudinary** (funcionalidad limitada a usuarios `PREMIUM`).
* **Seguridad:** Caducidad de tokens y funcionalidad de cierre de sesión seguro.
* **Estandarización de Tiempo:** Todos los *timestamps* son manejados en **UTC** para consistencia global.

---

## ⚙️ Configuración y Ejecución Local

Para poner en marcha la DeskFlow API en tu entorno local, sigue los siguientes pasos.

### 1. Requisitos Indispensables

Asegúrate de tener instalados los siguientes componentes:

* **Java Development Kit (JDK) 17**
* **Docker & Docker Compose** (para la base de datos PostgreSQL)
* **Maven** (el proyecto incluye el `mvnw` *wrapper*)

### 2. Iniciar la Base de Datos con Docker

El proyecto utiliza un contenedor de **PostgreSQL 15** para la base de datos, el cual se inicializa mediante `docker-compose.yml`.

Ejecuta el siguiente comando en la raíz del proyecto para levantar el contenedor en segundo plano:

```bash
docker compose up -d
```