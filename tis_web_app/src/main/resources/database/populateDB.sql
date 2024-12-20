-- Заполнение таблицы roles
INSERT INTO roles (name) VALUES
('Администратор'),
('Учитель'),
('Студент');

-- Заполнение таблицы users
INSERT INTO users (first_name, patronymic, last_name, password, role_id) VALUES
('Админ', 'Админович', 'Админов', '1111', 1),  -- Администратор
('Препод', 'Иван', 'Николаевич', '1111', 2),  -- Учитель
('Пахалева', 'Дарья', 'Борисовна', '1111', 3),  -- Студент
('Нурмухамедов', 'Наиль', 'Радикович', '1111', 3),  -- Студент
('Препод', 'Иван', 'Иванович', '1111', 2);  -- Учитель

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
INSERT INTO test (topic_id, tasks_id, teacher_id) VALUES
(1, ARRAY[1], 2),
(2, ARRAY[2], 5),
(3, ARRAY[3], 5);

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