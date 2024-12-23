package com.tis.tis_web_app;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DataBase {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/TIS_WebApp");
        config.setUsername("system-user");
        config.setPassword("100415");
        config.setMaximumPoolSize(10); // Максимальное количество соединений в пуле
        dataSource = new HikariDataSource(config);
        System.out.println("Пул соединений с базой данных инициализирован.");
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection(); // Получаем соединение из пула
    }

    public static Boolean is_old(int user_id) {
        String query = "SELECT COUNT(*) FROM users WHERE id = ?";
        try (Connection connection = getConnection(); // Получаем соединение из пула
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Если количество больше 0, значит запись существует
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void add_user(String firstName, String patronymic, String lastName, String password, int role_id) {
        // Проверяем, существует ли пользователь
        if (get_user(firstName, patronymic, lastName) != null) {
            System.out.println("Пользователь уже существует");
            return; // Если пользователь существует, выходим из метода
        }
    
        String query = "INSERT INTO users (first_name, patronymic, last_name, password_hash, role_id) VALUES (?, ?, ?, ?, ?)";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password_hash = passwordEncoder.encode(password);
        try (Connection connection = getConnection(); // Получаем соединение из пула
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, patronymic);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, password_hash);
            preparedStatement.setInt(5, role_id);
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Пользователь успешно добавлен");
            } else {
                System.out.println("Не удалось добавить пользователя");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void add_test(int id, int topic_id, ArrayList<Integer> tasks_id, int teacher_id) {
        String query = "INSERT INTO tests (id, topic_id, teacher_id) VALUES (?, ?, ?)";
        
        try (Connection connection = getConnection(); // Получаем соединение из пула
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id); // Устанавливаем идентификатор теста
            preparedStatement.setInt(2, topic_id); // Устанавливаем идентификатор темы
            preparedStatement.setInt(3, teacher_id); // Устанавливаем идентификатор преподавателя
            
            int rowsAffected = preparedStatement.executeUpdate(); // Выполняем вставку
            
            if (rowsAffected > 0) {
                System.out.println("Тест успешно добавлен");
                // Если есть задачи, добавляем их в таблицу test_tasks
                if (!tasks_id.isEmpty()) {
                    for (Integer task_id : tasks_id) {
                        String task_query = "INSERT INTO test_tasks (test_id, task_id) VALUES (?, ?)";
                        try (PreparedStatement taskPreparedStatement = connection.prepareStatement(task_query)) {
                            taskPreparedStatement.setInt(1, id); // Устанавливаем идентификатор теста
                            taskPreparedStatement.setInt(2, task_id); // Устанавливаем идентификатор задачи
                            taskPreparedStatement.executeUpdate(); // Выполняем вставку
                        }
                    }
                }
            } else {
                System.out.println("Не удалось добавить тест");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void add_task(int task_id, String question, String answer) {
        String query = "INSERT INTO tasks (id, question, answer) VALUES (?, ?, ?)";
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, task_id); // Устанавливаем идентификатор задачи
            preparedStatement.setString(2, question); // Устанавливаем вопрос
            preparedStatement.setString(3, answer); // Устанавливаем ответ
            
            int rowsAffected = preparedStatement.executeUpdate(); // Выполняем вставку
            
            if (rowsAffected > 0) {
                System.out.println("Задача успешно добавлена");
            } else {
                System.out.println("Не удалось добавить задачу");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<User> get_users(){
        List<User> users = new ArrayList<>();
        String query = "SELECT id, first_name, patronymic, last_name, password_hash, role_id FROM users"; // Предполагается, что role_id хранится в базе данных

        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String first_name = resultSet.getString("first_name");
                String patronymic = resultSet.getString("patronymic");
                String last_name = resultSet.getString("last_name");
                String password_hash = resultSet.getString("password_hash");
                int role_id = resultSet.getInt("role_id");

                User user = new User(id, first_name, patronymic, last_name, password_hash, role_id);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users; 
    }

    public static User get_user(int user_id) {
        User user = null;
        String query = "SELECT id, first_name, patronymic, last_name, password_hash, role_id FROM users WHERE id = ?"; // SQL-запрос для получения пользователя по id
    
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, user_id); // Устанавливаем значение user_id в запрос
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполняем запрос
    
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String first_name = resultSet.getString("first_name");
                String patronymic = resultSet.getString("patronymic");
                String last_name = resultSet.getString("last_name");
                String password_hash = resultSet.getString("password_hash");
                int role_id = resultSet.getInt("role_id"); // Получаем role_id из результата
    
                user = new User(id, first_name, patronymic, last_name, password_hash, role_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return user;
    }

    public static User get_user(String last_name, String first_name, String patronymic) {
        User user = null;
        String query = "SELECT id, first_name, patronymic, last_name, password_hash, role_id FROM users WHERE first_name = ? AND patronymic = ? AND last_name = ?";
    
        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, patronymic);
            preparedStatement.setString(3, last_name);
            
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String password_hash = resultSet.getString("password_hash");
                int role_id = resultSet.getInt("role_id");
    
                user = new User(id, first_name, patronymic, last_name, password_hash, role_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return user;
    }

    public static List<Role> get_roles() {
        List<Role> roles = new ArrayList<>();
        String query = "SELECT id, name FROM roles"; // SQL-запрос для получения id и name ролей
    
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
    
            while (resultSet.next()) {
                // Извлекаем данные из результата
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
    
                // Создаем объект Role
                Role role = new Role(id, name); // Используем конструктор Role с id и name
    
                roles.add(role); // Добавляем роль в список
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roles; 
    }

    public static Role get_role(int role_id) {
        Role role = null; // Изначально роль равна null
        String query = "SELECT name FROM roles WHERE id = ?"; // SQL-запрос для получения роли по id
    
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, role_id); // Устанавливаем значение role_id в запрос
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполняем запрос
    
            if (resultSet.next()) { // Если есть результат
                String name = resultSet.getString("name");
                role = new Role(role_id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Обработка исключений
        }
        
        return role;
    }

    public static String get_role_name(int role_id) {
        String role_name = null;
        String query = "SELECT name FROM roles WHERE id = ?"; // SQL-запрос для получения name по role_id
    
        try (Connection connection = getConnection(); // Получаем соединение из пула
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, role_id); // Устанавливаем значение role_id в запрос
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполняем запрос
    
            if (resultSet.next()) { // Если есть результат
                role_name = resultSet.getString("name"); // Получаем name
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Обработка исключений
        }
    
        return role_name;
    }

    public static List<Task> get_tasks() {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT id, question, answer FROM tasks"; // SQL-запрос для получения id, question и answer задач
    
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
    
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String question = resultSet.getString("question");
                String answer = resultSet.getString("answer");
    
                Task task = new Task(id, question, answer);
                tasks.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks; 
    }

    public static Task get_task(int task_id) {
        Task task = null;
        String query = "SELECT question, answer FROM tasks WHERE id = ?"; 
    
        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, task_id);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                String question = resultSet.getString("question");
                String answer = resultSet.getString("answer");
    
                task = new Task(task_id, question, answer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return task;
    }

    private static ArrayList<Integer> get_tasks_id_by_test_id(int test_id) {
        ArrayList<Integer> tasks_id = new ArrayList<>();
        String query = "SELECT task_id FROM test_tasks WHERE test_id = ?";
    
        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, test_id);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            while (resultSet.next()) {
                int task_id = resultSet.getInt("task_id");
                tasks_id.add(task_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return tasks_id;
    }

    public static ArrayList<Test> get_tests() {
        ArrayList<Test> tests = new ArrayList<>();
        String query = "SELECT id, topic_id, teacher_id FROM tests";
    
        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
    
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int topic_id = resultSet.getInt("topic_id");
                int teacher_id = resultSet.getInt("teacher_id");
                ArrayList<Integer> tasks_id = get_tasks_id_by_test_id(id);
                Test test = new Test(id, topic_id, tasks_id, teacher_id);
                tests.add(test);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tests; 
    }

    public static Test get_test(int test_id) {
        Test test = null;
        String query = "SELECT id, topic_id, teacher_id FROM tests WHERE id = ?"; 
    
        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, test_id);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int topic_id = resultSet.getInt("topic_id");
                int teacher_id = resultSet.getInt("teacher_id");
                ArrayList<Integer> tasks_id = get_tasks_id_by_test_id(test_id);
                test = new Test(id, topic_id, tasks_id, teacher_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return test;
    }

    public static ArrayList<TestResult> get_test_results() {
        ArrayList<TestResult> testResults = new ArrayList<>();
        String query = "SELECT id, test_id, points FROM test_results"; // SQL query to get test results
    
        try (Connection connection = DataBase.getConnection(); // Get connection from the DataBase class
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
    
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int test_id = resultSet.getInt("test_id");
                int points = resultSet.getInt("points");
    
                TestResult testResult = new TestResult(id, test_id, points);
                testResults.add(testResult);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return testResults;
    }

    public static TestResult get_test_result(int test_result_id) {
        TestResult test_result = null;
        String query = "SELECT id, test_id, points FROM test_results WHERE id = ?";
    
        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setInt(1, test_result_id);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int test_id = resultSet.getInt("test_id");
                int points = resultSet.getInt("points");
    
                test_result = new TestResult(id, test_id, points);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return test_result;
    }

    public static ArrayList<Topic> get_topics() {
        ArrayList<Topic> topics = new ArrayList<>();
        String query = "SELECT id, name FROM topics";
    
        try (Connection connection = DataBase.getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
    
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
    
                Topic topic = new Topic(id, name); 
                topics.add(topic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return topics;
    }

    public static Topic get_topic(int topic_id) {
        Topic topic = null;
        String query = "SELECT id, name FROM topics WHERE id = ?";
    
        try (Connection connection = DataBase.getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, topic_id); 
            ResultSet resultSet = preparedStatement.executeQuery(); 
    
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
    
                topic = new Topic(id, name); 
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        
        return topic;
    }

    public static ArrayList<Integer> get_students_id_by_class_id(int class_id) {
        ArrayList<Integer> students_id = new ArrayList<>();
        String query = "SELECT student_id FROM class_students WHERE class_id = ?"; 
    
        try (Connection connection = DataBase.getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, class_id); 
            ResultSet resultSet = preparedStatement.executeQuery(); 
    
            while (resultSet.next()) { 
                int student_id = resultSet.getInt("student_id");
                students_id.add(student_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return students_id;
    }

    public static ArrayList<Class> get_classes() {
        ArrayList<Class> classes = new ArrayList<>();
        String query = "SELECT id, name, teacher_id FROM classes"; 
    
        try (Connection connection = DataBase.getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
    
            while (resultSet.next()) { 
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int teacher_id = resultSet.getInt("teacher_id");
    
                ArrayList<Integer> students_id = get_students_id_by_class_id(id);
                Class class_obj = new Class(id, name, students_id, teacher_id);
                classes.add(class_obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return classes;
    }

    public static StudentProfile get_student_profile(int student_id) {
        StudentProfile student_profile = null;
        String query = "SELECT id, user_id, test_results_id FROM student_profiles WHERE user_id = ?"; // Adjust the query based on your database schema
    
        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, student_id);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int user_id = resultSet.getInt("user_id");
                ArrayList<Integer> test_results_id = new ArrayList<>();
                String test_results_id_str = resultSet.getString("test_results_id");
                if (test_results_id_str != null && !test_results_id_str.isEmpty()) {
                    String[] ids = test_results_id_str.split(",");
                    for (String test_id : ids) {
                        test_results_id.add(Integer.parseInt(test_id.trim()));
                    }
                }
    
                student_profile = new StudentProfile(id, user_id, test_results_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return student_profile;
    }

    public static TeacherProfile get_teacher_profile(int teacher_id) {
        TeacherProfile teacher_profile = null;
        String query = "SELECT id, user_id, topics_id FROM teacher_profiles WHERE user_id = ?"; // Предполагается, что topics_id хранится в виде строки, например, "1,2,3"
    
        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, teacher_id);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int user_id = resultSet.getInt("user_id");
                ArrayList<Integer> topics_id = new ArrayList<>();
                String topics_id_str = resultSet.getString("topics_id");
                if (topics_id_str != null && !topics_id_str.isEmpty()) {
                    String[] ids = topics_id_str.split(",");
                    for (String topic_id : ids) {
                        topics_id.add(Integer.parseInt(topic_id.trim()));
                    }
                }
    
                teacher_profile = new TeacherProfile(id, user_id, topics_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return teacher_profile;
    }

    public static AdminProfile get_admin_profile(int admin_id) {
        AdminProfile admin_profile = null;
        String query = "SELECT id, user_id FROM admin_profiles WHERE user_id = ?"; // Предполагается, что admin_id соответствует user_id
    
        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, admin_id);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int user_id = resultSet.getInt("user_id");
    
                admin_profile = new AdminProfile(id, user_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return admin_profile;
    }

    public static void change_user_data(int id, String first_name, String patronymic, String last_name, String password_hash, int role_id) {
        String query = "UPDATE users SET first_name = ?, patronymic = ?, last_name = ?, password_hash = ?, role_id = ? WHERE id = ?";
    
        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, patronymic);
            preparedStatement.setString(3, last_name);
            preparedStatement.setString(4, password_hash);
            preparedStatement.setInt(5, role_id);
            preparedStatement.setInt(6, id);
    
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Данные пользователя успешно обновлены");
            } else {
                System.out.println("Пользователь с id = "+ id +" не найден");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void change_task_data(int id, String question, String answer) {
        String query = "UPDATE tasks SET question = ?, answer = ? WHERE id = ?";
    
        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, question);
            preparedStatement.setString(2, answer);
            preparedStatement.setInt(3, id);
    
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Данные задачи успешно обновлены");
            } else {
                System.out.println("Задача с id="+ id +" не найдена");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void change_test_data(int id, int topic_id, ArrayList<Integer> tasks_id, int teacher_id) {
        String query = "UPDATE tests SET topic_id = ?, teacher_id = ? WHERE id = ?";
    
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, topic_id); // Устанавливаем новое значение topic_id
            preparedStatement.setInt(2, teacher_id); // Устанавливаем новое значение teacher_id
            preparedStatement.setInt(3, id); // Устанавливаем id теста, который нужно изменить
    
            int rowsAffected = preparedStatement.executeUpdate(); // Выполняем обновление
    
            if (rowsAffected > 0) {
                // Если обновление прошло успешно, обновляем задачи, связанные с тестом
                update_test_tasks(id, tasks_id);
            } else {
                System.out.println("Тест с id " + id + " не найден.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void update_test_tasks(int test_id, ArrayList<Integer> tasks_id) {
        String deleteQuery = "DELETE FROM test_tasks WHERE test_id = ?";
        try (Connection connection = DataBase.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            
            deleteStatement.setInt(1, test_id);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        String insertQuery = "INSERT INTO test_tasks (test_id, task_id) VALUES (?, ?)";
        try (Connection connection = DataBase.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            
            for (Integer task_id : tasks_id) {
                insertStatement.setInt(1, test_id);
                insertStatement.setInt(2, task_id);
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void change_student_profile_data(int id, int user_id, ArrayList<Integer> test_results_id) {
        String query = "UPDATE student_profiles SET user_id = ? WHERE id = ?";
    
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, user_id); // Устанавливаем новое значение user_id
            preparedStatement.setInt(2, id); // Устанавливаем id профиля студента, который нужно изменить
    
            int rowsAffected = preparedStatement.executeUpdate(); // Выполняем обновление
    
            if (rowsAffected > 0) {
                // Если обновление прошло успешно, обновляем результаты тестов, связанные с профилем студента
                update_student_test_results(id, test_results_id);
            } else {
                System.out.println("Профиль студента с id " + id + " не найден.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void update_student_test_results(int student_profile_id, ArrayList<Integer> test_results_id) {
        // Удаляем старые результаты тестов, если необходимо
        String deleteQuery = "DELETE FROM student_test_results WHERE student_profile_id = ?";
        try (Connection connection = DataBase.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            
            deleteStatement.setInt(1, student_profile_id);
            deleteStatement.executeUpdate(); // Удаляем старые результаты тестов
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        // Добавляем новые результаты тестов
        String insertQuery = "INSERT INTO student_test_results (student_profile_id, test_result_id) VALUES (?, ?)";
        try (Connection connection = DataBase.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            
            for (Integer test_result_id : test_results_id) {
                insertStatement.setInt(1, student_profile_id);
                insertStatement.setInt(2, test_result_id);
                insertStatement.executeUpdate(); // Добавляем новый результат теста
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void change_teacher_profile_data(int id, int user_id, ArrayList<Integer> topics_id) {
        String query = "UPDATE teacher_profiles SET user_id = ? WHERE id = ?";
    
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, user_id); // Устанавливаем новое значение user_id
            preparedStatement.setInt(2, id); // Устанавливаем id профиля преподавателя, который нужно изменить
    
            int rowsAffected = preparedStatement.executeUpdate(); // Выполняем обновление
    
            if (rowsAffected > 0) {
                // Если обновление прошло успешно, обновляем темы, связанные с профилем преподавателя
                update_teacher_topics(id, topics_id);
            } else {
                System.out.println("Профиль преподавателя с id " + id + " не найден.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void update_teacher_topics(int teacher_profile_id, ArrayList<Integer> topics_id) {
        // Удаляем старые темы, если необходимо
        String deleteQuery = "DELETE FROM teacher_topics WHERE teacher_profile_id = ?";
        try (Connection connection = DataBase.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            
            deleteStatement.setInt(1, teacher_profile_id);
            deleteStatement.executeUpdate(); // Удаляем старые темы
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        // Добавляем новые темы
        String insertQuery = "INSERT INTO teacher_topics (teacher_profile_id, topic_id) VALUES (?, ?)";
        try (Connection connection = DataBase.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            
            for (Integer topic_id : topics_id) {
                insertStatement.setInt(1, teacher_profile_id);
                insertStatement.setInt(2, topic_id);
                insertStatement.executeUpdate(); // Добавляем новую тему
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void change_class_data(int id, String name, ArrayList<Integer> students_id, int teacher_id) {
        String query = "UPDATE classes SET name = ?, teacher_id = ? WHERE id = ?";
    
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, name); // Устанавливаем новое значение имени класса
            preparedStatement.setInt(2, teacher_id); // Устанавливаем новое значение teacher_id
            preparedStatement.setInt(3, id); // Устанавливаем id класса, который нужно изменить
    
            int rowsAffected = preparedStatement.executeUpdate(); // Выполняем обновление
    
            if (rowsAffected > 0) {
                // Если обновление прошло успешно, обновляем студентов, связанные с классом
                update_class_students(id, students_id);
            } else {
                System.out.println("Класс с id " + id + " не найден.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void update_class_students(int class_id, ArrayList<Integer> students_id) {
        // Удаляем старых студентов, если необходимо
        String deleteQuery = "DELETE FROM class_students WHERE class_id = ?";
        try (Connection connection = DataBase.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            
            deleteStatement.setInt(1, class_id);
            deleteStatement.executeUpdate(); // Удаляем старых студентов
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        // Добавляем новых студентов
        String insertQuery = "INSERT INTO class_students (class_id, student_id) VALUES (?, ?)";
        try (Connection connection = DataBase.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            
            for (Integer student_id : students_id) {
                insertStatement.setInt(1, class_id);
                insertStatement.setInt(2, student_id);
                insertStatement.executeUpdate(); // Добавляем нового студента
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserStatistic get_user_statistic(int user_id) {
        UserStatistic statistic = null;
        String query = "SELECT AVG(points) AS average_score, " +
                       "MAX(points) AS highest_score, " +
                       "MIN(points) AS lowest_score " +
                       "FROM test_results WHERE user_id = ?";
    
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, user_id); // Устанавливаем значение user_id в запрос
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполняем запрос
    
            if (resultSet.next()) {
                double average_points = resultSet.getDouble("average_score");
                int highest_points = resultSet.getInt("highest_score");
                int lowest_points = resultSet.getInt("lowest_score");
    
                // Создаем объект UserStatistic с полученными данными
                statistic = new UserStatistic(user_id, average_points, highest_points, lowest_points);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return statistic; // Возвращаем объект статистики или null, если данные не найдены
    }

    public TestStatistic get_test_statistic(int test_id) {
        TestStatistic statistic = null;
        String query = "SELECT AVG(points) AS average_score, " +
                       "MAX(points) AS highest_score, " +
                       "MIN(points) AS lowest_score " +
                       "FROM test_results WHERE test_id = ?";
    
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, test_id); // Устанавливаем значение test_id в запрос
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполняем запрос
    
            if (resultSet.next()) {
                double average_test_results = resultSet.getDouble("average_score");
                int highest_test_results = resultSet.getInt("highest_score");
                int lowest_test_results = resultSet.getInt("lowest_score");
    
                // Создаем объект TestStatistic с полученными данными
                statistic = new TestStatistic(test_id, average_test_results, highest_test_results, lowest_test_results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return statistic; // Возвращаем объект статистики или null, если данные не найдены
    }

    public ArrayList<User> get_worst_students(int student_id) {
        ArrayList<User> worst_students = new ArrayList<>();
        String query = "SELECT user_id, AVG(points) AS average_score " +
                       "FROM test_results " +
                       "WHERE user_id != ? " + // Исключаем текущего студента
                       "GROUP BY user_id " +
                       "ORDER BY average_score ASC " + // Сортируем по среднему баллу (возрастание)
                       "LIMIT 5"; // Ограничиваем до 5 учеников
    
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, student_id); // Устанавливаем значение student_id в запрос
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполняем запрос
    
            while (resultSet.next()) {
                int user_id = resultSet.getInt("user_id");
                // Получаем информацию о пользователе по user_id
                User user = DataBase.get_user(user_id); // Предполагается, что у вас есть метод для получения пользователя по ID
                if (user != null) {
                    worst_students.add(user); // Добавляем пользователя в список
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return worst_students; // Возвращаем список худших учеников
    }

    public ArrayList<User> get_best_students(int student_id) {
        ArrayList<User> best_students = new ArrayList<>();
        String query = "SELECT user_id, AVG(points) AS average_score " +
                       "FROM test_results " +
                       "WHERE user_id != ? " + // Исключаем текущего студента
                       "GROUP BY user_id " +
                       "ORDER BY average_score DESC " + // Сортируем по среднему баллу (убывание)
                       "LIMIT 5"; // Ограничиваем до 5 учеников
    
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, student_id); // Устанавливаем значение student_id в запрос
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполняем запрос
    
            while (resultSet.next()) {
                int user_id = resultSet.getInt("user_id");
                // Получаем информацию о пользователе по user_id
                User user = DataBase.get_user(user_id); // Предполагается, что у вас есть метод для получения пользователя по ID
                if (user != null) {
                    best_students.add(user); // Добавляем пользователя в список
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return best_students; // Возвращаем список лучших учеников
    }

    public Test get_most_difficult_test() {
        Test mostDifficultTest = null;
        String query = "SELECT test_id, AVG(points) AS average_score " +
                       "FROM test_results " +
                       "GROUP BY test_id " +
                       "ORDER BY average_score ASC " + // Сортируем по среднему баллу (возрастание)
                       "LIMIT 1"; // Ограничиваем до 1 теста
    
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполняем запрос
    
            if (resultSet.next()) {
                int testId = resultSet.getInt("test_id");
                // Получаем информацию о тесте по testId
                mostDifficultTest = DataBase.get_test(testId); // Предполагается, что у вас есть метод для получения теста по ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return mostDifficultTest; // Возвращаем тест с наименьшим средним баллом или null, если тесты не найдены
    }

    public Test get_easiest_test() {
        Test easiest_test = null;
        String query = "SELECT test_id, AVG(points) AS average_score " +
                       "FROM test_results " +
                       "GROUP BY test_id " +
                       "ORDER BY average_score DESC " + // Сортируем по среднему баллу (убывание)
                       "LIMIT 1"; // Ограничиваем до 1 теста
    
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполняем запрос
    
            if (resultSet.next()) {
                int test_id = resultSet.getInt("test_id");
                // Получаем информацию о тесте по test_id
                easiest_test = DataBase.get_test(test_id); // Предполагается, что у вас есть метод для получения теста по ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return easiest_test; // Возвращаем тест с наивысшим средним баллом или null, если тесты не найдены
    }

} 