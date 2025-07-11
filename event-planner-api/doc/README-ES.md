# 🗓️ Personal Event Planner API - Spring Boot & JWT

Este repositorio contiene el código fuente y la documentación técnica del proyecto final del portafolio: API REST
en la cual cada usuario autenticado puede gestionar sus eventos personales.

> Para ver la versión en inglés, visite `README-EN.md`.
> To view the English version, visit `README-EN.md`.

## 📌 Descripción del Proyecto

API para gestión personal de eventos (recordatorios, reuniones, tareas) con autenticación basada en roles. El sistema permite:

- Crear, editar y eliminar eventos personales
- Gestionar usuarios y permisos
- Acceso diferenciado según rol (USER/ADMIN)
---

## 🧑‍💼 Roles y funcionalidades

| Rol      | Funcionalidades principales                                                                 |
|----------|---------------------------------------------------------------------------------------------|
| USER     | Gestionar sus propios eventos (CRUD completo)                                               |
| ADMIN    | Listar todos los usuarios/eventos, crear nuevos administradores                             |

---

## 🔒 Gestión de accesos

**Todos los usuarios** pueden:
- Actualizar su perfil y credenciales
- Eliminar su propia cuenta

**Administradores** adicionalmente pueden:
- Listar todos los eventos del sistema
- Ver información de cualquier usuario

⚠️ La eliminación de usuarios/eventos sigue las reglas de negocio:
- Usuarios normales solo eliminan sus propios eventos
- Admins pueden eliminar cualquier recurso

---

## ⚙️ Tecnologías Utilizadas

- **Spring Boot 3.x** (Framework principal)
- **Spring Security + JWT** (Autenticación/autorización)
- **JPA/Hibernate** (Persistencia)
- **MySQL** (Base de datos)
- **MapStruct** (Mapeo entre DTOs/entidades)
- **Maven** (Gestión de dependencias)
- **BCrypt** (Hashing de contraseñas)
- **Lombok**
---

## 🏗️ Estructura del Proyecto

### 🗂️ Paquetes principales

| Paquete          | Componentes clave                                                                 | Responsabilidad                                                                 |
|------------------|-----------------------------------------------------------------------------------|---------------------------------------------------------------------------------|
| `config`         | `ValidationConfig`                                                                | Configura mensajes de validación para DTOs                                      |
| `controllers`    | `EventController`, `UserController`                                               | Contiene todos los endpoints REST de la API                                     |
| `exceptions`    | `AdminAccessRequiredException`                                                    | Excepciones personalizadas del negocio                                          |
| `mappers`       | `EventMapper`, `UserMapper`                                                       | Conversión entre Entidades y DTOs                                               |

### 🔐 Seguridad (JWT)

| Subpaquete       | Componentes                                                                       | Función                                                                         |
|------------------|-----------------------------------------------------------------------------------|---------------------------------------------------------------------------------|
| `auth`           | `JwtAuthenticationFilter`, `JwtValidationFilter`                                  | Filtros para autenticación JWT                                                  |
| `config`         | `SecurityConfig`, `TokenJwtConfig`                                                | Configuración global de seguridad y constantes                                  |
| `context`        | `AuthenticatedUserContext`                                                        | Acceso al usuario autenticado                                                   |
| `exceptions`     | `CustomAccessDeniedHandler`, `CustomAuthenticationEntryPoint`, `GlobalExceptionHandler` | Manejo de errores 401/403                                                      |
| `services`       | `CustomUserDetailsService`, `JwtService`                                          | Servicios para autenticación y generación de tokens                            |

### 💼 Lógica de negocio

| Componente               | Archivos                                                                          | Descripción                                                                     |
|--------------------------|-----------------------------------------------------------------------------------|---------------------------------------------------------------------------------|
| Interfaces               | `EventService`, `UserService`                                                     | Contratos de servicios                                                         |
| Implementaciones         | `EventServiceImpl`, `UserServiceImpl` (en `impl/`)                               | Lógica principal del negocio                                                   |

### 🗃️ Modelado de datos

| Subpaquete       | Contenido                                     | Tipo de datos                                                                  |
|------------------|-----------------------------------------------|--------------------------------------------------------------------------------|
| `dtos/event`     | `EventRequestDTO`, `EventResponseDTO`.        | Objetos de transferencia para eventos                                          |
| `dtos/user`      | `RegisterRequestDTO`, `UserResponseDTO`, etc. | Objetos de transferencia para usuarios                                         |
| `entities`       | `Event`, `User`                               | Entidades JPA                                                                  |
| `enums`          | `EventType`, `Importance`, `Role`             | Enumeraciones del sistema                                                      |

### 📦 Persistencia

| Componente       | Interfaces                                                                        | Función                                                                        |
|------------------|-----------------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| `repositories`   | `EventRepository`, `UserRepository` (extienden `JpaRepository`)                   | Acceso a la base de datos                                                     |

---

## 🌐 Endpoints Principales

Notas clave:

✅ /login y /register son los únicos endpoints públicos

🔒 Todos los demás requieren JWT en el header Authorization: Bearer <token>

⚠️ Los códigos 403 indican acceso denegado por permisos insuficientes

📝 Paginación: ?page=0&size=10 (valores por defecto en endpoints /admin)

### Eventos (`/api/events`)

| Método | Endpoint               | Rol       | Descripción                                  | Códigos de Respuesta |
|--------|------------------------|-----------|----------------------------------------------|----------------------|
| GET    | `/`                    | USER      | Obtiene todos los eventos del usuario        | 200, 401             |
| GET    | `/{id}`                | USER      | Obtiene un evento específico                 | 200, 404, 401        |
| POST   | `/`                    | USER      | Crea un nuevo evento                         | 201, 400, 401        |
| PUT    | `/{id}`                | USER      | Actualiza un evento existente                | 204, 400, 401, 404   |
| DELETE | `/{id}`                | USER      | Elimina un evento                            | 204, 401, 404        |
| GET    | `/admin`               | ADMIN     | Obtiene todos los eventos (paginados)        | 200, 401, 403        |

### Usuarios (`/api/users`)

| Método | Endpoint               | Rol       | Descripción                                  | Códigos de Respuesta |
|--------|------------------------|-----------|----------------------------------------------|----------------------|
| POST   | `/register`            | Público   | Registra un nuevo usuario                    | 201, 400, 409        |
| POST   | `/admin`               | ADMIN     | Crea un nuevo administrador                  | 201, 400, 403        |
| GET    | `/me`                  | USER      | Obtiene los datos del usuario autenticado    | 200, 401             |
| PATCH  | `/me`                  | USER      | Actualiza los datos del usuario              | 200, 400, 401        |
| DELETE | `/me`                  | USER      | Elimina la cuenta del usuario                | 204, 401             |
| GET    | `/admin`               | ADMIN     | Obtiene todos los usuarios (paginados)       | 200, 401, 403        |


### Autenticación (`/api/auth`)
| Método | Endpoint       | Rol     | Descripción                          | Códigos de Respuesta |
|--------|----------------|---------|--------------------------------------|----------------------|
| POST   | `/login`       | Público | Genera JWT al validar credenciales   | 200, 401             |

**Ejemplo de respuesta exitosa**:
```json
{
  "token": "eyJhbGciOi...",
  "username": "user1",
  "role": "USER"
} 
