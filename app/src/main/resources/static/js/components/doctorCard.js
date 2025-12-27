

import { showBookingOverlay } from '../loggedPatient.js';
import { deleteDoctor } from '../services/doctorServices.js';
import { getPatientData } from '../services/patientServices.js';

export function createDoctorCard(doctor) {
  const card = document.createElement('div');
  card.classList.add('doctor-card');

  const role = localStorage.getItem('userRole');

  const infoDiv = document.createElement('div');
  infoDiv.classList.add('doctor-info');

  const name = document.createElement('h3');
  name.textContent = doctor.name;

  const specialization = document.createElement('p');
  specialization.textContent = `Specialty: ${doctor.specialization}`;

  const email = document.createElement('p');
  email.textContent = `Email: ${doctor.email}`;

  const availability = document.createElement('p');
  availability.textContent = `Availability: ${doctor.availability.join(', ')}`;

  infoDiv.appendChild(name);
  infoDiv.appendChild(specialization);
  infoDiv.appendChild(email);
  infoDiv.appendChild(availability);

  const actionsDiv = document.createElement('div');
  actionsDiv.classList.add('card-actions');

  if (role === 'admin') {
    const removeBtn = document.createElement('button');
    removeBtn.textContent = 'Delete';
    removeBtn.addEventListener('click', async () => {
      const confirmDelete = confirm('Are you sure you want to delete this doctor?');
      if (!confirmDelete) return;

      const token = localStorage.getItem('token');
      try {
        await deleteDoctor(doctor.id, token);
        card.remove();
      } catch (error) {
        alert('Error deleting doctor');
        console.error(error);
      }
    });
    actionsDiv.appendChild(removeBtn);
  } else if (role === 'patient') {
    const bookBtn = document.createElement('button');
    bookBtn.textContent = 'Book Now';
    bookBtn.addEventListener('click', () => alert('Please login to book an appointment.'));
    actionsDiv.appendChild(bookBtn);
  } else if (role === 'loggedPatient') {
    const bookBtn = document.createElement('button');
    bookBtn.textContent = 'Book Now';
    bookBtn.addEventListener('click', async (e) => {
      const token = localStorage.getItem('token');
      if (!token) return alert('Token not available, please login again.');
      const patientData = await getPatientData(token);
      showBookingOverlay(e, doctor, patientData);
    });
    actionsDiv.appendChild(bookBtn);
  }

  card.appendChild(infoDiv);
  card.appendChild(actionsDiv);

  return card;
}
