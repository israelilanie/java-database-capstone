// render.js

function selectRole(role) {
  if (!role) {
    return;
  }

  const normalizedRole = role.toLowerCase();
  const storedRole = normalizedRole === "loggedpatient" ? "loggedPatient" : normalizedRole;
  setRole(storedRole);

  const token = localStorage.getItem('token');
  if (normalizedRole === "admin") {
    if (token) {
      window.location.href = `/adminDashboard/${token}`;
    }
    return;
  }

  if (normalizedRole === "patient") {
    window.location.href = "/pages/patientDashboard.html";
    return;
  }

  if (normalizedRole === "doctor") {
    if (token) {
      window.location.href = `/doctorDashboard/${token}`;
    }
    return;
  }

  if (normalizedRole === "loggedpatient") {
    window.location.href = "/pages/loggedPatientDashboard.html";
  }
}


function renderContent() {
  const role = getRole();
  if (!role) {
    window.location.href = "/"; // if no role, send to role selection page
    return;
  }
}
