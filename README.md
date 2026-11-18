# DeskFlow API – Sistema de Gestión de Tickets e Incidentes

---

## 📋 Información de la Aplicación

### ¿Qué es DeskFlow API?

DeskFlow API es un servicio backend RESTful robusto diseñado para la gestión integral de tickets de soporte e incidentes. Proporciona un sistema completo de mesa de ayuda con autenticación personalizada, control de acceso basado en roles (RBAC), gestión del ciclo de vida de tickets, categorización, y almacenamiento de archivos en la nube.

### Funcionalidades Principales

#### Autenticación y Seguridad

- **Autenticación JWT**: Uso de JSON Web Tokens (Stateless) para la autenticación segura
- **Control de acceso basado en roles (RBAC)**: Sistema de permisos con 4 roles diferentes (USER, PREMIUM, AGENT, ADMIN)
- **Expiración de tokens**: Configurable mediante propiedades (default 24 horas)
- **Seguridad stateless**: No requiere almacenamiento de sesión en servidor
- **Encriptación de contraseñas**: Uso de BCrypt para hash seguro de contraseñas

#### Gestión de Tickets

- **Ciclo de vida completo**: Creación, actualización, consulta y eliminación de tickets.
- **Asignación Automática**: Los tickets se asignan automáticamente al "Mejor Agente Disponible" (menor carga de trabajo) al momento de su creación.
- **Estados de tickets**: `OPEN`, `IN_PROGRESS`, `CLOSED`, `RESOLVED`.
- **Prioridades**: `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`.
- **Categorización**: Organización de tickets por categorías personalizables.
- **Tickets de demostración**: Acceso público a tickets de ejemplo sin autenticación.

#### Gestión de Archivos

- **Almacenamiento en Cloudinary**: Subida y eliminación de archivos adjuntos a tickets
- **Acceso restringido**: Solo usuarios `PREMIUM` y `ADMIN` pueden subir/eliminar archivos

#### Tareas Automatizadas

- **Limpieza programada**: Eliminación automática de tickets antiguos (más de 3 meses) ejecutada el ultimo día del mes a la 1:00 AM

### Tecnologías Implementadas

#### Framework y Lenguaje

- **Java 17**: Lenguaje de programación
- **Spring Boot 4.0.0**: Framework principal para desarrollo de aplicaciones Java
- **Spring Web MVC**: Framework para construcción de APIs REST
- **Spring Data JPA**: Abstracción para acceso a datos y persistencia
- **Spring Security**: Framework de seguridad y autenticación
- **Spring Actuator**: Monitoreo y métricas de la aplicación

#### Base de Datos

- **PostgreSQL 15**: Base de datos relacional
- **Hibernate**: ORM (Object-Relational Mapping) para JPA
- **Docker Compose**: Orquestación del contenedor de PostgreSQL

#### Servicios Externos

- **Cloudinary**: Servicio de gestión de imágenes y archivos en la nube
  - SDK: `cloudinary-http5` versión 2.0.0

#### Utilidades y Herramientas

- **Lombok 1.18.42**: Reducción de código boilerplate (getters, setters, builders, etc.)
- **JJWT 0.11.5**: Librería para generación y validación de JSON Web Tokens
- **Bean Validation**: Validación de datos de entrada
- **Spring DevTools**: Herramientas de desarrollo para recarga automática

#### Arquitectura

- **Arquitectura en capas**: Separación clara entre Controladores, Servicios, Repositorios y Entidades
- **DTOs (Data Transfer Objects)**: Objetos para transferencia de datos entre capas
- **Manejo centralizado de excepciones**: `GlobalExceptionHandler` para gestión de errores
- **Filtros de seguridad personalizados**: `AuthFilter` para validación de tokens en cada request


---

## 🚀 Información de Uso

### Requisitos Previos

Antes de ejecutar la aplicación, asegúrate de tener instalado:

- **Java Development Kit (JDK) 17** o superior
- **Docker** y **Docker Compose** (para la base de datos PostgreSQL)
- **Maven** (o usar el wrapper `mvnw` incluido en el proyecto)

### Configuración Inicial

#### 1. Iniciar la Base de Datos

El proyecto utiliza PostgreSQL 15 en un contenedor Docker. Para iniciarlo:

```bash
docker compose up -d
```

Esto levantará un contenedor PostgreSQL con las siguientes configuraciones:

- **Puerto**: 5432
- **Base de datos**: `deskflow_db`
- **Usuario**: `postgres`
- **Contraseña**: `postgres`

#### 2. Configurar Variables de Entorno

La aplicación utiliza perfiles de Spring Boot. El perfil activo por defecto es `dev`:

**Configuración de Desarrollo** (`application-dev.properties`):

- Puerto del servidor: `8080`
- Expiración de tokens: `24 horas`
- Hibernate DDL: `update` (actualiza el esquema automáticamente)
- SQL logging: Habilitado

**Configuración de Producción** (`application-prod.properties`):

- Expiración de tokens: `168 horas` (7 días)
- Hibernate DDL: `validate` (solo valida el esquema)

#### 3. Configurar Cloudinary

La aplicación requiere credenciales de Cloudinary. Estas se configuran en `application.properties`:

- `cloudinary.cloud_name`
- `cloudinary.api_key`
- `cloudinary.api_secret`

#### 4. Configurar JWT

Se recomieda configurar el secreto de JWT en variable de entorno:

- `JWT_SECRET`: Clave secreta para firmar los tokens (min 256 bits)

### Ejecutar la Aplicación

#### Opción 1: Usando Maven Wrapper (Recomendado)

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

#### Opción 2: Usando Maven instalado

```bash
mvn spring-boot:run
```

#### Opción 3: Compilar y ejecutar JAR

```bash
mvn clean package
java -jar target/deskflow-1.0.0.jar
```

### Acceso a la Aplicación

Una vez iniciada, la API estará disponible en:

- **URL Base**: `http://localhost:8080`
- **Endpoints públicos**: Accesibles sin autenticación
- **Endpoints protegidos**: Requieren token de autenticación en el header `Authorization: Bearer <token>`

### Flujo de Autenticación

### Flujo de Autenticación

1. **Registro de Usuario**:

   - POST `/auth/register` con email, contraseña y nombre completo
   - Recibe un token JWT (String) en el cuerpo de la respuesta

2. **Inicio de Sesión**:

   - POST `/auth/login` con email y contraseña
   - Recibe un token JWT (String) en el cuerpo de la respuesta

3. **Uso de la API**:

   - Incluir el token en el header: `Authorization: Bearer <token>`
   - El token se valida automáticamente en cada request mediante `AuthFilter` (verifica firma y expiración)

4. **Cierre de Sesión**:

   - Al ser stateless, el cierre de sesión se maneja en el cliente eliminando el token.
   - El endpoint `/auth/logout` está disponible pero no realiza acciones en servidor (stateless).

5. **Validación de Token**:
   - GET `/auth/validate` para verificar si un token es válido y obtener información del usuario

### Roles y Permisos

La aplicación implementa 4 roles con diferentes niveles de acceso para garantizar una gestión eficiente:

| Rol | Descripción | Capacidades Clave |
| :--- | :--- | :--- |
| **USER** | Usuario estándar | Puede crear, ver y gestionar sus propios tickets. No tiene acceso a archivos adjuntos. |
| **PREMIUM** | Usuario con beneficios | Todas las funciones de USER + **Gestión de archivos adjuntos** (subida y eliminación) para mayor detalle en sus reportes. |
| **AGENT** | Personal de soporte | Puede gestionar, comentar y resolver los tickets que tiene asignados. |
| **ADMIN** | Administrador | Control total del sistema, incluyendo la asignación manual de agentes a cualquier ticket. |

### Sistema de Asignación de Agentes

El flujo de asignación está diseñado para maximizar la velocidad de respuesta:

1.  **Asignación Equitativa**: No existe una distinción en la cola de agentes entre usuarios `USER` y `PREMIUM`. Ambos reciben atención de los mismos agentes capacitados.
2.  **Criterio de Carga**: El sistema busca automáticamente al agente con el menor número de tickets en estado `OPEN` para balancear el trabajo.
3.  **Transparencia**: El usuario puede ver quién es su agente asignado en todo momento desde el detalle del ticket.
4.  **Asignación Manual**: Los administradores pueden reasignar tickets si un caso requiere un experto específico o para ajustar cargas de trabajo críticas.

### Endpoints Públicos vs Protegidos

**Endpoints Públicos** (sin autenticación):

- `/auth/**` - Registro, login, logout, validación
- `/public/tickets` - Lista de tickets de demostración
- `/public/tickets/{id}` - Detalle de ticket de demostración
- `/categories` - Gestión de categorías

**Endpoints Protegidos** (requieren autenticación):

- `/tickets` - Gestión de tickets del usuario autenticado
- `/tickets/{id}/files` - Gestión de archivos adjuntos

---

## 🔒 Seguridad y Consideraciones

### Autenticación

- Uso de **JSON Web Tokens (JWT)** firmados con algoritmo HS256
- Los tokens son **stateless**, no se almacenan en base de datos
- Expiración configurable en `application.properties`
- Las contraseñas se encriptan con BCrypt antes de almacenarse

### Validación de Datos

- Se utiliza Bean Validation para validar datos de entrada
- Los DTOs incluyen anotaciones de validación (`@NotBlank`, `@Email`, `@Size`, etc.)
- Los errores de validación se manejan centralmente mediante `GlobalExceptionHandler`

### Control de Acceso

- Los endpoints públicos no requieren autenticación
- Los endpoints protegidos validan el token en cada request
- Los archivos adjuntos están restringidos a usuarios PREMIUM y ADMIN
- Los usuarios solo pueden acceder a sus propios tickets

### Manejo de Errores

- Errores HTTP estándar (400, 401, 403, 404, 500)
- Mensajes de error descriptivos en formato JSON
- Manejo centralizado de excepciones mediante `GlobalExceptionHandler`

---

## 📝 Notas Adicionales

### Timestamps

- Todos los timestamps se manejan en formato UTC (`OffsetDateTime`)
- Se establecen automáticamente al crear entidades mediante `@PrePersist`

### Base de Datos

- El esquema se actualiza automáticamente en desarrollo (`hibernate.ddl-auto=update`)
- En producción se recomienda usar `validate` para evitar cambios no deseados
- La limpieza automática elimina tickets con más de 3 meses de antigüedad

### Cloudinary

- Los archivos se almacenan en Cloudinary, no localmente
- Se requiere configuración de credenciales en `application.properties`
- Los archivos eliminados también se eliminan de Cloudinary

### Desarrollo

- Spring DevTools está habilitado para recarga automática en desarrollo
- El logging SQL está habilitado en desarrollo para debugging
- Spring Actuator está disponible para monitoreo (endpoints en `/actuator`)

---

## 📚 Estructura del Proyecto

```
deskflow/
├── src/
│   ├── main/
│   │   ├── java/com/github/oscarmgh/deskflow/
│   │   │   ├── config/          # Configuraciones (Security, Cloudinary, DataLoader)
│   │   │   ├── controllers/     # Controladores REST
│   │   │   ├── dtos/            # Data Transfer Objects
│   │   │   ├── entities/        # Entidades JPA
│   │   │   ├── exceptions/      # Excepciones personalizadas
│   │   │   ├── repositories/    # Repositorios JPA
│   │   │   ├── security/        # Filtros de seguridad
│   │   │   └── services/        # Lógica de negocio
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/                    # Tests unitarios
├── docker-compose.yml           # Configuración de PostgreSQL
├── pom.xml                      # Dependencias Maven
└── README.md                    # Este archivo
```

**Versión**: 1.0.0
