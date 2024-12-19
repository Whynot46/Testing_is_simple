-- Создание таблицы roles
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    name VARCHAR(255) NOT NULL  -- Строка для имени роли
);

-- Создание таблицы users
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    first_name VARCHAR(255) NOT NULL,  -- Имя пользователя
    patronymic VARCHAR(255),  -- Отчество пользователя
    last_name VARCHAR(255) NOT NULL,  -- Фамилия пользователя
    password VARCHAR(255) NOT NULL,  -- пароль пользователя
    role_id INT REFERENCES roles(id)  -- Внешний ключ на таблицу roles
);

-- Создание таблицы topic
CREATE TABLE IF NOT EXISTS topic (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    name VARCHAR(255) NOT NULL  -- Строка для имени темы
);

-- Создание таблицы task
CREATE TABLE IF NOT EXISTS task (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    question TEXT NOT NULL,  -- Вопрос задачи
    answer TEXT NOT NULL  -- Ответ задачи
);

-- Создание таблицы test
CREATE TABLE IF NOT EXISTS test (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    topic_id INT REFERENCES topic(id),  -- Внешний ключ на таблицу topic
    tasks_id INT[],  -- Массив идентификаторов задач
    teacher_id INT REFERENCES users(id)  -- Внешний ключ на таблицу users
);

-- Создание таблицы test_result
CREATE TABLE IF NOT EXISTS test_result (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    test_id INT REFERENCES test(id),  -- Внешний ключ на таблицу test
    points FLOAT NOT NULL  -- Вещественное число для оценки
);

-- Создание таблицы admin_profile
CREATE TABLE IF NOT EXISTS admin_profile (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    user_id INT REFERENCES users(id)  -- Внешний ключ на таблицу users
);

-- Создание таблицы teacher_profile
CREATE TABLE IF NOT EXISTS teacher_profile (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    user_id INT REFERENCES users(id)  -- Внешний ключ на таблицу users
);

-- Создание таблицы student_profile
CREATE TABLE IF NOT EXISTS student_profile (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    user_id INT REFERENCES users(id),  -- Внешний ключ на таблицу users
    test_results_id INT[]  -- Массив идентификаторов результатов тестов
);

-- Создание таблицы class
CREATE TABLE IF NOT EXISTS class (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    name VARCHAR(255) NOT NULL,  -- Имя класса
    students_id INT[],  -- Массив идентификаторов студентов
    teacher_id INT REFERENCES users(id)  -- Внешний ключ на таблицу users
);