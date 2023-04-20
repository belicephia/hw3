import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.ietf.jgss.MessageProp;
import java.nio.ByteBuffer;
import java.util.Arrays;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@WebServlet(name = "SwipeServlet", value = "/swipe/*")
public class SwipeServlet extends HttpServlet {

    public static String QUEUE_NAME = "queue";
    public static RMQChannelPool rmqChannelPool;

    @Override
    public void init() throws ServletException{
        super.init();
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPassword("guest");
        connectionFactory.setUsername("guest");
        connectionFactory.setPort(5672);
        Connection connection;
        try{
            connection = connectionFactory.newConnection();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        RMQChannelFactory channelFactory = new RMQChannelFactory(connection);
        rmqChannelPool = new RMQChannelPool(100, channelFactory);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.getWriter().write("Hello world");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String url = req.getPathInfo();

        if (!isUrlValid(url,res) ){
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = req.getReader().readLine()) != null) {
            sb.append(s);
        }
        SwipeDetails swipeDetails = gson.fromJson(sb.toString(), SwipeDetails.class);
        if (url.equalsIgnoreCase("/left")){
            swipeDetails.setLikeornot(false);
        }else if(url.equalsIgnoreCase("/right")){
            swipeDetails.setLikeornot(true);
        }


        Channel borrowedChannel = rmqChannelPool.borrowObject();
        borrowedChannel.queueDeclare(QUEUE_NAME, false, false ,false ,null);
        borrowedChannel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
                gson.toJson(swipeDetails).getBytes());
        System.out.format("[x] Sent %s\n", gson.toJson(swipeDetails));
        try {
            rmqChannelPool.returnObject(borrowedChannel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        res.getWriter().write(gson.toJson(swipeDetails));
        res.flushBuffer();
    }

    private boolean isUrlValid(String url,HttpServletResponse res) throws IOException {

//        // TODO: validate the request url path according to the API spec
//        // urlPath  = "/1/seasons/2019/day/1/skier/123"
//        // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
        String[] urlParts = url.split("/");
        int size = urlParts.length;

        if (size != 2) {
            res.getWriter().write("length not right"+ size);
            return false;
        } else {
            if (!urlParts[0].equals("")){
                res.getWriter().write(urlParts[0]);
                return false;
            }else{
                if (!urlParts[1].equals("left") && !urlParts[1].equals("right")){
                    res.getWriter().write("user did not pick");
                    return false;
                }
            }

            return true;
        }
    }
}
