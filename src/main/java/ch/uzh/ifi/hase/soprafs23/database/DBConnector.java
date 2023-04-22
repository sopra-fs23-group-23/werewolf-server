package ch.uzh.ifi.hase.soprafs23.database;
/*
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnector {

    public static void connectDB(){

        try {

            String connectionURL = "jdbc:mysql://localhost:3306/werewolves";
            String username = "root";
            String password = "8cnNNJ!YVi!F9h";

            Connection connection = DriverManager.getConnection(connectionURL, username, password);

            Statement statement = connection.createStatement();

            // ResultSet resultSet = statement.executeQuery("insert into users (id, username, password)" + " values (2, 'eggmann', 'egg')");

            ResultSet resultSet = statement.executeQuery("select * from users;");

            while (resultSet.next()){
                System.out.println(resultSet.getString("username"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

 */
