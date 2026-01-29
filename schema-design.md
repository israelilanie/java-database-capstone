# Database Schema Design

## MySQL Database Design

Structured, validated, and interrelated operational data is stored in MySQL. This includes users, scheduling, and administrative control data.

### Table: patients
- `id`: INT, Primary Key, AUTO_INCREMENT
- `first_name`: VARCHAR(100), NOT NULL
- `last_name`: VARCHAR(100), NOT NULL
- `email`: VARCHAR(150), NOT NULL, UNIQUE
- `password_hash`: VARCHAR(255), NOT NULL
- `phone`: VARCHAR(20), UNIQUE
- `date_of_birth`: DATE
- `created_at`: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP

### Table: doctors
- `id`: INT, Primary Key, AUTO_INCREMENT
- `first_name`: VARCHAR(100), NOT NULL
- `last_name`: VARCHAR(100), NOT NULL
- `email`: VARCHAR(150), NOT NULL, UNIQUE
- `specialization`: VARCHAR(100), NOT NULL
- `phone`: VARCHAR(20), UNIQUE
- `is_active`: BOOLEAN, DEFAULT TRUE
- `created_at`: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP

### Table: admin
- `id`: INT, Primary Key, AUTO_INCREMENT
- `username`: VARCHAR(100), NOT NULL, UNIQUE
- `password_hash`: VARCHAR(255), NOT NULL
- `email`: VARCHAR(150), UNIQUE
- `created_at`: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP

### Table: appointments
- `id`: INT, Primary Key, AUTO_INCREMENT
- `doctor_id`: INT, Foreign Key → doctors(id), NOT NULL
- `patient_id`: INT, Foreign Key → patients(id), NOT NULL
- `appointment_time`: DATETIME, NOT NULL
- `duration_minutes`: INT, DEFAULT 60
- `status`: ENUM('SCHEDULED','COMPLETED','CANCELLED'), NOT NULL
- `created_at`: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP

### Table: doctor_availability
- `id`: INT, Primary Key, AUTO_INCREMENT
- `doctor_id`: INT, Foreign Key → doctors(id), NOT NULL
- `available_date`: DATE, NOT NULL
- `start_time`: TIME, NOT NULL
- `end_time`: TIME, NOT NULL

### Table: clinic_locations (optional extension)
- `id`: INT, Primary Key, AUTO_INCREMENT
- `name`: VARCHAR(150), NOT NULL
- `address`: TEXT, NOT NULL
- `phone`: VARCHAR(20)

### Table: payments (optional extension)
- `id`: INT, Primary Key, AUTO_INCREMENT
- `appointment_id`: INT, Foreign Key → appointments(id), NOT NULL
- `amount`: DECIMAL(10,2), NOT NULL
- `payment_status`: ENUM('PENDING','PAID','FAILED'), NOT NULL
- `payment_method`: VARCHAR(50)
- `paid_at`: TIMESTAMP

## MongoDB Collection Design

### Collection: prescriptions
```json
{
  "_id": "ObjectId('64abc123456')",
  "appointmentId": 102,
  "patientId": 15,
  "doctorId": 7,
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500mg",
      "frequency": "Every 6 hours",
      "durationDays": 5
    }
  ],
  "doctorNotes": "Drink plenty of fluids.",
  "tags": ["pain", "fever"],
  "refillCount": 2,
  "createdAt": "2025-10-01T10:30:00Z"
}
```

**Design decisions:**
- Store only `patientId` and `doctorId` (not full objects) to avoid duplication.
- Embed medication arrays to support multiple drugs per prescription.
- Allow schema evolution for medication attributes without migrations.
- Tie prescriptions to appointments for traceability.

### Collection: patient_feedback
```json
{
  "_id": "ObjectId('64def987654')",
  "appointmentId": 102,
  "doctorId": 7,
  "patientId": 15,
  "rating": 4,
  "comments": "Doctor was attentive and helpful.",
  "submittedAt": "2025-10-02T08:15:00Z"
}
```

**Design decisions:**
- Feedback is optional and unstructured.
- Free-text comments fit well in MongoDB without rigid schema constraints.
- Supports analytics without affecting core operations.

### Collection: activity_logs
```json
{
  "_id": "ObjectId('64log123999')",
  "userId": 15,
  "userRole": "PATIENT",
  "action": "BOOK_APPOINTMENT",
  "metadata": {
    "appointmentId": 102,
    "doctorId": 7
  },
  "timestamp": "2025-10-01T09:45:00Z"
}
```

**Design decisions:**
- Keep logs flexible with a `metadata` object for event-specific data.
- Support auditing and tracing without expanding relational tables.

## MySQL Stored Procedures (CLI-ready)

Below are CLI-ready stored procedures that follow consistent aggregation logic for appointments by doctor and time period.

### Procedure: GetDailyAppointmentReportByDoctor
```sql
DELIMITER //
CREATE PROCEDURE GetDailyAppointmentReportByDoctor(
    IN p_doctor_id BIGINT,
    IN p_day DATE
)
BEGIN
    SELECT
        d.id AS doctor_id,
        d.name AS doctor_name,
        DATE(a.appointment_time) AS appointment_day,
        COUNT(*) AS appointment_count
    FROM appointments a
    JOIN doctors d ON d.id = a.doctor_id
    WHERE a.doctor_id = p_doctor_id
      AND DATE(a.appointment_time) = p_day
    GROUP BY d.id, d.name, DATE(a.appointment_time);
END//
DELIMITER ;
```

### Procedure: GetDoctorWithMostPatientByMonth
```sql
DELIMITER //
CREATE PROCEDURE GetDoctorWithMostPatientByMonth(
    IN p_year INT,
    IN p_month INT
)
BEGIN
    SELECT
        d.id AS doctor_id,
        d.name AS doctor_name,
        COUNT(*) AS appointment_count
    FROM appointments a
    JOIN doctors d ON d.id = a.doctor_id
    WHERE YEAR(a.appointment_time) = p_year
      AND MONTH(a.appointment_time) = p_month
    GROUP BY d.id, d.name
    ORDER BY appointment_count DESC
    LIMIT 1;
END//
DELIMITER ;
```

### Procedure: GetDoctorWithMostPatientByYear
```sql
DELIMITER //
CREATE PROCEDURE GetDoctorWithMostPatientByYear(
    IN p_year INT
)
BEGIN
    SELECT
        d.id AS doctor_id,
        d.name AS doctor_name,
        COUNT(*) AS appointment_count
    FROM appointments a
    JOIN doctors d ON d.id = a.doctor_id
    WHERE YEAR(a.appointment_time) = p_year
    GROUP BY d.id, d.name
    ORDER BY appointment_count DESC
    LIMIT 1;
END//
DELIMITER ;
```
