package com.tis.tis_web_app;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    
    public static boolean add_user(String firstName, String patronymic, String lastName, String password, int role_id) {
        String query = "INSERT INTO users (first_name, patronymic, last_name, password, role_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection(); // Получаем соединение из пула
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, patronymic);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, password);
            preparedStatement.setInt(5, role_id);
            
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<User> get_users(){
        List<User> users = new ArrayList<>();
        String query = "SELECT id, first_name, patronymic, last_name, password, role_id FROM users"; // Предполагается, что role_id хранится в базе данных

        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String first_name = resultSet.getString("first_name");
                String patronymic = resultSet.getString("patronymic");
                String last_name = resultSet.getString("last_name");
                String password = resultSet.getString("password");
                int role_id = resultSet.getInt("role_id");

                Role role = new Role(role_id);
                User user = new User(id, first_name, patronymic, last_name, password, role);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users; 
    }

    public static User get_user(int user_id) {
        User user = null;
        String query = "SELECT id, first_name, patronymic, last_name, password, role_id FROM users WHERE id = ?"; // SQL-запрос для получения пользователя по id
    
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, user_id); // Устанавливаем значение user_id в запрос
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполняем запрос
    
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String first_name = resultSet.getString("first_name");
                String patronymic = resultSet.getString("patronymic");
                String last_name = resultSet.getString("last_name");
                String password = resultSet.getString("password");
                int role_id = resultSet.getInt("role_id"); // Получаем role_id из результата
    
                Role role = DataBase.get_role(role_id);
                user = new User(id, first_name, patronymic, last_name, password, role);
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
        String query = "SELECT id, topic_id, teacher_id FROM tests"; // SQL-запрос для получения id, topic_id и teacher_id тестов
    
        try (Connection connection = DataBase.getConnection(); // Получаем соединение из класса DataBase
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
    
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int topic_id = resultSet.getInt("topic_id");
                String tasks_id_str = resultSet.getString("tasks_id");
                int teacher_id = resultSet.getInt("teacher_id");
                
                ArrayList<Task> tasks = new ArrayList<>();
    
                if (tasks_id_str != null && !tasks_id_str.isEmpty()) {
                    tasks_id_str = tasks_id_str.replaceAll("[{}]", "").trim();
                    String[] tasks_id = tasks_id_str.split(","); // Предполагаем, что идентификаторы разделены запятыми
                    for (String task_id : tasks_id) {
                        Task task = DataBase.get_task(Integer.parseInt(task_id.trim())); // Получаем объект User по userId
                        if (task != null) {
                            tasks.add(task);
                        }
                    }
                }

                Test test = new Test(id, topic_id, tasks, teacher_id);
                tests.add(test);
            }
        } catch (Exception e) {
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
                ArrayList<Task> tasks = new ArrayList<>();
                for (int task_id : tasks_id) {
                    Task task = DataBase.get_task(task_id);
                    if (task != null) {
                        tasks.add(task);
                        }
                    }
                test = new Test(id, topic_id, tasks, teacher_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return test;
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

} 