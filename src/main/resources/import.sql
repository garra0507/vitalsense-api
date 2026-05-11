INSERT INTO users (user_id, username, email, password, first_name, last_name, role, active) VALUES (1, 'pacienteprueba', 'paciente@vitalsense.com', 'hashedpwd123', 'Juan', 'Perez', 'PATIENT', true);
INSERT INTO users (user_id, username, email, password, first_name, last_name, role, active) VALUES (2, 'doctorprueba', 'doctor@vitalsense.com', 'hashedpwd456', 'Dra. Maria', 'Gomez', 'DOCTOR', true);
INSERT INTO patients (patient_id, user_id, age, gender, emergency_contact) VALUES (1, 1, 36, 'MALE', 'Maria Perez: 999888777');
INSERT INTO doctors (doctor_id, user_id, specialty, years_of_experience, consultation_fee, biography) VALUES (2, 2, 'Cardiología', 10, 150.00, 'Especialista en cardiología intervencionista con 10 años de experiencia.');
SELECT setval(pg_get_serial_sequence('users', 'user_id'), COALESCE(MAX(user_id), 1)) FROM users;
SELECT setval(pg_get_serial_sequence('patients', 'patient_id'), COALESCE(MAX(patient_id), 1)) FROM patients;
SELECT setval(pg_get_serial_sequence('doctors', 'doctor_id'), COALESCE(MAX(doctor_id), 1)) FROM doctors;
