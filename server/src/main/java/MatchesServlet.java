import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

@WebServlet(name = "MatchesServlet", value = "/matches/*")
public class MatchesServlet extends HttpServlet {

    String HOST_NAME = "localhost";
    String PORT = "3306";
    String DATABASE = "database";
    String USERNAME = "root";
    String PASSWORD = "root";
    ResultSet rs = null;
    String jdbcURL = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT, DATABASE);

    List<String> potentialMatches = new ArrayList<>();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String path = request.getPathInfo().toString();
        String[] pathParts = path.split("/");
        String userId = pathParts[1];

        List<String> potentialMatches = new ArrayList<>();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        System.out.println("Loading driver...");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }
//
        try{
            // Get a database connection
            Connection dbconn = DriverManager.getConnection(jdbcURL,USERNAME,PASSWORD);
//            String match_sql = "SELECT swiper_id FROM SWIPE WHERE swipee_id = " + userId;

//            //Execute a query
//            PreparedStatement pstmt = dbconn.prepareStatement(match_sql);
//            rs = pstmt.executeQuery();
//            while (rs.next()){
//                System.out.println(rs.toString());
//            }
            String matchSql = "SELECT swiper_id FROM swipe WHERE swipee_id = ?";
            PreparedStatement pstmt = dbconn.prepareStatement(matchSql);
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            int counter = 100;


            // Process the results and add the potential matches to the list
            while (rs.next() && counter > 0) {
                String swiperId = rs.getString("swiper_id");
                potentialMatches.add(swiperId);
                counter --;
            }

            // Convert the list of potential matches to a JSON string and write it to the response


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        }


//


    }

