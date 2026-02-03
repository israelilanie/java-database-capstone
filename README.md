# Job Application Tracking System

A Spring Boot service for tracking job applications end-to-end, with a REST API that captures company details, role information, interview status, follow-ups, and notes. The project still contains legacy clinic-management modules (appointments, doctors, patients, prescriptions) so you can either expand or remove them later.

## ✨ What’s Included

### Job application tracking
- Create, update, and delete job applications.
- Track application status (Draft, Applied, Interviewing, Offer, Rejected, Withdrawn).
- Capture applied dates, next steps, contact details, sources, and notes.
- Filter applications by status.

### Legacy modules still available
- Appointments, doctors, patients, prescriptions, admin auth, and JWT flows.

## 🧭 API Overview

Base URL: `http://localhost:8080`

### Job Applications
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

Status patch:
```json
{
  "status": "INTERVIEWING"
}
```

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

Update status:
```bash
curl -X PATCH http://localhost:8080/job-applications/1/status \
  -H "Content-Type: application/json" \
  -d '{"status":"INTERVIEWING"}'
```

List by status:
```bash
curl "http://localhost:8080/job-applications?status=APPLIED"
```

## 🚀 Getting Started

### Prerequisites
- Java 17
- Maven 3.9+
- MySQL 8+
- MongoDB 6+ (used by legacy prescriptions module)

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

## 🐳 Docker

### Build and run the API
```bash
docker build -t job-tracker .
docker run --rm -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/job_tracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" \
  -e SPRING_DATASOURCE_USERNAME=jobtracker \
  -e SPRING_DATASOURCE_PASSWORD=jobtracker \
  -e SPRING_DATA_MONGODB_URI="mongodb://jobtracker:jobtracker@host.docker.internal:27017/job_tracker?authSource=admin" \
  -e JWT_SECRET="change-me" \
  job-tracker
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

---
If you want the legacy clinic modules removed or renamed, we can migrate the codebase to a pure job-tracking domain next.
