package handler;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import java.util.HashMap;
import java.sql.*;
import java.io.*;
import db.*;
import states.*;

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

        //HashMap<String, String> GETparams = this.getGETparams(t);
    }
    //Login
    private void postUser(HttpExchange t){
        try{
            System.out.println("POST " + t.getRequestURI());

            //Get body
            String value = this.getBodyToString(t);
            JSONObject o = new JSONObject(value);
            //Username
            String userName = o.getString("username");
            //Password
            String userHash = o.getString("userhash");
            //Response
            JSONObject response;

            //Check if valid user
            int userId = Users.checkLoginCorrect(SQLConnection,userName,userHash);
            //If password is not correct, return
            if(userId < 1){
                System.err.println("Invalid user");
                this.sendHttpResponse(t,403,"");
                return;
            }

            //If user is valid, create a session of him
            States.loginUser(userName,userId);

            //Build response
            response = new JSONObject();

            response.put("username",userName);
            response.put("accesstoken",States.getUserAToken(userName));
            response.put("refreshtoken",States.getUserRToken(userName));
            //Send response
            sendHttpResponse(t,303,response.toString());
        }catch(Exception e){
            try{
                this.sendHttpResponse(t,404,"");
            }catch(Exception e2){
				e2.printStackTrace();
            }
            System.err.println("Error on post");
			e.printStackTrace();
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
			System.out.println("Reached");
            this.sendHttpResponse(t,201,value);
			return;
        }catch(Exception e){
            try{
                this.sendHttpResponse(t,404,"");
            }catch(Exception e2){
				e2.printStackTrace();
            }
			e.printStackTrace();
            System.err.println("Error on post");
            return;
        }
    }

    private void deleteUser(HttpExchange t){
        System.out.println("DELETE " + t.getRequestURI());
        try{
            //Get body
            String value = this.getBodyToString(t);
            JSONObject o = new JSONObject(value);
            //Username
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
            States.logoutUser(userName);
            this.sendHttpResponse(t,303,"");
            
        }catch(Exception e){

        }
         

    }

}