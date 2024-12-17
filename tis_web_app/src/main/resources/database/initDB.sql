-- Создание таблицы roles
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    name VARCHAR(255) NOT NULL  -- Строка для имени роли
);

-- Создание таблицы users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    first_name VARCHAR(255) NOT NULL,  -- Имя пользователя
    patronymic VARCHAR(255),  -- Отчество пользователя
    last_name VARCHAR(255) NOT NULL,  -- Фамилия пользователя
    role_id INT REFERENCES roles(id)  -- Внешний ключ на таблицу roles
);

-- Создание таблицы topic
CREATE TABLE topic (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    name VARCHAR(255) NOT NULL  -- Строка для имени темы
);

-- Создание таблицы task
CREATE TABLE task (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    question TEXT NOT NULL,  -- Вопрос задачи
    answer TEXT NOT NULL  -- Ответ задачи
);

-- Создание таблицы test
CREATE TABLE test (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    topic_id INT REFERENCES topic(id),  -- Внешний ключ на таблицу topic
    tasks_id INT[] REFERENCES task(id),  -- Массив идентификаторов задач
    teacher_id INT REFERENCES users(id)  -- Внешний ключ на таблицу users
);

-- Создание таблицы test_result
CREATE TABLE test_result (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    test_id INT REFERENCES test(id),  -- Внешний ключ на таблицу test
    points FLOAT NOT NULL  -- Вещественное число для оценки
);

-- Создание таблицы admin_profile
CREATE TABLE admin_profile (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    user_id INT REFERENCES users(id)  -- Внешний ключ на таблицу users
);

-- Создание таблицы teacher_profile
CREATE TABLE teacher_profile (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    user_id INT REFERENCES users(id)  -- Внешний ключ на таблицу users
);

-- Создание таблицы student_profile
CREATE TABLE student_profile (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    user_id INT REFERENCES users(id),  -- Внешний ключ на таблицу users
    test_results_id INT[] REFERENCES test_result(id)  -- Массив идентификаторов результатов тестов
);

-- Создание таблицы class
CREATE TABLE class (
    id SERIAL PRIMARY KEY,  -- Автоинкрементный идентификатор
    name VARCHAR(255) NOT NULL,  -- Имя класса
    students_id INT[] REFERENCES users(id),  -- Массив идентификаторов студентов
    teacher_id INT REFERENCES users(id)  -- Внешний ключ на таблицу users
);