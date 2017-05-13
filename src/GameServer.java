import handler.MapHandler;

import java.io.IOException;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.sql.*;

public class GameServer {

    private static HttpServer httpServer;
    private static int port = 8000;
    private static Connection sqlConn;

    public static void main (String[] args){

        // Database try
        try {

            //TODO: change path!
            String path = "";
            //Class.forName("org.sqlite.JDBC");
            //sqlConn = DriverManager.getConnection(path);

        } catch (Exception e ){
            System.err.println("Couldn't open database successfully!");
            return;
        }

        System.out.println("Database opened successfully.");

        // Server try
        try {
            
            // Create Server
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            
            // Context + Handler
            //server.createContext("/users", new UserHandler());
            httpServer.createContext("/maps", new MapHandler(sqlConn));

            // Create Default Executor
            httpServer.setExecutor(null);


        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Start the server
        httpServer.start();
        System.out.println("Started HttpServer on port 8000");

    }
}