INSERT INTO location (latitude, longitude) VALUES (42.004, 21.409);

INSERT INTO hospital (name, network_address, location_id) VALUES ('Saint P.', '192.168.100.0/24', 1);

INSERT INTO sensor_sync_application (name_of_application, provided_by_hospital_id) VALUES ('Health monitoring sensor', 1);

INSERT INTO user (active, email, doctor, name, password, phone_number, uses_sensor_sync_application_id) VALUES (1, 'john@mail.com', 0, 'John Malkovich', 'john', '070 111 111', 1);
INSERT INTO user (active, email, doctor, name, password, phone_number, uses_sensor_sync_application_id) VALUES (1, 'bob@mail.com', 1, 'Bob Dob', 'bob', '070 111 111', 1);

INSERT INTO sensor(regular_from, regular_to, type, unit, owner_id) VALUES (60, 140, 'Pulse', 'bpm', 2);
INSERT INTO sensor(regular_from, regular_to, type, unit, owner_id) VALUES (20 ,60, 'Temperature', 'C', 1);

INSERT INTO observation (time, value, sensor_id) VALUES (CURRENT_TIMESTAMP , 66, 1);
INSERT INTO observation (time, value, sensor_id) VALUES (CURRENT_TIMESTAMP , 57, 1);
INSERT INTO observation (time, value, sensor_id) VALUES (CURRENT_TIMESTAMP , 55, 2);

INSERT INTO treatment(treatment_from_date, patient_request, treatment_to_date, for_patient_id, has_doctor_id)
VALUES (current_timestamp, 'Feeling pain and coughing', CURRENT_TIMESTAMP, 1, 2);
INSERT INTO treatment(diagnosis, treatment_from_date, patient_request, treatment_to_date, for_patient_id, has_doctor_id)
VALUES ('Blood diagnosis done. You need to lower the iron level', CURRENT_TIMESTAMP , 'Request for blood test', CURRENT_TIMESTAMP, 1, 2);
INSERT INTO treatment(treatment_from_date, patient_request, for_patient_id)
VALUES (CURRENT_TIMESTAMP , 'Request for test', 1);