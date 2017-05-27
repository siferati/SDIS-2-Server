package states;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import java.util.*;
import java.sql.*;
import java.io.*;
import db.*;

public class Player{
    public String position;
    //Create player
    public Player(String position){
        this.position = position;
    }
    //Update player coordinates
    public void setCoords(String position){
        this.position = position;
    }
}