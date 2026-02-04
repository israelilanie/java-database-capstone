# Smart Clinic Health Management System

Smart Clinic is a Spring Boot health management system that blends REST APIs with MVC dashboards (Thymeleaf). It supports patient onboarding, doctor operations, appointment scheduling, and prescription tracking. The backend uses MySQL for structured clinical data and MongoDB for flexible prescription documents. For a deeper architectural walkthrough, see `schema-architecture.md`.

## ✅ What the App Does

### Patients
Patients interact with the system primarily through REST APIs:
- **Sign up and log in** to create a profile and receive a JWT token.
- **View profile details** using a token-based lookup.
- **Browse appointments** (past or upcoming) and filter by doctor.
- **Book, update, or cancel appointments** once authenticated.

### Doctors
Doctors use JWT-authenticated REST endpoints and have an MVC dashboard:
- **Log in to receive a JWT token**.
- **Check availability** for a given date.
- **View daily appointments** and patient schedules.
- **Create and retrieve prescriptions**, stored in MongoDB.
- **Access the doctor dashboard** rendered by Thymeleaf.

### Admins
Admins use JWT-authenticated REST endpoints and have an MVC dashboard:
- **Log in to receive a JWT token**.
- **Add, update, or delete doctors**.
- **Access the admin dashboard** rendered by Thymeleaf.

## 🧠 How It Works (Architecture Summary)

Smart Clinic follows a layered Spring Boot architecture:

1. **User Interface Layer**
   - Thymeleaf dashboards for admin and doctor users.
   - REST clients (web/mobile) for patient workflows and scheduling.

2. **Controller Layer**
   - MVC controllers return HTML views for dashboards.
   - REST controllers validate inputs and return JSON responses.

3. **Service Layer**
   - Centralizes business rules (token validation, availability checks, scheduling logic).
   - Keeps controllers focused on request handling.

4. **Repository Layer**
   - MySQL repositories via Spring Data JPA for patients, doctors, appointments, and admins.
   - MongoDB repository via Spring Data MongoDB for prescriptions.

5. **Database Layer**
   - **MySQL** stores structured, relational clinical data.
   - **MongoDB** stores prescription documents that may evolve in structure.

For the full walkthrough, read `schema-architecture.md` and `schema-design.md`.

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven (or use the Maven wrapper)
- MySQL 8+
- MongoDB 7+

### Option 1: Docker Compose (Recommended)
```bash
docker compose up --build
```
This starts:
- App on `http://localhost:8080`
- MySQL on `localhost:3306`
- MongoDB on `localhost:27017`

### Option 2: Local Run (Manual)
1. Configure environment variables (see below).
2. Start MySQL and MongoDB.
3. Run the application:
```bash
cd app
./mvnw spring-boot:run
```

### Environment Variables
| Variable | Default | Description |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/job_tracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC` | MySQL connection string |
| `SPRING_DATASOURCE_USERNAME` | `jobtracker` | MySQL user |
| `SPRING_DATASOURCE_PASSWORD` | `jobtracker` | MySQL password |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` | Schema management strategy |
| `SPRING_DATA_MONGODB_URI` | `mongodb://jobtracker:jobtracker@localhost:27017/job_tracker?authSource=admin` | MongoDB connection string |
| `JWT_SECRET` | `change-me-for-prod` | JWT signing secret |

## 🔐 Authentication Notes
- JWT tokens are returned from `/admin/login`, `/doctor/login`, and `/patient/login`.
- Tokens are passed as **path variables** for protected endpoints.

## 📌 API Overview

Base URL: `http://localhost:8080`

### Admin
| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/admin/login` | Admin login, returns JWT |

### Doctors
| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/doctor` | List all doctors |
| GET | `/doctor/availability/{user}/{doctorId}/{date}/{token}` | Doctor availability by date |
| GET | `/doctor/filter?name=&time=&specialty=` | Filter doctors |
| POST | `/doctor/{token}` | Add doctor (admin token required) |
| PUT | `/doctor/{token}` | Update doctor (admin token required) |
| DELETE | `/doctor/{id}/{token}` | Delete doctor (admin token required) |
| POST | `/doctor/login` | Doctor login, returns JWT |

### Patients
| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/patient` | Patient sign-up |
| POST | `/patient/login` | Patient login, returns JWT |
| GET | `/patient/{token}` | Get patient profile from token |
| GET | `/patient/{id}/{user}/{token}` | Patient appointments (patient or doctor token) |
| GET | `/patient/filter/{condition}/{name}/{token}` | Filter appointments (`past`/`future`, doctor name) |

### Appointments
| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/appointments/{date}/{patientName}/{token}` | Doctor view of appointments for a date |
| POST | `/appointments/{token}` | Book appointment (patient token required) |
| PUT | `/appointments/{token}` | Update appointment (patient token required) |
| DELETE | `/appointments/{id}/{token}` | Cancel appointment (patient token required) |

### Prescriptions
| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/prescription/{token}` | Save prescription (doctor token required) |
| GET | `/prescription/{appointmentId}/{token}` | Get prescription by appointment |

## 📚 Related Documentation
- Architecture walkthrough: `schema-architecture.md`
- Database schema design: `schema-design.md`
- Product and user stories: `user_stories.md`
