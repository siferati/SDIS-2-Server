package states;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import java.util.*;
import java.sql.*;
import java.io.*;
import db.*;

public class Player{
    public double lat;
    public double lng;
    //Create player
    public Player(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }
    //Update player coordinates
    public void setCoords(double lat, double lng){
        lat = lat;
        lng = lng;
    }
}