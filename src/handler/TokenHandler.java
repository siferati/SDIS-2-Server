package handler;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import java.util.HashMap;
import java.sql.*;
import java.io.*;
import db.*;
import states.*;

public class TokenHandler extends Handler {
    public TokenHandler(Connection SQLconn) {
        super(SQLconn);
    }

    @Override
    public void handle(HttpExchange t) { 
        
        // 1. Determine the request command
        String method = t.getRequestMethod();

        switch(method) {
            case "POST": 
                postToken(t);
                break;
            default:
                System.out.println("Invalid Request Method invoked.");
                return;
        }
    }

    private void postToken(HttpExchange t){
        try{
           System.out.println("POST " + t.getRequestURI());

            //Get body
            String value = this.getBodyToString(t);
            JSONObject o = new JSONObject(value);
            //Username
            String userName = o.getString("username");
            //Refresh token
            String refreshToken = o.getString("refreshtoken");

            //Check if user logged and refresh token correct
            if(!States.userLogged(userName)){
                System.err.println("User not logged in");
                this.sendHttpResponse(t,403,"");
                return;
            }
            if(!States.validRefreshToken(userName,refreshToken)){
                System.err.println("Invalid refresh token");
                this.sendHttpResponse(t,401,"");
                return;
            }
            //Update tokens
            States.refreshTokens(userName);

            //Response
            JSONObject response;
            //Build response
            response = new JSONObject();

            response.put("username",userName);
            response.put("accesstoken",States.getUserAToken(userName));
            response.put("refreshtoken",States.getUserRToken(userName));
            //Send response
            sendHttpResponse(t,303,response.toString());
        }catch(Exception e){
            
        }
    }
}