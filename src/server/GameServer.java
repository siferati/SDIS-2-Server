package server;

import handler.MapHandler;
import handler.UserAccHandler;

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
            String path = "jdbc:sqlite:../db/maps.db";
            Class.forName("org.sqlite.JDBC");
            sqlConn = DriverManager.getConnection(path);
            if(sqlConn == null){
                System.err.println("Couldn't open database successfully!");
                return;
            }
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
            httpServer.createContext("/users", new UserAccHandler(sqlConn));
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