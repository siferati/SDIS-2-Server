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
    public GameMap map;

    public GameState(GameMap map){
        players = new Hashtable<String,Player>();
        this.map = map;
    }

    public void addPlayer(String username,double lat, double lng){
        players.put(username,new Player(lat,lng));
    }

    public void changePosition(String username, double lat, double lng){
        if(players.get(username) != null){
            players.get(username).setCoords(lat,lng);
        }
    }

    public void removePlayer(String username){
        if(players.get(username) != null){
            players.remove(username);
        }
    }
}