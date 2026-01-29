# User Stories: Patient Appointment Portal

## Story template

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

## Admin user stories

### 1. Admin Login
_As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**
1. Admin can enter a valid username and password.
2. Successful login redirects to the AdminDashboard.
3. Invalid credentials show an error message.

**Priority:** High  
**Story Points:** 3  
**Notes:**
- Supports session management for security.

### 2. Admin Logout
_As an admin, I want to log out of the portal, so that system access is protected._

**Acceptance Criteria:**
1. Admin can click logout from the dashboard.
2. Session ends and redirects to the login page.
3. Admin features are blocked after logout.

**Priority:** High  
**Story Points:** 2  
**Notes:**
- Ensure server-side session invalidation.

### 3. Add Doctor
_As an admin, I want to add doctors to the portal, so that they can manage appointments and patient records._

**Acceptance Criteria:**
1. Admin can enter doctor details (name, specialization, contact info).
2. Doctor profile is saved in the MySQL database.
3. A confirmation message is shown upon success.

**Priority:** High  
**Story Points:** 5  
**Notes:**
- Validate unique doctor identifier before save.

### 4. Delete Doctor Profile
_As an admin, I want to delete a doctor’s profile, so that outdated or inactive accounts are removed._

**Acceptance Criteria:**
1. Admin can select a doctor to delete.
2. Confirmation prompt appears before deletion.
3. Profile is removed from the MySQL database.

**Priority:** Medium  
**Story Points:** 3  
**Notes:**
- Confirm cascading behavior for related appointments.

### 5. Track Appointment Statistics
_As an admin, I want to run a stored procedure in MySQL to get the number of appointments per month, so that I can track usage statistics._

**Acceptance Criteria:**
1. Admin can execute the stored procedure from the admin interface or CLI.
2. Procedure returns appointment counts per month.
3. Results are accurate and displayed clearly.

**Priority:** Medium  
**Story Points:** 3  
**Notes:**
- Align procedure output with dashboard reporting format.

## Patient user stories

### 1. View Doctors List
_As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering._

**Acceptance Criteria:**
1. Patient can access the doctor list publicly.
2. List includes name, specialization, and contact info.
3. Results can be filtered or sorted by specialization.

**Priority:** Medium  
**Story Points:** 2  
**Notes:**
- Use pagination for large lists.

### 2. Patient Sign Up
_As a patient, I want to sign up using my email and password, so that I can book appointments._

**Acceptance Criteria:**
1. Registration form collects email and password.
2. Patient account is created in the MySQL database.
3. Confirmation email is sent after registration.

**Priority:** High  
**Story Points:** 3  
**Notes:**
- Enforce password complexity requirements.

### 3. Patient Login
_As a patient, I want to log into the portal, so that I can manage my bookings._

**Acceptance Criteria:**
1. Patient can enter credentials to log in.
2. Successful login redirects to PatientDashboard.
3. Invalid credentials show an error message.

**Priority:** High  
**Story Points:** 3  
**Notes:**
- Lock account after repeated failed attempts.

### 4. Patient Logout
_As a patient, I want to log out, so that my account is secured._

**Acceptance Criteria:**
1. Logout button terminates the session.
2. Patient is redirected to the login page.
3. Authenticated pages are no longer accessible after logout.

**Priority:** Medium  
**Story Points:** 2  
**Notes:**
- Ensure token revocation for API clients.

### 5. Book Appointment
_As a patient, I want to book an hour-long appointment with a doctor, so that I can consult with them._

**Acceptance Criteria:**
1. Patient selects a doctor and available time slot.
2. Appointment is saved in the MySQL database.
3. Confirmation is shown to the patient.

**Priority:** High  
**Story Points:** 5  
**Notes:**
- Prevent double-booking of the same slot.

### 6. View Upcoming Appointments
_As a patient, I want to view my upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**
1. Patient can see a list of future appointments.
2. Each entry shows date, time, and doctor name.
3. Appointments are ordered chronologically.

**Priority:** Medium  
**Story Points:** 3  
**Notes:**
- Allow calendar export in a later enhancement.

## Doctor user stories

### 1. Doctor Login
_As a doctor, I want to log into the portal, so that I can manage my appointments._

**Acceptance Criteria:**
1. Doctor enters credentials to log in.
2. Successful login redirects to DoctorDashboard.
3. Invalid login shows an error message.

**Priority:** High  
**Story Points:** 3  
**Notes:**
- Require MFA for privileged accounts.

### 2. Doctor Logout
_As a doctor, I want to log out, so that my data is protected._

**Acceptance Criteria:**
1. Logout button terminates the session.
2. Doctor is redirected to the login page.
3. Session tokens are invalidated server-side.

**Priority:** Medium  
**Story Points:** 2  
**Notes:**
- Ensure concurrent sessions are handled consistently.

### 3. View Appointment Calendar
_As a doctor, I want to view my appointment calendar, so that I can stay organized._

**Acceptance Criteria:**
1. Calendar displays all upcoming appointments.
2. Each entry shows patient name and time.
3. The calendar defaults to the current week.

**Priority:** High  
**Story Points:** 5  
**Notes:**
- Support day/week views in future iterations.

### 4. Mark Unavailability
_As a doctor, I want to mark my unavailability, so that patients can only book available slots._

**Acceptance Criteria:**
1. Doctor selects unavailable time slots.
2. Slots are blocked in the booking system.
3. Existing appointments are unaffected or rescheduled with consent.

**Priority:** Medium  
**Story Points:** 3  
**Notes:**
- Require confirmation when overriding availability.

### 5. Update Profile
_As a doctor, I want to update my profile with specialization and contact info, so that patients have accurate information._

**Acceptance Criteria:**
1. Doctor can edit profile fields.
2. Updates are saved in the MySQL database.
3. Confirmation is shown after save.

**Priority:** Medium  
**Story Points:** 3  
**Notes:**
- Track audit history for profile changes.
