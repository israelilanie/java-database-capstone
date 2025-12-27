/*
  doctorServices.js
  Handles all API interactions related to doctor data
  Modular, reusable functions for Admin and Patient dashboards
*/

import { API_BASE_URL } from '../config/config.js';

const DOCTOR_API = API_BASE_URL + '/doctor';

// Get all doctors
export async function getDoctors() {
  try {
    const response = await fetch(DOCTOR_API);
    if (!response.ok) throw new Error('Failed to fetch doctors');
    const data = await response.json();
    return data;
  } catch (error) {
    console.error(error);
    return [];
  }
}

// Delete a doctor (Admin only)
export async function deleteDoctor(id, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/${id}`, {
      method: 'DELETE',
      headers: { 'Authorization': `Bearer ${token}` }
    });
    const data = await response.json();
    return { success: response.ok, message: data.message || '' };
  } catch (error) {
    console.error(error);
    return { success: false, message: 'Error deleting doctor' };
  }
}

// Save (Add) a new doctor (Admin only)
export async function saveDoctor(doctor, token) {
  try {
    const response = await fetch(DOCTOR_API, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(doctor)
    });
    const data = await response.json();
    return { success: response.ok, message: data.message || '' };
  } catch (error) {
    console.error(error);
    return { success: false, message: 'Error saving doctor' };
  }
}

// Filter doctors by name, time, and specialty
export async function filterDoctors(name = '', time = '', specialty = '') {
  try {
    const queryParams = new URLSearchParams();
    if (name) queryParams.append('name', name);
    if (time) queryParams.append('time', time);
    if (specialty) queryParams.append('specialty', specialty);

    const response = await fetch(`${DOCTOR_API}/filter?${queryParams.toString()}`);
    if (!response.ok) throw new Error('Failed to fetch filtered doctors');
    const data = await response.json();
    return data;
  } catch (error) {
    console.error(error);
    return [];
  }
}