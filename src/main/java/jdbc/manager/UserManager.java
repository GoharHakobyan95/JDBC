package jdbc.manager;

import jdbc.db.DBConnectionProvider;
import model.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserManager {
    private Connection connection;

    public UserManager() {
        connection = DBConnectionProvider.getInstance().getConnection();
    }

    public void addUser(User user) throws SQLException {
        PreparedStatement prepareStatement = connection.prepareStatement("Insert into user (name,surname,email,password) Values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        prepareStatement.setString(1, user.getName());
        prepareStatement.setString(2, user.getSurname());
        prepareStatement.setString(3, user.getEmail());
        prepareStatement.setString(4, user.getPassword());
        prepareStatement.executeUpdate();

        ResultSet resultSet = prepareStatement.getGeneratedKeys();
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            user.setId(id);
        }
    }

    public List<User> getAllUsers() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
        List<User> users = new LinkedList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setName(resultSet.getString("name"));
            user.setSurname(resultSet.getString("surname"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            users.add(user);
        }
        return users;
    }

    public void deleteUserById(int id) throws SQLException {
        PreparedStatement prepareStatement = connection.prepareStatement("DELETE FROM user WHERE id =?");
        prepareStatement.setInt(1, id);
        prepareStatement.executeUpdate();
    }
}
