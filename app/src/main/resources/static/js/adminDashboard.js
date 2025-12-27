/*
  This script handles the admin dashboard functionality for managing doctors:
  - Loads all doctor cards
  - Filters doctors by name, time, or specialty
  - Adds a new doctor via modal form
*/

import { openModal } from './components/modals.js';
import { getDoctors, filterDoctors, saveDoctor } from './services/doctorServices.js';
import { createDoctorCard } from './components/doctorCard.js';

// Attach click listener to "Add Doctor" button
document.getElementById('addDocBtn').addEventListener('click', () => openModal('addDoctor'));

// Load doctor cards when DOM is fully loaded
window.addEventListener('DOMContentLoaded', loadDoctorCards);

async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error('Error loading doctors:', error);
  }
}

// Render doctor cards helper
function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById('content');
  contentDiv.innerHTML = '';

  if (!doctors || doctors.length === 0) {
    contentDiv.textContent = 'No doctors found.';
    return;
  }

  doctors.forEach(doctor => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

// Attach search and filter listeners
document.getElementById('searchBar').addEventListener('input', filterDoctorsOnChange);
document.getElementById('filterTime').addEventListener('change', filterDoctorsOnChange);
document.getElementById('filterSpecialty').addEventListener('change', filterDoctorsOnChange);

async function filterDoctorsOnChange() {
  try {
    const name = document.getElementById('searchBar').value || null;
    const time = document.getElementById('filterTime').value || null;
    const specialty = document.getElementById('filterSpecialty').value || null;

    const doctors = await filterDoctors(name, time, specialty);

    if (doctors && doctors.length > 0) {
      renderDoctorCards(doctors);
    } else {
      const contentDiv = document.getElementById('content');
      contentDiv.textContent = 'No doctors found with the given filters.';
    }
  } catch (error) {
    console.error('Error filtering doctors:', error);
    alert('Failed to filter doctors. Please try again.');
  }
}

// Add doctor via modal form
export async function adminAddDoctor() {
  const name = document.getElementById('doctorName').value;
  const email = document.getElementById('doctorEmail').value;
  const phone = document.getElementById('doctorPhone').value;
  const password = document.getElementById('doctorPassword').value;
  const specialty = document.getElementById('doctorSpecialty').value;

  const availabilityCheckboxes = document.querySelectorAll('input[name="availability"]:checked');
  const availability = Array.from(availabilityCheckboxes).map(cb => cb.value);

  const token = localStorage.getItem('token');
  if (!token) {
    alert('Admin not authenticated. Please login again.');
    return;
  }

  const doctor = { name, email, phone, password, specialty, availability };

  try {
    const result = await saveDoctor(doctor, token);
    if (result.success) {
      alert('Doctor added successfully!');
      document.getElementById('addDoctorForm').reset();
      // Close modal if using a modal library, e.g., closeModal('addDoctor');
      loadDoctorCards();
    } else {
      alert('Failed to add doctor: ' + result.message);
    }
  } catch (error) {
    console.error('Error adding doctor:', error);
    alert('An unexpected error occurred.');
  }
}