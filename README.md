# Job Application Tracking System
# Health Management System (Smart Clinic)

A Spring Boot service for tracking job applications end-to-end, with a REST API that captures company details, role information, interview status, follow-ups, and notes. The project also retains the original clinic-management modules (appointments, doctors, patients, prescriptions) so you can evolve or remove them later.
A Spring Boot health management system that supports patient onboarding, doctor management, appointment scheduling, and prescription tracking. The application combines REST APIs with MVC dashboards (Thymeleaf) and uses MySQL for structured clinical data plus MongoDB for flexible prescription documents.

## ✨ Core Features

### Job application tracking
- Create, update, and delete job applications.
- Track application status (Draft, Applied, Interviewing, Offer, Rejected, Withdrawn).
- Capture applied dates, next steps, contact details, sources, and notes.
- Filter applications by status.

### Legacy modules still available
- Appointments, doctors, patients, prescriptions, admin auth, and JWT flows.
- **Patient portal**
  - Patient sign-up and login.
  - View personal profile details.
  - Browse upcoming or past appointments and filter by doctor.
- **Doctor operations**
  - Doctor login with JWT tokens.
  - View availability for a given date.
  - Manage appointments and view patient history.
- **Admin operations**
  - Admin login with JWT tokens.
  - Add, update, and delete doctors.
- **Appointments**
  - Book, update, and cancel appointments with validation.
  - Filter appointments by time frame or doctor name.
- **Prescriptions**
  - Store and retrieve prescription records in MongoDB.
- **MVC dashboards**
  - Admin and Doctor dashboards rendered via Thymeleaf templates.

## 🧭 API Overview

Base URL: `http://localhost:8080`

### Job Applications
> 🔐 **Authentication**
> - The API returns JWT tokens from `/admin/login`, `/doctor/login`, and `/patient/login`.
> - Tokens are passed as **path variables** on protected endpoints (see tables below).

### Admin
| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/job-applications` | List all applications |
| GET | `/job-applications?status=APPLIED` | Filter by status |
| GET | `/job-applications/{id}` | Get one application |
| POST | `/job-applications` | Create a new application |
| PUT | `/job-applications/{id}` | Update an existing application |
| PATCH | `/job-applications/{id}/status` | Update status only |
| DELETE | `/job-applications/{id}` | Delete an application |

#### Sample payloads
Create/Update:
```json
{
  "companyName": "OpenAI",
  "roleTitle": "Software Engineer",
  "location": "Remote",
  "status": "APPLIED",
  "appliedDate": "2025-04-01",
  "nextStepDate": "2025-04-10",
  "source": "Referral",
  "salaryRange": "$150k-$180k",
  "contactName": "Alex Kim",
  "contactEmail": "alex@example.com",
  "notes": "Follow up after recruiter screen."
}
```
| POST | `/admin/login` | Admin login, returns JWT |

Status patch:
```json
{
  "status": "INTERVIEWING"
}
```
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

## 📦 Data Model & Architecture

Use these documents as the canonical references for the project’s architecture and schemas:

- **Architecture walkthrough**: `schema-architecture.md`
- **Database schema design**: `schema-design.md`
- **Product/user stories**: `user_stories.md`

## 🚀 Getting Started

### Prerequisites
- Java 17
- Maven 3.9+
- MySQL 8+
- MongoDB 6+ (used by legacy prescriptions module)
- MongoDB 6+

### Local run (Maven)
```bash
cd app
./mvnw spring-boot:run
```

### Environment variables
| Variable | Purpose | Default |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | MySQL JDBC URL | `jdbc:mysql://localhost:3306/job_tracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC` |
| `SPRING_DATASOURCE_USERNAME` | MySQL username | `jobtracker` |
| `SPRING_DATASOURCE_PASSWORD` | MySQL password | `jobtracker` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Schema strategy | `update` |
| `SPRING_DATA_MONGODB_URI` | MongoDB URI | `mongodb://jobtracker:jobtracker@localhost:27017/job_tracker?authSource=admin` |
| `JWT_SECRET` | JWT signing secret | `change-me-for-prod` |

> ✅ Tip: Use environment variables to rename the database to something clinic-specific without changing code.

## 🐳 Docker

### Build and run the API
```bash
docker build -t job-tracker .
docker build -t smart-clinic-api .
docker run --rm -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/job_tracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" \
  -e SPRING_DATASOURCE_USERNAME=jobtracker \
  -e SPRING_DATASOURCE_PASSWORD=jobtracker \
  -e SPRING_DATA_MONGODB_URI="mongodb://jobtracker:jobtracker@host.docker.internal:27017/job_tracker?authSource=admin" \
  -e JWT_SECRET="change-me" \
  job-tracker
  smart-clinic-api
```

### Docker Compose (recommended)
```bash
docker compose up --build
```

## ✅ Next Improvements (Suggestions)
- Add authentication for job-application endpoints.
- Add analytics dashboards (status funnels, response time).
- Add reminders/notifications for follow-up dates.
- Add pagination and search.
## ✅ Suggested Next Improvements

---
If you want the legacy clinic modules removed or renamed, we can migrate the codebase to a pure job-tracking domain next.
- Move JWT token handling from path variables to `Authorization` headers.
- Add role-based access control for appointment and prescription operations.
- Introduce pagination and search for patients and doctors.
- Add reminders (email/SMS) for upcoming appointments.
