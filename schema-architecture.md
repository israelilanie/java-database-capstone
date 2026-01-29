# Smart Clinic Architecture Walkthrough

## Architecture summary

The Smart Clinic application combines MVC and REST architectures within a Spring Boot monolith. Admin and Doctor dashboards are rendered with Thymeleaf templates, while modules like Appointments, PatientDashboard, and PatientRecord are exposed as REST endpoints that return JSON. Requests pass through a service layer that enforces business rules and delegates persistence to repositories backed by two databases: MySQL for structured clinical data and MongoDB for flexible prescription documents. JPA entities and MongoDB documents provide a consistent model layer across both storage engines, keeping concerns cleanly separated. 

## Step-by-step architecture walkthrough

Each step represents a tier or logical component in the system and how a request flows through it.

1. **User Interface Layer**
   - **Thymeleaf dashboards**: AdminDashboard and DoctorDashboard render server-side HTML views for browser-based users.
   - **REST API clients**: Modules such as Appointments, PatientDashboard, and PatientRecord call JSON endpoints from web or mobile clients.

2. **Controller Layer**
   - **Thymeleaf Controllers** return `.html` templates populated with model data.
   - **REST Controllers** handle API requests, validate input, and return JSON responses.
   - Controllers are the entry point for request routing and response shaping.

3. **Service Layer**
   - Encapsulates business rules and validations.
   - Coordinates workflows across multiple entities (for example, checking doctor availability before scheduling an appointment).
   - Keeps controllers thin and testable by centralizing application logic.

4. **Repository Layer**
   - **MySQL Repositories** use Spring Data JPA for relational entities like patients, doctors, appointments, and admins.
   - **MongoDB Repository** uses Spring Data MongoDB for document-based prescriptions.
   - Repositories abstract database access with declarative query interfaces.

5. **Database Access**
   - **MySQL** stores normalized, relational data with constraints and relationships.
   - **MongoDB** stores nested or flexible prescription records that benefit from schema evolution.

6. **Model Binding**
   - MySQL rows are mapped into JPA entities annotated with `@Entity`.
   - MongoDB documents are mapped into models annotated with `@Document`.
   - These models provide a unified object representation across the system.

7. **Application Models in Use**
   - **MVC flows**: Controllers pass models to Thymeleaf templates for HTML rendering.
   - **REST flows**: Controllers serialize models (or DTOs) into JSON responses.
   - This completes the request-response cycle for both UI and API clients.
