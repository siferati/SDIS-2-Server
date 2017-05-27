package states;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import java.util.*;
import java.sql.*;
import java.io.*;
import db.*;

public class Token{
    String value;
    long start;
    long duration;

    public Token(int durationInSec){
        generateToken();
        start = System.currentTimeMillis();
        duration = durationInSec*1000;
    }

    public boolean isExpired(){
		System.out.println("Token: " + (start + duration) + " ; " + System.currentTimeMillis());
        return (start + duration) < System.currentTimeMillis();
    }
    public void generateToken(){
		char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 16; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		String output = sb.toString();
		value = output;
	}

    public String toString(){
        return value;
    }
}