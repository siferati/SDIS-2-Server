package handler;

import java.io.OutputStream;
import java.io.IOException;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.sql.Connection;

public class Handler implements HttpHandler {

    protected Connection SQLConnection;
    protected String AND_DELIMITER = "&";
    protected String EQUAL_DELIMITER = "=";

    public Handler (Connection sqlConn) {
        this.SQLConnection = sqlConn;
    }

    @Override
    public void handle(HttpExchange t) { 
        System.out.println("Called base handler. Exiting...");
        return;
    }

    public void sendHttpResponse(HttpExchange t, int code, String response) throws IOException {

        // Send Code + Response
        t.sendResponseHeaders(code, response.length());
        
        // Write it
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();

    } 

}