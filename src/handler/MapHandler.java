package handler;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import java.util.HashMap;
import java.sql.*;
import java.io.*;
import db.*;
import states.*;
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
        //Check if map name was sent
        if(GETparams.get("name") == null){
            return;
        }
        //Map
        ResultSet rs;
        //Map lines
        ResultSet rs2;
        //Username from user id
        ResultSet rs3;
        //Response json object
        JSONObject all;
        //Response string
        String response;
        try{
            //Get map
            rs = Maps.getMap(SQLConnection,GETparams.get("name"));
            if(!rs.next()){
                System.out.println("Map doesn't exist");
                this.sendHttpResponse(t,404,"");
                return;
            }
            //Get map lines
            rs2 = Maps.getMapLines(SQLConnection,rs.getInt("id"));
            //Get name of owner of map
            rs3 = Users.getUsernameFromId(SQLConnection,rs.getInt("owner"));
            if(!rs3.next()){
                System.out.println("User doesn't exist");
                this.sendHttpResponse(t,404,"");
                return;
            }
            //Making json
            all = new JSONObject();
            JSONObject obj = new JSONObject();
            JSONArray lines = new JSONArray();
            JSONObject singline;
            
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
            response = all.toString();
            //Send 200 as answer
            this.sendHttpResponse(t,200,response);  
        }catch(Exception e){
            System.out.println("Error accessing database.");
            try{
                this.sendHttpResponse(t,409,"");
            }catch(Exception e2){

            }
            return;
        }
    }

    private void postMap(HttpExchange t){
        System.out.println("POST " + t.getRequestURI());
    }

    private void putMap(HttpExchange t){
        try{
            System.out.println("PUT " + t.getRequestURI());
            //Get body
            String value = this.getBodyToString(t);
            JSONObject o = new JSONObject(value);
            
            JSONObject mapinfo = o.getJSONObject("map");

            String userName = o.getString("username");
            String accessToken = o.getString("accesstoken");
            String mapname = mapinfo.getString("name");
            double startlat = mapinfo.getDouble("startlat");
            double startlng = mapinfo.getDouble("startlng");
            double finishlat = mapinfo.getDouble("finishlat");
            double finishlng = mapinfo.getDouble("finishlng");

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
            int userId = Users.getUserId(SQLConnection,userName);
            //Check if map exists
            if(Maps.getMap(SQLConnection,mapname).next()){
                Maps.deleteMap(SQLConnection,mapname,userId);
            }
            //Begin transaction
            //SQLConnection.setAutoCommit(false);
            //Create map
            int mapId = Maps.insertMap(SQLConnection,mapname,userId,startlat,startlng,finishlat,finishlng);
            //Check if insert wasn't successful'
            if(mapId == -1){
				System.err.println("Insert wasn't successful");
                return;
            }
            //Create lines
            JSONArray lines = o.getJSONArray("lines");
            for(int i = 0;i < lines.length();i++){
                JSONObject line = (JSONObject)lines.get(i);
                Maps.insertLine(SQLConnection,line.getString("draw"),mapId);
            }
            //Send transaction
            //SQLConnection.commit();
            //Send created http response
            this.sendHttpResponse(t,201,Integer.valueOf(mapId).toString());
        }catch(Exception e){
            try{
                this.sendHttpResponse(t,409,"");
            }catch(Exception e2){

            }
            System.err.println("Error putting");
            return;
        }
        //Turn on auto commit again, after transaction concluded or failed
        try{
            SQLConnection.setAutoCommit(true);
        }catch(Exception e){

        }
        
    }

    private void deleteMap(HttpExchange t){
        try{
            System.out.println("DELETE " + t.getRequestURI());
            String value = this.getBodyToString(t);
            JSONObject o = new JSONObject(value);
            String mapName = o.getString("mapname");
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
            int userId = States.getUserId(userName);
            //Delete map with name and user id
            if(!Maps.deleteMap(SQLConnection,mapName,userId)){
                System.out.println("Map doesn't exist");
                this.sendHttpResponse(t,404,"");
                return;
            }
            //Send no content http response
            this.sendHttpResponse(t,204,"");
        }catch(Exception e){
            System.err.println("Error deleting");
            try{
                this.sendHttpResponse(t,404,"");
            }catch(Exception e2){

            }
            return;
        }
        
    }

}