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

} 