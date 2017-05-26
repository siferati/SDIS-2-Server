package states;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import java.util.*;
import java.sql.*;
import java.io.*;
import db.*;

public class LoginState{
	public String username;
	public String userhash;
	public String accessToken;

	public LoginState(String username,String userhash){
		//Assign username
		this.username = username;
		//Assign password
		this.userhash = userhash;
		//Generate random acess token
		this.accessToken = LoginState.generateToken();
	}

	public static String generateToken(){
		char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 16; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		String output = sb.toString();
		return output;
	}
}