import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "StatsServlet", value = "/stats/*")
public class StatsServlet extends HttpServlet {
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
            String dislikeCount = "select COUNT(*) FROM swipe WHERE LikeorNot = 0 AND swipee_id = ? ";
            String likeCount = "select COUNT(*) FROM swipe WHERE swipee_id = ? AND LikeorNot = 1";

            PreparedStatement pstmt_dislike = dbconn.prepareStatement(dislikeCount);
            pstmt_dislike.setString(1, userId);
            PreparedStatement pstmt_like = dbconn.prepareStatement(likeCount);
            pstmt_like.setString(1, userId);


            try {
                // ...
                ResultSet rs_dislike = pstmt_dislike.executeQuery();
                if (rs_dislike.next()) {
                    int dislikeInt = rs_dislike.getInt(1);
                    ResultSet re_like = pstmt_like.executeQuery();
                    if (re_like.next()){
                        int likeInt = re_like.getInt(1);
                        System.out.println(likeInt + "like, dislike" + dislikeInt);
                    }
                    // do something with the count value

                }
            } catch (SQLException e) {
                // handle the exception
                e.printStackTrace();
            }


//            PreparedStatement pstmt_like = dbconn.prepareStatement(likeCount);
//            pstmt_like.setString(1, userId);
//            ResultSet rs_like = pstmt_like.executeQuery();
//
//            System.out.println(rs_dislike.toString()+ rs_like.toString());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


//


}





