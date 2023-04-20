import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.*;
import org.ietf.jgss.MessageProp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
public class ConsumerToDB {

    protected static final String QUEUE_NAME = "queue";
    protected static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
//    private static final Integer Threads = 10;



    public static void main(String[] args) throws IOException, TimeoutException, SQLException {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection;
        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }


        Channel channel = connection.createChannel();

        ToDB this_conn = new ToDB();
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                Gson g = new Gson();
                SwipeDetails this_info = g.fromJson(new String(body), SwipeDetails.class);

                    try {
                        this_conn.insertInToDB(this_info.getSwipee(),this_info.getSwiper(),this_info.isLikeornot());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }


                // connect to the DB
            }

        };
        try {
            channel.basicConsume("queue", true, consumer);
//            consumer.handleDelivery();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    }








