package states;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import java.util.*;
import java.sql.*;
import java.io.*;
import db.*;

public class States{
	public static Hashtable<String,LoginState> loggedUsers;

	public static void loginUser(String username,String userhash){
		loggedUsers.put(username,new LoginState(username,userhash));
	}

	public static void logoutUser(String username){
		loggedUsers.remove(username);
	}
}