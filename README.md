# Health Management System (Smart Clinic)

## ✨ What’s Included
=======
A Spring Boot health management system that supports patient onboarding, doctor management, appointment scheduling, and prescription tracking. The application combines REST APIs with MVC dashboards (Thymeleaf) and uses MySQL for structured clinical data plus MongoDB for flexible prescription documents.
=======

main

## ✨ Core Features
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

> 🔐 **Authentication**
> - The API returns JWT tokens from `/admin/login`, `/doctor/login`, and `/patient/login`.
> - Tokens are passed as **path variables** on protected endpoints (see tables below).

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

## 📦 Data Model & Architecture

Use these documents as the canonical references for the project’s architecture and schemas:

- **Architecture walkthrough**: `schema-architecture.md`
- **Database schema design**: `schema-design.md`
- **Product/user stories**: `user_stories.md`


## 📚 Documentation



