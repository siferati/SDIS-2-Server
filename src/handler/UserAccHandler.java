package handler;

import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;

import java.sql.*;

public class UserAccHandler extends Handler {

    public UserAccHandler(Connection SQLconn) {
        super(SQLconn);
    }

    @Override
    public void handle(HttpExchange t) { 
        
        // 1. Determine the request command
        String method = t.getRequestMethod();

        switch(method) {
            case "GET": 
                getUser(t);
                break;
            case "POST": 
                postUser(t);
                break;
            case "PUT": 
                putUser(t);
                break;
            case "DELETE":
                deleteUser(t);
                break;
            default:
                System.out.println("Invalid Request Method invoked.");
                return;
        }

    }    

    private void getUser(HttpExchange t) {

        System.out.println("GET " + t.getRequestURI());

        HashMap<String, String> GETparams = this.getGETparams(t);

        // selects, wtv
    }

    private void postUser(HttpExchange t){
        System.out.println("POST " + t.getRequestURI());
    }

    private void putUser(HttpExchange t){
        System.out.println("PUT " + t.getRequestURI());
    }

    private void deleteUser(HttpExchange t){
        System.out.println("DELETE " + t.getRequestURI());
    }

}