-- Заполнение таблицы roles
INSERT INTO roles (name) VALUES
('ADMIN'),
('TEACHER'),
('STUDENT');

-- Заполнение таблицы users
INSERT INTO users (first_name, patronymic, last_name, password_hash, role_id) VALUES
('Админ', 'Админович', 'Админов', '$2a$10$zkiBQmq5Ih3bZ4Y5UYWq8eiCsWoF.f29Tf0r3ymX9ZNl3UvTvWL.m', 1),  -- Администратор
('Иван', 'Николаевич', 'Препод', '$2a$10$zkiBQmq5Ih3bZ4Y5UYWq8eiCsWoF.f29Tf0r3ymX9ZNl3UvTvWL.m', 2),  -- Учитель
('Дарья', 'Борисовна', 'Пахалева', '$2a$10$zkiBQmq5Ih3bZ4Y5UYWq8eiCsWoF.f29Tf0r3ymX9ZNl3UvTvWL.m', 3),  -- Студент
('Наиль', 'Радикович', 'Нурмухамедов', '$2a$10$zkiBQmq5Ih3bZ4Y5UYWq8eiCsWoF.f29Tf0r3ymX9ZNl3UvTvWL.m', 3),  -- Студент
('Иван', 'Иванович', 'Препод', '$2a$10$zkiBQmq5Ih3bZ4Y5UYWq8eiCsWoF.f29Tf0r3ymX9ZNl3UvTvWL.m', 2);  -- Учитель

-- Заполнение таблицы topic
INSERT INTO topic (name) VALUES
('Алгебра'),
('Физика'),
('Химия');

-- Заполнение таблицы task
INSERT INTO task (question, answer) VALUES
('Сколько будет 2 + 2?', '4'),
('Какова формула воды?', 'H2O'),
('Что такое сила?', 'Сила — это воздействие на тело, которое изменяет его движение.');

-- Заполнение таблицы test
INSERT INTO test (topic_id, name, tasks_id, teacher_id) VALUES
(1, 'Самый лёгкий тест', ARRAY[1], 2),
(2, 'Самый средний тест', ARRAY[2], 5),
(3, 'Самый тяжёлый тест', ARRAY[3], 5);

-- Заполнение таблицы test_result
INSERT INTO test_result (test_id, points) VALUES
(1, 95.0),  -- Результат теста по математике
(2, 44.5),  -- Результат теста по физике
(3, 23.0);  -- Результат теста по химии

-- Заполнение таблицы admin_profile
INSERT INTO admin_profile (user_id) VALUES
(1);  -- Профиль администратора

-- Заполнение таблицы teacher_profile
INSERT INTO teacher_profile (user_id, topics_id) VALUES
(2, ARRAY[1]),  -- Профиль учителя Петрова
(5, ARRAY[2]);  -- Профиль учителя Дмитриева

-- Заполнение таблицы student_profile
INSERT INTO student_profile (user_id, test_results_id) VALUES
(3, ARRAY[1]),  -- Профиль студента Сидоровой с результатом теста по математике
(4, ARRAY[2]);  -- Профиль студента Алексеева с результатом теста по физике

-- Заполнение таблицы class
INSERT INTO class (name, students_id, teacher_id) VALUES
('10А', ARRAY[3, 4], 2);  -- Класс 10А с учениками Сидоровой и Алексеева, учитель Петров