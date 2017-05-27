package handler;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import java.util.HashMap;
import java.sql.*;
import java.io.*;
import db.*;
import states.*;

public class GameHandler extends Handler {

    public GameHandler(Connection SQLconn) {
        super(SQLconn);
    }

    @Override
    public void handle(HttpExchange t) { 
        
        // 1. Determine the request command
        String method = t.getRequestMethod();

        switch(method) {
            case "GET": 
                getGame(t);
                break;
            case "POST": 
                postGame(t);
                break;
            case "PUT": 
                putGame(t);
                break;
            case "DELETE":
                deleteGame(t);
                break;
            default:
                System.out.println("Invalid Request Method invoked.");
                return;
        }

    }    
    //Used to get list of ongoing games
    private void getGame(HttpExchange t) {

        System.out.println("GET " + t.getRequestURI());

        HashMap<String, String> GETparams = this.getGETparams(t);
        
    }
    //Used to join game
    private void postGame(HttpExchange t){
        System.out.println("POST " + t.getRequestURI());
    }
    //Used to create game
    private void putGame(HttpExchange t){
        
        
    }
    //Leave game
    private void deleteGame(HttpExchange t){
        
        
    }

}