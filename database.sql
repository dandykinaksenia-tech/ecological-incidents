DROP DATABASE IF EXISTS environmental_incidents;
CREATE DATABASE environmental_incidents;
USE environmental_incidents;

-- Таблиця локація
CREATE TABLE location(
id BIGINT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(150) NOT NULL,
region VARCHAR(150) NOT NULL,
address VARCHAR(255),
latitude DECIMAL(9,6),
longitude DECIMAL(9,6),
description TEXT
);

-- Таблиця інцидентів
CREATE TABLE incident (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
title VARCHAR(150) NOT NULL,
description TEXT,
incident_type VARCHAR(100) NOT NULL,
location_id BIGINT NOT NULL,
f1 DECIMAL(5,2) NOT NULL,
f2 DECIMAL(5,2) NOT NULL,
f3 DECIMAL(5,2) NOT NULL,
integral_score DECIMAL(5,2) NOT NULL,
status VARCHAR(20) NOT NULL,
created_at DATETIME NOT NULL,
 FOREIGN KEY (location_id) REFERENCES location(id)
);

-- Таблиця спостережень
CREATE TABLE observation(
id BIGINT AUTO_INCREMENT PRIMARY KEY,
incident_id BIGINT NOT NULL,
observation_date DATETIME NOT NULL,
indicator_name VARCHAR(100) NOT NULL,
indicator_value DECIMAL(10,3) NOT NULL,
unit VARCHAR(30) NOT NULL,
description TEXT,
source VARCHAR(150),
FOREIGN KEY (incident_id) REFERENCES incident(id)
);

-- Таблиця заходів реагування
CREATE TABLE response_action (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
incident_id BIGINT NOT NULL,
title VARCHAR(150) NOT NULL,
description TEXT,
responsible_person VARCHAR(150),
planned_date DATETIME NOT NULL,
completed_date DATETIME,
status VARCHAR(30) NOT NULL,
FOREIGN KEY (incident_id) REFERENCES incident(id)
);

INSERT INTO location (name, region, address, latitude, longitude) VALUES
('Промислова зона', 'Полтавська область', 'м. Полтава, вул. Заводська', 49.5880, 34.5510),
('Річкова зона', 'Полтавська область', 'м. Полтава, вул. Набережна', 49.5750, 34.5400);

INSERT INTO incident(title, description, incident_type, location_id, f1, f2, f3, integral_score, status, created_at) VALUES
('Забруднення повітря', 'Перевищення допустимої концентрації шкідливих речовин', 'Забруднення повітря', 1,60.00, 70.00, 45.00, 59.75, 'Зареєстровано', NOW()),
('Забруднення води', 'Виявлено забруднення води у водоймі','Забруднення води', 2,40.00, 50.00, 30.00, 41.00, 'Зареєстровано', NOW());

INSERT INTO observation(incident_id, observation_date, indicator_name,indicator_value, unit, description, source) VALUES
(1, NOW(), 'Концентрація шкідливих речовин',45.500, 'мг/м³','Зафіксовано підвищений рівень забруднення повітря','Екологічна інспекція'),
(2, NOW(), 'Концентрація забруднювачів', 12.300, 'мг/л','Виявлено забруднення води','Лабораторне дослідження');

INSERT INTO response_action(incident_id, title, description, responsible_person,planned_date, completed_date, status) VALUES
(1, 'Перевірка джерела забруднення','Встановлення джерела забруднення повітря та перевірка його впливу','Іваненко І.І.','2026-11-18 09:00:00', NULL, 'Заплановано'),
(2, 'Аналіз стану води', 'Відбір проб води та проведення лабораторного дослідження','Петренко П.П.','2026-11-19 10:00:00', NULL, 'Заплановано');