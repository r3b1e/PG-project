import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBConnect {
    static Connection connection;
    static Statement statement;

    DBConnect() {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("database.properties"));

            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String password = prop.getProperty("db.password");

            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new DBConnect();
    }
}
