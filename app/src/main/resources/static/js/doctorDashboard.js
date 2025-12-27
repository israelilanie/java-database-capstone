/*
  Import getAllAppointments to fetch appointments from the backend
  Import createPatientRow to generate a table row for each patient appointment
*/

import { getAllAppointments } from './services/appointmentRecordService.js';
import { createPatientRow } from './components/patientRows.js';

// Table body where patient rows will be added
const patientTableBody = document.getElementById('patientTableBody');

// Initialize selectedDate with today's date in 'YYYY-MM-DD' format
let selectedDate = new Date().toISOString().split('T')[0];

// Get token from localStorage for authenticated API calls
const token = localStorage.getItem('token');

// Initialize patientName for filtering
let patientName = null;

// Search bar input event listener
document.getElementById('searchBar').addEventListener('input', () => {
  const input = document.getElementById('searchBar').value.trim();
  patientName = input !== '' ? input : 'null';
  loadAppointments();
});

// Today button click listener
document.getElementById('todayButton').addEventListener('click', () => {
  selectedDate = new Date().toISOString().split('T')[0];
  document.getElementById('datePicker').value = selectedDate;
  loadAppointments();
});

// Date picker change event listener
document.getElementById('datePicker').addEventListener('change', () => {
  selectedDate = document.getElementById('datePicker').value;
  loadAppointments();
});

// Function to fetch and display appointments
async function loadAppointments() {
  try {
    const appointments = await getAllAppointments(selectedDate, patientName, token);

    // Clear table body
    patientTableBody.innerHTML = '';

    if (!appointments || appointments.length === 0) {
      const row = document.createElement('tr');
      const cell = document.createElement('td');
      cell.colSpan = 4;
      cell.textContent = 'No Appointments found for today.';
      row.appendChild(cell);
      patientTableBody.appendChild(row);
      return;
    }

    appointments.forEach(app => {
      const patient = {
        id: app.patientId,
        name: app.patientName,
        phone: app.patientPhone,
        email: app.patientEmail
      };
      const row = createPatientRow(patient, app);
      patientTableBody.appendChild(row);
    });

  } catch (error) {
    console.error('Error loading appointments:', error);
    const row = document.createElement('tr');
    const cell = document.createElement('td');
    cell.colSpan = 4;
    cell.textContent = 'Error loading appointments. Try again later.';
    row.appendChild(cell);
    patientTableBody.appendChild(row);
  }
}

// On page load, render content and load today's appointments
document.addEventListener('DOMContentLoaded', () => {
  if (typeof renderContent === 'function') renderContent();
  loadAppointments();
});