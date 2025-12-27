/*
  Header Component
  ----------------
  This file dynamically renders the header based on:
  - Current page
  - User role
  - Login/session state

  Roles supported:
  - admin
  - doctor
  - patient
  - loggedPatient
*/

function renderHeader() {
  const headerDiv = document.getElementById("header");
  if (!headerDiv) return;

  /* --------------------------------------------------
     1. Handle Homepage (No Role-Based Header)
  -------------------------------------------------- */
  if (window.location.pathname.endsWith("/")) {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");

    headerDiv.innerHTML = `
      <header class="header">
        <div class="logo-section">
          <img src="./assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
          <span class="logo-title">Hospital CMS</span>
        </div>
      </header>
    `;
    return;
  }

  /* --------------------------------------------------
     2. Get Role & Token
  -------------------------------------------------- */
  const role = localStorage.getItem("userRole");
  const token = localStorage.getItem("token");

  /* --------------------------------------------------
     3. Handle Invalid / Expired Sessions
  -------------------------------------------------- */
  if (
    (role === "loggedPatient" || role === "admin" || role === "doctor") &&
    !token
  ) {
    localStorage.removeItem("userRole");
    alert("Session expired or invalid login. Please log in again.");
    window.location.href = "/";
    return;
  }

  /* --------------------------------------------------
     4. Base Header Layout
  -------------------------------------------------- */
  let headerContent = `
    <header class="header">
      <div class="logo-section">
        <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
        <span class="logo-title">Hospital CMS</span>
      </div>
      <nav class="nav-actions">
  `;

  /* --------------------------------------------------
     5. Role-Based Navigation
  -------------------------------------------------- */
  if (role === "admin") {
    headerContent += `
      <button id="addDocBtn" class="adminBtn">Add Doctor</button>
      <a href="#" id="logoutBtn">Logout</a>
    `;
  }

  else if (role === "doctor") {
    headerContent += `
      <button id="doctorHome" class="adminBtn">Home</button>
      <a href="#" id="logoutBtn">Logout</a>
    `;
  }

  else if (role === "patient") {
    headerContent += `
      <button id="patientLogin" class="adminBtn">Login</button>
      <button id="patientSignup" class="adminBtn">Sign Up</button>
    `;
  }

  else if (role === "loggedPatient") {
    headerContent += `
      <button id="home" class="adminBtn">Home</button>
      <button id="patientAppointments" class="adminBtn">Appointments</button>
      <a href="#" id="logoutPatientBtn">Logout</a>
    `;
  }

  headerContent += `
      </nav>
    </header>
  `;

  /* --------------------------------------------------
     6. Inject Header
  -------------------------------------------------- */
  headerDiv.innerHTML = headerContent;

  /* --------------------------------------------------
     7. Attach Event Listeners
  -------------------------------------------------- */
  attachHeaderButtonListeners();
}

/* --------------------------------------------------
   Attach Button Listeners
-------------------------------------------------- */
function attachHeaderButtonListeners() {
  const addDocBtn = document.getElementById("addDocBtn");
  if (addDocBtn) {
    addDocBtn.addEventListener("click", () => openModal("addDoctor"));
  }

  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", logout);
  }

  const logoutPatientBtn = document.getElementById("logoutPatientBtn");
  if (logoutPatientBtn) {
    logoutPatientBtn.addEventListener("click", logoutPatient);
  }

  const patientLogin = document.getElementById("patientLogin");
  if (patientLogin) {
    patientLogin.addEventListener("click", () => openModal("patientLogin"));
  }

  const patientSignup = document.getElementById("patientSignup");
  if (patientSignup) {
    patientSignup.addEventListener("click", () => openModal("patientSignup"));
  }

  const homeBtn = document.getElementById("home");
  if (homeBtn) {
    homeBtn.addEventListener("click", () => {
      window.location.href = "/pages/loggedPatientDashboard.html";
    });
  }

  const appointmentsBtn = document.getElementById("patientAppointments");
  if (appointmentsBtn) {
    appointmentsBtn.addEventListener("click", () => {
      window.location.href = "/pages/patientAppointments.html";
    });
  }

  const doctorHome = document.getElementById("doctorHome");
  if (doctorHome) {
    doctorHome.addEventListener("click", () => {
      window.location.href = "/doctor/doctorDashboard.html";
    });
  }
}

/* --------------------------------------------------
   Logout Helpers
-------------------------------------------------- */
function logout() {
  localStorage.removeItem("token");
  localStorage.removeItem("userRole");
  window.location.href = "/";
}

function logoutPatient() {
  localStorage.removeItem("token");
  localStorage.setItem("userRole", "patient");
  window.location.href = "/pages/patientDashboard.html";
}

/* --------------------------------------------------
   Initialize Header
-------------------------------------------------- */
renderHeader();
