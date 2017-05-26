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
		if(loggedUsers.get(username) != null){
			loggedUsers.remove(username);
		}
		loggedUsers.put(username,new LoginState(username,userhash));
	}

	public static LoginState getUser(String username){
		return loggedUsers.get(username);
	}

	public static boolean userLogged(String username){
		return loggedUsers.get(username) != null;
	}

	public static boolean validToken(String username){
		LoginState st = loggedUsers.get(username);
		if(st != null){
			return !st.accessTokenExpired();
		}else return false;
	}

	public static void logoutUser(String username){
		if(loggedUsers.get(username) != null)
			loggedUsers.remove(username);
	}
}