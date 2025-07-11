# 🗓️ Personal Event Planner API - Spring Boot & JWT

This repository contains the source code and technical documentation for a portfolio project: A secure REST API where authenticated users can manage their personal events.

> To view the Spanish version, visit `README-ES.md`.
> Para ver la versión en español, visite `README-ES.md`.

## 📌 Project Description

Event management API (reminders, meetings, tasks) with role-based authentication. The system enables:

- Create, edit and delete personal events
- User and permission management
- Role-based access control (USER/ADMIN)

---

## 🧑‍💼 Roles and Capabilities

| Role    | Key Functionalities                                                                 |
|---------|------------------------------------------------------------------------------------|
| USER    | Full CRUD operations on their own events                                           |
| ADMIN   | List all users/events, create new administrators                                   |

---

## 🔒 Access Management

**All users** can:
- Update their profile and credentials
- Delete their own account

**Admins** additionally can:
- List all system events
- View any user's information

⚠️ Deletion rules:
- Regular users can only delete their own events
- Admins can delete any resource

---

## ⚙️ Technologies Used

- **Spring Boot 3.x** (Main framework)
- **Spring Security + JWT** (Authentication/authorization)
- **JPA/Hibernate** (Persistence)
- **MySQL** (Database)
- **MapStruct** (DTO/entity mapping)
- **Maven** (Dependency management)
- **BCrypt** (Password hashing)
- **Lombok**

---

## 🏗️ Project Structure

### 🗂️ Core Packages

| Package         | Key Components                                                                 | Responsibility                                                             |
|-----------------|--------------------------------------------------------------------------------|---------------------------------------------------------------------------|
| `config`        | `ValidationConfig`                                                             | Configures DTO validation messages                                       |
| `controllers`   | `EventController`, `UserController`                                            | Contains all API endpoints                                               |
| `exceptions`    | `AdminAccessRequiredException`                                                 | Custom business exceptions                                               |
| `mappers`       | `EventMapper`, `UserMapper`                                                    | Handles entity-DTO conversion                                            |

### 🔐 Security (JWT)

| Subpackage      | Components                                                                     | Function                                                                 |
|-----------------|--------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| `auth`          | `JwtAuthenticationFilter`, `JwtValidationFilter`                               | JWT authentication filters                                               |
| `config`        | `SecurityConfig`, `TokenJwtConfig`                                             | Security configuration and constants                                     |
| `context`       | `AuthenticatedUserContext`                                                     | Provides access to authenticated user                                    |
| `exceptions`    | `CustomAccessDeniedHandler`, `CustomAuthenticationEntryPoint`, `GlobalExceptionHandler` | Handles 401/403 errors                                                 |
| `services`      | `CustomUserDetailsService`, `JwtService`                                       | Authentication services                                                  |

### 💼 Business Logic

| Component        | Files                                                                          | Description                                                              |
|------------------|--------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| Interfaces       | `EventService`, `UserService`                                                  | Service contracts                                                        |
| Implementations  | `EventServiceImpl`, `UserServiceImpl` (in `impl/`)                             | Core business logic                                                      |

### 🗃️ Data Modeling

| Subpackage       | Content                                    | Data Type                                                               |
|------------------|--------------------------------------------|-------------------------------------------------------------------------|
| `dtos/event`     | `EventRequestDTO`, `EventResponseDTO`      | Event transfer objects                                                  |
| `dtos/user`      | `RegisterRequestDTO`, `UserResponseDTO`... | User transfer objects                                                   |
| `entities`       | `Event`, `User`                            | JPA entities                                                            |
| `enums`          | `EventType`, `Importance`, `Role`                                     | System enums                                                            |

### 📦 Persistence

| Component       | Interfaces                                                                     | Function                                                                |
|-----------------|--------------------------------------------------------------------------------|-------------------------------------------------------------------------|
| `repositories`  | `EventRepository`, `UserRepository` (extend `JpaRepository`)                   | Database access                                                         |

---

## 🌐 Key Endpoints

Key notes:

✅ /login and /register are the only public endpoints

🔒 All others require JWT in Authorization: Bearer <token> header

⚠️ 403 codes indicate insufficient permissions

📝 Pagination: ?page=0&size=10 (default values for /admin endpoints)

### Events (`/api/events`)

| Method | Endpoint            | Role    | Description                              | Status Codes          |
|--------|---------------------|---------|------------------------------------------|-----------------------|
| GET    | `/`                 | USER    | Get all user's events                    | 200, 401              |
| GET    | `/{id}`             | USER    | Get specific event                       | 200, 404, 401         |
| POST   | `/`                 | USER    | Create new event                         | 201, 400, 401         |
| PUT    | `/{id}`             | USER    | Update existing event                    | 204, 400, 401, 404    |
| DELETE | `/{id}`             | USER    | Delete event                             | 204, 401, 404         |
| GET    | `/admin`            | ADMIN   | Get all events (paginated)               | 200, 401, 403         |

### Users (`/api/users`)

| Method | Endpoint            | Role      | Description                          | Status Codes          |
|--------|---------------------|-----------|--------------------------------------|-----------------------|
| POST   | `/register`         | Public    | Register new user                    | 201, 400, 409         |
| POST   | `/admin`            | ADMIN     | Create new admin                     | 201, 400, 403         |
| GET    | `/me`               | USER      | Get authenticated user's data        | 200, 401              |
| PATCH  | `/me`               | USER      | Update user's data                   | 200, 400, 401         |
| DELETE | `/me`               | USER      | Delete user account                  | 204, 401              |
| GET    | `/admin`            | ADMIN     | Get all users (paginated)            | 200, 401, 403         |

### Authentication (`/api/auth`)

| Method | Endpoint      | Role    | Description                      | Status Codes  |
|--------|---------------|---------|----------------------------------|---------------|
| POST   | `/login`      | Public  | Generates JWT on valid credentials | 200, 401      |

**Success response example**:
```json
{
  "token": "eyJhbGciOi...",
  "username": "user1",
  "role": "USER"
}