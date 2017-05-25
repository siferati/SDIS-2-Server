package db;

import java.sql.*;
import java.io.*;
public class Maps{
        public static ResultSet getMap(Connection c,String name) throws Exception{
            String query = "SELECT * FROM Map WHERE name = ?";
            PreparedStatement stmt;
            stmt = c.prepareStatement(query);
            stmt.setString(1,name);
            ResultSet rs = stmt.executeQuery();
            return rs;
        }

        public static ResultSet getMapLines(Connection c,int mapid) throws Exception{
            String query = "SELECT * FROM MapLine WHERE map_id = ?";
            PreparedStatement stmt;
            stmt = c.prepareStatement(query);
            stmt.setInt(1,mapid);
            ResultSet rs = stmt.executeQuery();
            return rs;
        }
        //Return true if map was deleted
        public static boolean deleteMap(Connection c,String mapname,int owner) throws Exception{
            String query = "DELETE FROM Map WHERE name = ? AND owner = ?";
            PreparedStatement stmt;
            stmt = c.prepareStatement(query);
            stmt.setString(1,mapname);
            stmt.setInt(2,owner);
            return stmt.executeUpdate() != 0;  
        }
        //Return map id(-1 if failed to insert)
        public static int insertMap(Connection c, String mapname,int owner,double startlat, double startlng, double finishlat, double finishlng) throws Exception{
            String query = "INSERT INTO Map (name,owner,startlat,startlng,finishlat,finishlng) VALUES(?,?,?,?,?,?)";
            PreparedStatement stmt;
            stmt = c.prepareStatement(query);
            stmt.setString(1,mapname);
            stmt.setInt(2,owner);
            stmt.setDouble(3,startlat);
            stmt.setDouble(4,startlng);
            stmt.setDouble(5,finishlat);
            stmt.setDouble(6,finishlng);

            stmt.executeUpdate();
            
            int mapid = -1;

            ResultSet gk = stmt.getGeneratedKeys();
            if(gk.next()){
                mapid = gk.getInt(1);
            }
            return mapid;
        }

        public static void insertLine(Connection c, String draw, int mapid) throws Exception{
            String query = "INSERT INTO MapLine (draw,map_id) VALUES (?,?)";
            PreparedStatement stmt;
            stmt = c.prepareStatement(query);
            stmt.setString(1,draw);
            stmt.setInt(2,mapid);

            stmt.executeUpdate();
        }
        

}