package yuanye.mariadb;

import java.sql.*;

/**
 * Created by Administrator on 2014/8/6.
 */
public class BasicTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.2.11:3306/clustertest","test","123456");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM mycluster;");
        while (resultSet.next()){
            System.out.println(resultSet.getString("name"));
        }
    }
}
