package pt.isec.pd.server.rest.dataacess;

import pt.isec.pd.server.rest.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersDataAccess {
    private Connection connection;

    public UsersDataAccess(Connection connection) {
        this.connection = connection;
    }

    public boolean createUser(User user) {
        String insertUser = "INSERT INTO Users (email, name, password, id_number, nif) VALUES (?, ?, ?, ?, ?)";

        if (isEmailUnique(user.getEmail())) {
            int id = getIdNumber();
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertUser)){
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.setString(2, user.getName());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.setInt(4, id);
                preparedStatement.setInt(5, user.getNif());

                preparedStatement.executeUpdate();
                return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public boolean updateUser(User currentInfo, User updatedInfo) {
        String updateSQL = "UPDATE Users SET email = ?, password = ?, name = ?, id_number = ?, nif = ? WHERE id = ?";
        int userId = getUserIdByEmail(currentInfo.getEmail());
        if(currentInfo.getEmail().equals(updatedInfo.getEmail())){
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)){
                preparedStatement.setString(1, updatedInfo.getEmail());
                preparedStatement.setString(2, updatedInfo.getPassword());
                preparedStatement.setString(3, updatedInfo.getName());
                preparedStatement.setInt(4, updatedInfo.getIdNumber());
                preparedStatement.setInt(5, updatedInfo.getNif());
                preparedStatement.setInt(6, userId);

                int updatedRows = preparedStatement.executeUpdate();

                if (updatedRows == 0) {
                    System.out.println("Error while trying to update user!");
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            if (userId != -1 && isEmailUnique(updatedInfo.getEmail())) {
                System.out.println("Entrei na base de dados");
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)){
                    preparedStatement.setString(1, updatedInfo.getEmail());
                    preparedStatement.setString(2, updatedInfo.getPassword());
                    preparedStatement.setString(3, updatedInfo.getName());
                    preparedStatement.setInt(4, updatedInfo.getIdNumber());
                    preparedStatement.setInt(5, updatedInfo.getNif());
                    preparedStatement.setInt(6, userId);

                    int updatedRows = preparedStatement.executeUpdate();

                    if (updatedRows == 0) {
                        System.out.println("Error while trying to update user!");
                        return false;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        return true;
    }

    public User getUserByEmail(String email) {
        String selectSQL = "SELECT * FROM Users WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)){
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    User user = new User(
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("name"),
                            resultSet.getInt("id_number"),
                            resultSet.getInt("nif")
                    );
                    user.setId(resultSet.getInt("id"));

                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public int getUserIdByEmail(String email) {
        String selectSQL = "SELECT id FROM Users WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String selectSQL = "SELECT * FROM Users";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getInt("id_number"),
                        resultSet.getInt("nif")
                );
                user.setId(resultSet.getInt("id"));
                userList.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userList;
    }

    public List<User> getUsersByIds(List<Integer> userIds) {
        List<User> users = new ArrayList<>();
        String selectSQL = "SELECT id, email, password, name, id_number,nif FROM Users WHERE id = ?";

        for (int userId : userIds) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                preparedStatement.setLong(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        User user = new User(
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                resultSet.getString("name"),
                                resultSet.getInt("id_number"),
                                resultSet.getInt("nif")
                        );
                        user.setId(resultSet.getInt("id"));
                        users.add(user);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return users;
    }

    public boolean isEmailUnique(String email) {
        String selectSQL = "SELECT COUNT(*) FROM Users WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean userExists(String email, String password) {
        String selectSQL = "SELECT id FROM Users WHERE email = ? AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getIdNumber() {
        String countSQL = "SELECT COUNT(*) FROM Users";

        try (PreparedStatement preparedStatement = connection.prepareStatement(countSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0; // Retorna 0 se ocorrer um erro ou não houver registros
    }

    public void addUserToEvent(int userId, int eventId) {
    }
}
