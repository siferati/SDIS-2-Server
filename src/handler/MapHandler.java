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
            if(!rs.next()){
                System.out.println("Map doesnt exist");
                this.sendHttpResponse(t,404,"");
                return;
            }
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
            System.err.println("Couldn't send http response.'");
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
        try{
            String value = this.getBodyToString(t);
            System.out.println(value);
            JSONObject o = new JSONObject(value);
            String mapName = o.getString("mapname");
            String userName = o.getString("username");
            String userHash = o.getString("userhash");
            //Statement to check if password is correct
            String checkPassword = "SELECT id,pass_hash FROM UserAcc WHERE username = ?";
            String deleteMap = "DELETE FROM Map WHERE name = ? AND owner = ?";
            //Retrieve user id and password
            PreparedStatement stmt = SQLConnection.prepareStatement(checkPassword);
            stmt.setString(1,userName);
            ResultSet rs;
            rs = stmt.executeQuery();
            rs.next();
            //If password is not correct, return
            System.out.println("got here");
            if(!rs.getString("pass_hash").equals(userHash)){
                return;
            }
            System.out.println("got here");
            //Delete map with name and user id
            PreparedStatement stmt2 = SQLConnection.prepareStatement(deleteMap);
            stmt2.setString(1,mapName);
            stmt2.setInt(2,rs.getInt("id"));
            stmt2.executeUpdate();

            this.sendHttpResponse(t,204,"");
        }catch(Exception e){
            System.err.println("Error deleting");
            return;
        }
        
    }

}