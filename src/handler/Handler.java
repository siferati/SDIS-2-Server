package handler;

import java.io.OutputStream;
import java.io.IOException;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class Handler implements HttpHandler {

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