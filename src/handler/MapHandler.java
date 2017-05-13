package handler;

import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;

import java.sql.*;

public class MapHandler extends Handler {

    public MapHandler(Connection SQLconn) {
        super(SQLconn);
    }

    @Override
    public void handle(HttpExchange t) { 
        
        // 1. Determine the request command
        String method = t.getRequestMethod();

        switch(method) {
            case "GET": 
                getMap(t);
                break;
            case "POST": 
                postMap(t);
                break;
            case "PUT": 
                putMap(t);
                break;
            case "DELETE":
                deleteMap(t);
                break;
            default:
                System.out.println("Invalid Request Method invoked.");
                return;
        }

    }    

    private void getMap(HttpExchange t) {

        System.out.println("GET " + t.getRequestURI());

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

        // selects, wtv
    }

    private void postMap(HttpExchange t){
        System.out.println("POST " + t.getRequestURI());
    }

    private void putMap(HttpExchange t){
        System.out.println("PUT " + t.getRequestURI());
    }

    private void deleteMap(HttpExchange t){
        System.out.println("DELETE " + t.getRequestURI());
    }

}