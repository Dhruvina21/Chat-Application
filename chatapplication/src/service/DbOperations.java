package service;

import model.Login;
import model.Register;

import java.sql.*;

import static constants.SQLQuries.INSERT_USERS_SQL;
import static constants.SQLQuries.UPDATE_USER;
import static service.DbConnectionService.dbConnection;

public class DbOperations {


    public static Integer validateUser(Login login) throws SQLException {
        Statement statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery("select userid from public.userdata where username='" + login.getUserName() + "' and userpassword='" + login.getPassword() + "'");
        if (rs.next()) {
            return rs.getInt("userid");
        }
        return null;
    }


    public static Integer addUser(Register register) throws SQLException {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(INSERT_USERS_SQL);
            preparedStatement.setString(1, register.getUserName());
            preparedStatement.setString(2, register.getPassword());
            System.out.println("Executing : " + preparedStatement);
            preparedStatement.executeUpdate();
            return validateUser(new Login(register.getUserName(), register.getPassword()));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public static Boolean updateUser(String username, String userPassword, Integer userId) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(UPDATE_USER);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, userPassword);
            preparedStatement.setInt(3, userId);
            System.out.println("Executing Update : " + preparedStatement);
            preparedStatement.executeUpdate();
            return Boolean.TRUE;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error while updating user :" + userId);
            return Boolean.FALSE;
        }
    }


}
