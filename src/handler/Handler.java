package handler;

import java.io.*;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;

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
        if(response.length() != 0){
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

    } 

    protected HashMap<String, String> getGETparams(HttpExchange t){
        // Get Request Query
        String query = t.getRequestURI().getQuery();

        HashMap<String, String> queryPairs = new HashMap<String, String>();

        if (query != null){
            String[] params = query.split(this.AND_DELIMITER);

            for (String param:params){
                String[] paramPair = param.split(this.EQUAL_DELIMITER);
                queryPairs.put(paramPair[0], paramPair[1]);
            }
        }
        return queryPairs;
    }

    protected String getBodyToString(HttpExchange t){
        String value = null;
        try{
            InputStream in = t.getRequestBody();
            BufferedInputStream bin = new BufferedInputStream(in);

            byte[] content = new byte[2048];
            int bytesread;
            value = "";
            while((bytesread = in.read(content)) != -1){
                value = new String(content,0,bytesread);
            }
            
        }catch(Exception e){
            System.err.println("Error reading body");
        }
        return value;
    }

}