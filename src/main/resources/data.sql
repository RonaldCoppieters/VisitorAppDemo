INSERT INTO department (code, name) VALUES
	('CAR', 'Cardiologie'),
	('KRA', 'Kraamafdeling'),
	('ONC', 'Oncologie'),
	('GER', 'Geriatrie');


INSERT INTO patient (code, admission, department_code) VALUES
 ('P001', '2020-06-11 14:03:00.0', 'KRA'),
 ('P002', '2020-05-05 07:34:00.0', 'GER'),
 ('P003', '2020-06-12 09:18:00.0', 'KRA'),
 ('P004', '2020-06-10 22:43:00.0', 'ONC'),
 ('P005', '2020-06-08 18:23:00.0', 'KRA'),
 ('P006', '2020-06-11 16:32:00.0', 'KRA');
