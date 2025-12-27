/*
  patientServices.js
  Handles all API interactions related to patient data
  Centralized functions for signup, login, appointments
*/

import { API_BASE_URL } from '../config/config.js';

const PATIENT_API = API_BASE_URL + '/patient';

// Patient signup
export async function patientSignup(data) {
  try {
    const response = await fetch(`${PATIENT_API}/signup`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    const result = await response.json();
    return { success: response.ok, message: result.message || '' };
  } catch (error) {
    console.error(error);
    return { success: false, message: 'Error during signup' };
  }
}

// Patient login
export async function patientLogin(data) {
  try {
    const response = await fetch(`${PATIENT_API}/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    return response;
  } catch (error) {
    console.error(error);
    return null;
  }
}

// Get logged-in patient data
export async function getPatientData(token) {
  try {
    const response = await fetch(`${PATIENT_API}/me`, {
      method: 'GET',
      headers: { 'Authorization': `Bearer ${token}` }
    });
    if (!response.ok) throw new Error('Failed to fetch patient data');
    const data = await response.json();
    return data;
  } catch (error) {
    console.error(error);
    return null;
  }
}

// Get patient appointments (supports patient and doctor dashboards)
export async function getPatientAppointments(id, token, user) {
  try {
    const endpoint = user === 'doctor' ? `${PATIENT_API}/${id}/appointments` : `${PATIENT_API}/appointments`;
    const response = await fetch(endpoint, {
      method: 'GET',
      headers: { 'Authorization': `Bearer ${token}` }
    });
    if (!response.ok) throw new Error('Failed to fetch appointments');
    const data = await response.json();
    return data;
  } catch (error) {
    console.error(error);
    return null;
  }
}

// Filter patient appointments
export async function filterAppointments(condition = '', name = '', token) {
  try {
    const queryParams = new URLSearchParams();
    if (condition) queryParams.append('condition', condition);
    if (name) queryParams.append('name', name);

    const response = await fetch(`${PATIENT_API}/appointments/filter?${queryParams.toString()}`, {
      method: 'GET',
      headers: { 'Authorization': `Bearer ${token}` }
    });
    if (!response.ok) throw new Error('Failed to fetch filtered appointments');
    const data = await response.json();
    return data;
  } catch (error) {
    console.error(error);
    alert('Error fetching filtered appointments');
    return [];
  }
}