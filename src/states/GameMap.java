package states;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import java.util.*;
import java.sql.*;
import java.io.*;
import db.*;

public class GameMap{
    public static class MapLine{
        public double slat,slng,flat,flng;
        public MapLine(double slat,double slng,double flat,double flng){
            this.slat = slat;
            this.slng = slng;
            this.flat = flat;
            this.flng = flng;
        }
    }
    public String name;
    public double slat,slng,flat,flng;
    public ArrayList<MapLine> lines;
    public GameMap(String name,double slat,double slng,double flat,double flng){
        this.name = name;
        this.slat = slat;
        this.slng = slng;
        this.flat = flat;
        this.flng = flng;
    }
    
    public void addLine(double slat,double slng,double flat,double flng){
        lines.add(new MapLine(slat,slng,flat,flng));
    }
}