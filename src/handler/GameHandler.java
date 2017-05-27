package handler;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import java.util.*;
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
        try{
            JSONObject all = new JSONObject();
            JSONArray gamelist = new JSONArray();

            //Obtain list of games
            Set<String> keys = States.games.keySet();

            for(String key: keys){
                JSONObject o = new JSONObject();
                o.put("owner", key);
                o.put("mapname", States.games.get(key).map);
                gamelist.put(o);
            }
            all.put("games",gamelist);

            String response = all.toString();
            //Send 200 as answer
            this.sendHttpResponse(t,200,response);

        }catch(Exception e){
            System.out.println("Error accessing getting game list.");
            try{
                this.sendHttpResponse(t,400,"");
            }catch(Exception e2){

            }
            return;
        }    
    }

    //Used to join game
    private void postGame(HttpExchange t){
        System.out.println("POST " + t.getRequestURI());
        try{
            String value = this.getBodyToString(t);
            JSONObject o = new JSONObject(value);

            String userName = o.getString("username");
            String accessToken = o.getString("accesstoken");
            String ownerName = o.getString("owner");
            JSONObject position = o.getJSONObject("postion");

            //Check if user logged and access token correct
            if(!States.userLogged(userName)){
                System.err.println("User not logged in");
                this.sendHttpResponse(t,403,"");
                return;
            }
            if(!States.validToken(userName)){
                System.err.println("Token expired");
                this.sendHttpResponse(t,401,"");
                return;
            }

            if(States.joinGame(ownerName,userName,position.toString())){
                this.sendHttpResponse(t,200,"");
            }else this.sendHttpResponse(t,404,"");

        }catch(Exception e){
            System.out.println("Error accessing getting game list.");
            try{
                this.sendHttpResponse(t,400,"");
            }catch(Exception e2){

            }
            return;
        }   

    }
    //Used to create game
    private void putGame(HttpExchange t){
        System.out.println("PUT " + t.getRequestURI());
        try{
            String value = this.getBodyToString(t);
            JSONObject o = new JSONObject(value);

            String userName = o.getString("username");
            String accessToken = o.getString("accesstoken");
            String mapName = o.getString("mapname");

            //Check if user logged and access token correct
            if(!States.userLogged(userName)){
                System.err.println("User not logged in: ");
                this.sendHttpResponse(t,403,"");
                return;
            }
            if(!States.validToken(userName)){
                System.err.println("Token expired");
                this.sendHttpResponse(t,401,"");
                return;
            }

            if(States.createGame(userName,mapName)){
                this.sendHttpResponse(t,200,"{}");
            }else this.sendHttpResponse(t,404,"");

        }catch(Exception e){
            System.out.println("Error accessing getting game list.");
            try{
                this.sendHttpResponse(t,400,"");
            }catch(Exception e2){

            }
            return;
        }
    }
    //Leave game
    private void deleteGame(HttpExchange t){
        System.out.println("DELETE " + t.getRequestURI());
        
        System.out.println("PUT " + t.getRequestURI());
        try{
            String value = this.getBodyToString(t);
            JSONObject o = new JSONObject(value);

            String userName = o.getString("username");
            String accessToken = o.getString("accesstoken");

            //Check if user logged and access token correct
            if(!States.userLogged(userName)){
                System.err.println("User not logged in");
                this.sendHttpResponse(t,403,"");
                return;
            }
            if(!States.validToken(userName)){
                System.err.println("Token expired");
                this.sendHttpResponse(t,401,"");
                return;
            }

            if(States.deleteGame(userName)){
                this.sendHttpResponse(t,200,"");
            }else this.sendHttpResponse(t,404,"");

        }catch(Exception e){
            System.out.println("Error accessing getting game list.");
            try{
                this.sendHttpResponse(t,400,"");
            }catch(Exception e2){

            }
            return;
        }

    }
}

