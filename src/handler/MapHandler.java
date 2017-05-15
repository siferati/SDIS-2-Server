package handler;

import com.sun.net.httpserver.HttpExchange;
import org.json.simple.JSONObject;
import java.util.HashMap;
import java.sql.*;
import java.io.*;

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

        HashMap<String, String> GETparams = this.getGETparams(t);

        if(GETparams.get("id") == null){
            return;
        }

        String query = "SELECT * FROM Map WHERE id = ?";

        PreparedStatement stmt;
        try{
            stmt = SQLConnection.prepareStatement(query);
            stmt.setString(1, GETparams.get("id"));
        }catch(Exception e){
            System.out.println("Error creating statement.");
            return;
        }

        ResultSet rs;
        try{
            rs = stmt.executeQuery();
        }catch(Exception e){
            System.out.println("Error accessing database.");
            return;
        }

        try{
            int total_rows = rs.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for(int i = 0 ; i < total_rows ; i++){
                obj.put(rs.getMetaData().getColumnLabel(i+1).toLowerCase(),rs.getObject(i+1));
            }
            
            this.sendHttpResponse(t,200,obj.toString());

        }catch(Exception e){
            System.err.println("Exception caught trying to send JSONobject");
            return;
        }
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