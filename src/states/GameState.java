package states;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import java.util.*;
import java.sql.*;
import java.io.*;
import db.*;

public class GameState{
    //All players in the game(key = username)
    public Hashtable<String,Player> players;
    public String map;

    public GameState(String map){
        players = new Hashtable<String,Player>();
        this.map = map;
    }

    public void addPlayer(String username,String position){
        players.put(username,new Player(position));
    }

    public void changePosition(String username, String position){
        if(players.get(username) != null){
            players.get(username).setCoords(position);
        }
    }

    public void removePlayer(String username){
        if(players.get(username) != null){
            players.remove(username);
        }
    }
}