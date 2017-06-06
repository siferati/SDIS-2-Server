package db;

import java.sql.*;
import java.io.*;

public class Users{

    public static int checkLoginCorrect(Connection c,String user,String hash) throws Exception{
        String query = "SELECT id,pass_hash FROM UserAcc WHERE username = ?";
        PreparedStatement stmt;
        stmt = c.prepareStatement(query);
        stmt.setString(1,user);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next()){
            return -1;
        }
        if(!rs.getString("pass_hash").equals(hash)){
            return -1;
        }
        return rs.getInt("id");
    }

    public static int getUserId(Connection c,String username) throws Exception{
        String query = "SELECT id FROM UserAcc WHERE username = ?";
        PreparedStatement stmt;
        stmt = c.prepareStatement(query);
        stmt.setString(1,username);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt("id");
    }

    public static boolean insertUser(Connection c,String user,String hash) throws Exception{
        String query1 = "SELECT username FROM UserAcc WHERE username = ?";
        PreparedStatement stmt1;
        stmt1 = c.prepareStatement(query1);
        stmt1.setString(1,user);
        ResultSet rs = stmt1.executeQuery();
        if(rs.next()){
            return false;
        }
        String query = "INSERT INTO UserAcc(username,pass_hash) VALUES (?,?)";
        PreparedStatement stmt;
        stmt = c.prepareStatement(query);
        stmt.setString(1,user);
        stmt.setString(2,hash);
        
        return stmt.executeUpdate() != 0;
    }


    public static ResultSet getUsernameFromId(Connection c,int id) throws Exception{
        String query = "SELECT username FROM UserAcc WHERE id = ?";
        PreparedStatement stmt;
        stmt = c.prepareStatement(query);
        stmt.setInt(1,id);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }
}