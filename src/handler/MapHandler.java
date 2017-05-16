package handler;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
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
        //Get map
        String query = "SELECT * FROM Map WHERE id = ?";
        String query2 = "SELECT * FROM MapLine WHERE map_id = ?";
        String query3 = "SELECT username FROM UserAcc WHERE id = ?";
        PreparedStatement stmt;
        PreparedStatement stmt2;
        PreparedStatement stmt3;
        ResultSet rs;
        ResultSet rs2;
        ResultSet rs3;
        JSONObject all;
        String response;
        try{
            //Get map
            stmt = SQLConnection.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(GETparams.get("id")));
            rs = stmt.executeQuery();
            //Get map lines
            stmt2 = SQLConnection.prepareStatement(query2);
            stmt2.setInt(1, Integer.parseInt(GETparams.get("id")));
            rs2 = stmt2.executeQuery();
            //Get name of owner of map
            stmt3 = SQLConnection.prepareStatement(query3);
            stmt3.setInt(1, rs.getInt("owner"));
            rs3 = stmt3.executeQuery();
            //Making json
            all = new JSONObject();
            JSONObject obj = new JSONObject();
            JSONArray lines = new JSONArray();
            JSONObject singline;
            
            obj.put("rating",rs.getInt("rating"));
            obj.put("finishlat",rs.getFloat("finishlat"));
            obj.put("finishlng",rs.getFloat("finishlng"));
            obj.put("startlat",rs.getFloat("startlng"));
            obj.put("startlng",rs.getFloat("startlng"));
            obj.put("owner",rs3.getString("username"));
            obj.put("name",rs.getString("name"));
            obj.put("id",rs.getString("id"));
            all.put("map",obj);

            while(rs2.next()){
                singline = new JSONObject();
                singline.put("id",rs2.getInt("id"));
                singline.put("draw",rs2.getString("draw"));
                lines.put(singline);
            }
            all.put("lines",lines);     
        }catch(Exception e){
            System.out.println("Error accessing database.");
            return;
        }
        response = all.toString();

        try{
            this.sendHttpResponse(t,200,response);
            

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