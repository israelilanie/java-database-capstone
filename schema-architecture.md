Section 1: Architecture summary

This Spring Boot application combines MVC and REST architectures. 
Admin and Doctor dashboards use Thymeleaf templates, while all other modules such as Appointments, PatientDashboard, and PatientRecord are exposed via REST APIs. 
Requests pass through a service layer that handles business logic and delegates database operations to MySQL repositories for structured data (patients, doctors, appointments, admins) and MongoDB repositories for document-based data (prescriptions). 
MySQL uses JPA entities and MongoDB uses document models, enabling a clean separation between presentation, business logic, and data persistence layers.


Section 2: Numbered flow of data and control


1. User Interaction: Users access AdminDashboard, DoctorDashboard, or RESTModules (Appointments, PatientDashboard, PatientRecord).

2. Controller Routing: Dashboards use Thymeleaf Controllers; REST modules use REST Controllers to handle requests.

3. Service Layer Invocation: Controllers call the Service Layer containing business logic.

4. Repository Access: Service Layer interacts with MySQL Repositories (relational data) and MongoDB Repository (document data).

5. Database Operations: Repositories query the corresponding MySQL or MongoDB databases.

6. Model Mapping: Retrieved data is mapped to MySQL Models (Patient, Doctor, Appointment, Admin) or MongoDB Models (Prescription).

7. Data Flow Back: Models return data to the Service Layer, which sends processed results back to the controller.