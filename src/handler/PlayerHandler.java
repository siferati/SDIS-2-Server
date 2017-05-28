package handler;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import java.util.*;
import java.sql.*;
import java.io.*;
import db.*;
import states.*;

public class PlayerHandler extends Handler {

    public PlayerHandler(Connection SQLconn) {
        super(SQLconn);
    }

    @Override
    public void handle(HttpExchange t) { 
        
        // 1. Determine the request command
        String method = t.getRequestMethod();

        switch(method) {
            case "GET": 
                getPlayer(t);
                break;
            case "POST": 
                postPlayer(t);
                break;
            case "PUT": 
                putPlayer(t);
                break;
            case "DELETE":
                deletePlayer(t);
                break;
            default:
                System.out.println("Invalid Request Method invoked.");
                return;
        }

    }    
    //Get position of all players
    private void getPlayer(HttpExchange t) {

        System.out.println("GET " + t.getRequestURI());
        try{
            HashMap<String, String> GETparams = this.getGETparams(t);
            //Check if map name was sent
            if(GETparams.get("owner") == null){
                this.sendHttpResponse(t,400,"");
                return;
            }
            String owner = GETparams.get("owner");

            JSONObject all = new JSONObject();
            JSONArray playerlist = new JSONArray();

            //Obtain list of players
            Set<String> keys = States.games.get(owner).players.keySet();

            for(String key: keys){
                JSONObject o = new JSONObject();
                o.put("username", key);
                o.put("position", new JSONObject(States.games.get(owner).players.get(key).position));
                playerlist.put(o);
            }
            all.put("players",playerlist);

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

    //Send player position
    private void postPlayer(HttpExchange t){
        System.out.println("POST " + t.getRequestURI());
        try{
            String value = this.getBodyToString(t);
            JSONObject o = new JSONObject(value);

            String userName = o.getString("username");
            String accessToken = o.getString("accesstoken");
            String ownerName = o.getString("owner");
            JSONObject position = o.getJSONObject("position");

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

            if(States.changePosition(ownerName,userName, position.toString())){
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
    //Unused
    private void putPlayer(HttpExchange t){
        System.out.println("PUT " + t.getRequestURI());
    }
    //Unused
    private void deletePlayer(HttpExchange t){
        System.out.println("DELETE " + t.getRequestURI());
        
        try{
            String value = this.getBodyToString(t);
            JSONObject o = new JSONObject(value);

            String userName = o.getString("username");
            String accessToken = o.getString("accesstoken");
            String owner = o.getString("owner");

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