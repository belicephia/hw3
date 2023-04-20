

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ToDB {
    String HOST_NAME = "localhost";
    String PORT = "3306";
    String DATABASE = "database";
    String USERNAME = "root";
    String PASSWORD = "root";


    String jdbcURL = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT, DATABASE);


    public void insertInToDB(String Swipee,String Swiper, Boolean likeornot) throws SQLException {


        Connection dbconn = DriverManager.getConnection(jdbcURL,USERNAME,PASSWORD);


        String sql = "INSERT INTO swipe (swipee_id, swiper_id, LikeorNot) values (?,?,?)";
//

        try(PreparedStatement addstmt = dbconn.prepareStatement(sql)){

            addstmt.setObject(1,Swipee);
            addstmt.setObject(2,Swiper);
            addstmt.setObject(3,likeornot);
//
            addstmt.execute();

        }catch (Exception err){
            err.printStackTrace();
        }
    }

}
