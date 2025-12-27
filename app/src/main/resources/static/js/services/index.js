/*
  Import the openModal function to handle showing login popups/modals
  Import the base API URL from the config file
  Define constants for the admin and doctor login API endpoints using the base URL
*/

import { openModal } from '../components/modals.js';
import { API_BASE_URL } from '../config/config.js';

const ADMIN_API = API_BASE_URL + '/admin';
const DOCTOR_API = API_BASE_URL + '/doctor/login';

/*
  Use the window.onload event to ensure DOM elements are available after page load
*/
window.onload = function () {
  const adminBtn = document.getElementById('adminLogin');
  const doctorBtn = document.getElementById('doctorLogin');

  if (adminBtn) {
    adminBtn.addEventListener('click', () => openModal('adminLogin'));
  }

  if (doctorBtn) {
    doctorBtn.addEventListener('click', () => openModal('doctorLogin'));
  }
};

/*
  Define a function named adminLoginHandler on the global window object
  This function will be triggered when the admin submits their login credentials
*/
window.adminLoginHandler = async function () {
  const username = document.getElementById('adminUsername').value;
  const password = document.getElementById('adminPassword').value;
  const admin = { username, password };

  try {
    const response = await fetch(ADMIN_API, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(admin)
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem('token', data.token);
      window.selectRole('admin');
    } else {
      alert('Invalid credentials!');
    }
  } catch (error) {
    console.error(error);
    alert('An error occurred during login. Please try again.');
  }
};

/*
  Define a function named doctorLoginHandler on the global window object
  This function will be triggered when a doctor submits their login credentials
*/
window.doctorLoginHandler = async function () {
  const email = document.getElementById('doctorEmail').value;
  const password = document.getElementById('doctorPassword').value;
  const doctor = { email, password };

  try {
    const response = await fetch(DOCTOR_API, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(doctor)
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem('token', data.token);
      window.selectRole('doctor');
    } else {
      alert('Invalid credentials!');
    }
  } catch (error) {
    console.error(error);
    alert('An error occurred during login. Please try again.');
  }
};
