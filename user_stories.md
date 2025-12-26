# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]

Admin User Stories

1. Title: Admin Login
As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely.
Acceptance Criteria:

Admin can enter username and password.

Successful login redirects to AdminDashboard.

Invalid credentials show an error message.
Priority: High
Story Points: 3
Notes: Supports session management for security.

2. Title: Admin Logout
As an admin, I want to log out of the portal, so that system access is protected.
Acceptance Criteria:

Admin can click logout.

Session ends, redirect to login page.

Access to admin features blocked after logout.
Priority: High
Story Points: 2

3. Title: Add Doctor
As an admin, I want to add doctors to the portal, so that they can manage appointments and patient records.
Acceptance Criteria:

Admin can enter doctor details (name, specialization, contact info).

Doctor profile is saved in MySQL database.

Confirmation message shown upon success.
Priority: High
Story Points: 5

4. Title: Delete Doctor Profile
As an admin, I want to delete a doctor’s profile, so that outdated or inactive accounts are removed.
Acceptance Criteria:

Admin can select a doctor to delete.

Confirmation prompt before deletion.

Profile is removed from MySQL database.
Priority: Medium
Story Points: 3

5. Title: Track Appointment Statistics
As an admin, I want to run a stored procedure in MySQL CLI to get the number of appointments per month, so that I can track usage statistics.
Acceptance Criteria:

Admin can execute stored procedure.

Procedure returns appointment counts per month.

Results are accurate and displayed clearly.
Priority: Medium
Story Points: 3

Patient User Stories

1. Title: View Doctors List
As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering.
Acceptance Criteria:

Patient can access doctor list publicly.

List includes name, specialization, and contact info.
Priority: Medium
Story Points: 2

2. Title: Patient Sign Up
As a patient, I want to sign up using my email and password, so that I can book appointments.
Acceptance Criteria:

Registration form collects email and password.

Patient account is created in MySQL database.

Confirmation email sent.
Priority: High
Story Points: 3

3. Title: Patient Login
As a patient, I want to log into the portal, so that I can manage my bookings.
Acceptance Criteria:

Patient can enter credentials to login.

Successful login redirects to PatientDashboard.

Invalid credentials show an error message.
Priority: High
Story Points: 3

4. Title: Patient Logout
As a patient, I want to log out, so that my account is secured.
Acceptance Criteria:

Logout button terminates session.

Patient redirected to login page.
Priority: Medium
Story Points: 2

5. Title: Book Appointment
As a patient, I want to book an hour-long appointment with a doctor, so that I can consult with them.
Acceptance Criteria:

Patient selects doctor and available slot.

Appointment saved in MySQL database.

Confirmation shown to patient.
Priority: High
Story Points: 5

6. Title: View Upcoming Appointments
As a patient, I want to view my upcoming appointments, so that I can prepare accordingly.
Acceptance Criteria:

Patient can see a list of future appointments.

Each entry shows date, time, and doctor name.
Priority: Medium
Story Points: 3

Doctor User Stories

1. Title: Doctor Login
As a doctor, I want to log into the portal, so that I can manage my appointments.
Acceptance Criteria:

Doctor enters credentials to login.

Redirected to DoctorDashboard.

Invalid login shows error.
Priority: High
Story Points: 3

2. Title: Doctor Logout
As a doctor, I want to log out, so that my data is protected.
Acceptance Criteria:

Logout button terminates session.

Redirected to login page.
Priority: Medium
Story Points: 2

3. Title: View Appointment Calendar
As a doctor, I want to view my appointment calendar, so that I can stay organized.
Acceptance Criteria:

Calendar displays all upcoming appointments.

Shows patient name and time.
Priority: High
Story Points: 5

4. Title: Mark Unavailability
As a doctor, I want to mark my unavailability, so that patients can only book available slots.
Acceptance Criteria:

Doctor selects unavailable time slots.

Slots are blocked in booking system.
Priority: Medium
Story Points: 3

5. Title: Update Profile
As a doctor, I want to update my profile with specialization and contact info, so that patients have accurate information.
Acceptance Criteria:

Doctor can edit profile fields.

Updates saved in MySQL database.

Confirmation shown after save.
Priority: Medium
Story Points: 3

6. Title: View Patient Details
As a doctor, I want to view patient details for upcoming appointments, so that I can be prepared.
Acceptance Criteria:

Doctor can access patient information (name, age, medical history).

Only details for scheduled appointments are shown.
Priority: High
Story Points: 5