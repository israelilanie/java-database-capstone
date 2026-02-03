 codex/refactor-readme-for-health-management-system-rpwddy
# Job Application Tracking System
# Health Management System (Smart Clinic)

A Spring Boot service for tracking job applications end-to-end, with a REST API that captures company details, role information, interview status, follow-ups, and notes. The project also retains the original clinic-management modules (appointments, doctors, patients, prescriptions) so you can evolve or remove them later.
A Spring Boot health management system that supports patient onboarding, doctor management, appointment scheduling, and prescription tracking. The application combines REST APIs with MVC dashboards (Thymeleaf) and uses MySQL for structured clinical data plus MongoDB for flexible prescription documents.
=======
# Health Management System (Smart Clinic)

 codex/improve-job-app-tracking-system-pubjjh
 main

A Spring Boot service for tracking job applications end-to-end, with a REST API that captures company details, role information, interview status, follow-ups, and notes. The project still contains legacy clinic-management modules (appointments, doctors, patients, prescriptions) so you can either expand or remove them later.

## ✨ What’s Included
=======
A Spring Boot service for tracking job applications end-to-end, with a REST API that captures company details, role information, interview status, follow-ups, and notes. The project also retains the original clinic-management modules (appointments, doctors, patients, prescriptions) so you can evolve or remove them later.
=======
A Spring Boot health management system that supports patient onboarding, doctor management, appointment scheduling, and prescription tracking. The application combines REST APIs with MVC dashboards (Thymeleaf) and uses MySQL for structured clinical data plus MongoDB for flexible prescription documents.
main

codex/refactor-readme-for-health-management-system-rpwddy
### Legacy modules still available
- Appointments, doctors, patients, prescriptions, admin auth, and JWT flows.
=======
## ✨ Core Features
 main

 main
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

 codex/refactor-readme-for-health-management-system-rpwddy
### Job Applications
 main
> 🔐 **Authentication**
> - The API returns JWT tokens from `/admin/login`, `/doctor/login`, and `/patient/login`.
> - Tokens are passed as **path variables** on protected endpoints (see tables below).

### Admin
 codex/refactor-readme-for-health-management-system-rpwddy
=======
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
 main
| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/prescription/{token}` | Save prescription (doctor token required) |
| GET | `/prescription/{appointmentId}/{token}` | Get prescription by appointment |

## 📦 Data Model & Architecture

Use these documents as the canonical references for the project’s architecture and schemas:

- **Architecture walkthrough**: `schema-architecture.md`
- **Database schema design**: `schema-design.md`
- **Product/user stories**: `user_stories.md`


#### Application status values
- `DRAFT`
- `APPLIED`
- `INTERVIEWING`
- `OFFER`
- `REJECTED`
- `WITHDRAWN`

## 📚 Documentation

### Data model (JobApplication)
| Field | Type | Required | Notes |
| --- | --- | --- | --- |
| `companyName` | string | ✅ | Employer or company name |
| `roleTitle` | string | ✅ | Job title / role |
| `location` | string | ❌ | City / region / remote |
| `status` | enum | ✅ | See status list above |
| `appliedDate` | date | ❌ | `YYYY-MM-DD` |
| `nextStepDate` | date | ❌ | `YYYY-MM-DD` |
| `source` | string | ❌ | Referral, LinkedIn, etc. |
| `salaryRange` | string | ❌ | Optional display string |
| `contactName` | string | ❌ | Recruiter / HM |
| `contactEmail` | string | ❌ | Email validation enforced |
| `notes` | string | ❌ | Up to 2000 chars |

### Example curl requests
Create:
```bash
curl -X POST http://localhost:8080/job-applications \
  -H "Content-Type: application/json" \
  -d '{
    "companyName": "OpenAI",
    "roleTitle": "Software Engineer",
    "status": "APPLIED"
  }'
```
| POST | `/admin/login` | Admin login, returns JWT |

Update status:
```bash
curl -X PATCH http://localhost:8080/job-applications/1/status \
  -H "Content-Type: application/json" \
  -d '{"status":"INTERVIEWING"}'
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

List by status:
```bash
curl "http://localhost:8080/job-applications?status=APPLIED"
```

=======
main
## 🚀 Getting Started

### Prerequisites
- Java 17
- Maven 3.9+
- MySQL 8+
codex/refactor-readme-for-health-management-system-rpwddy
- MongoDB 6+ (used by legacy prescriptions module)
=======
 main
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
 codex/refactor-readme-for-health-management-system-rpwddy
docker build -t job-tracker .
=======
 main
docker build -t smart-clinic-api .
docker run --rm -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/job_tracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" \
  -e SPRING_DATASOURCE_USERNAME=jobtracker \
  -e SPRING_DATASOURCE_PASSWORD=jobtracker \
  -e SPRING_DATA_MONGODB_URI="mongodb://jobtracker:jobtracker@host.docker.internal:27017/job_tracker?authSource=admin" \
  -e JWT_SECRET="change-me" \
codex/refactor-readme-for-health-management-system-rpwddy
  job-tracker
 main
  smart-clinic-api
```

### Docker Compose (recommended)
```bash
docker compose up --build
```

codex/refactor-readme-for-health-management-system-rpwddy
## ✅ Next Improvements (Suggestions)
- Add authentication for job-application endpoints.
- Add analytics dashboards (status funnels, response time).
- Add reminders/notifications for follow-up dates.
- Add pagination and search.
## ✅ Suggested Next Improvements

---
If you want the legacy clinic modules removed or renamed, we can migrate the codebase to a pure job-tracking domain next.
=======
## ✅ Suggested Next Improvements

 main
- Move JWT token handling from path variables to `Authorization` headers.
- Add role-based access control for appointment and prescription operations.
- Introduce pagination and search for patients and doctors.
- Add reminders (email/SMS) for upcoming appointments.
