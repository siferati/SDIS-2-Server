package handler;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import java.util.HashMap;
import java.sql.*;
import java.io.*;
import db.*;

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
    //Login
    private void postUser(HttpExchange t){
        try{
            System.out.println("POST " + t.getRequestURI());

            String value = this.getBodyToString(t);
            JSONObject o = new JSONObject(value);

            String userName = o.getString("username");
            String userHash = o.getString("userhash");

            //Check if valid user
            int userId = Users.checkLoginCorrect(SQLConnection,userName,userHash);
            //If password is not correct, return
            if(userId < 1){
                System.err.println("Invalid user");
                this.sendHttpResponse(t,403,"");
                return;
            }

            sendHttpResponse(t,303,"");
        }catch(Exception e){
            try{
                this.sendHttpResponse(t,404,"");
            }catch(Exception e2){

            }
            System.err.println("Error on post");
            return;
        }
    }

    private void putUser(HttpExchange t){
        try{
            System.out.println("PUT " + t.getRequestURI());

            String value = this.getBodyToString(t);
            JSONObject o = new JSONObject(value);

            String userName = o.getString("username");
            String userHash = o.getString("userhash");

            if(!Users.insertUser(SQLConnection,userName,userHash)){
                System.err.println("User already exists");
                this.sendHttpResponse(t,409,"");
                return;
            }
            this.sendHttpResponse(t,201,"");
        }catch(Exception e){
            try{
                this.sendHttpResponse(t,404,"");
            }catch(Exception e2){

            }
            System.err.println("Error on post");
            return;
        }
    }

    private void deleteUser(HttpExchange t){
        System.out.println("DELETE " + t.getRequestURI());
    }

}