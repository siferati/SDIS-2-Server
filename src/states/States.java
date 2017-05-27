package states;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import java.util.*;
import java.sql.*;
import java.io.*;
import db.*;

public class States{
	public static Hashtable<String,LoginState> loggedUsers = new Hashtable<String,LoginState>();
	public static Hashtable<String,GameState> games = new Hashtable<String,GameState>();
	
	//Game methods
	public static void createGame(String owner,GameMap map){
		if(games.get(owner) != null){
			games.remove(owner);
		}else games.put(owner, new GameState(map));
	}

	public static void joinGame(String owner,String username, double lat, double lng){
		if(games.get(owner) != null){
			games.get(owner).addPlayer(username,lat,lng);
		}
	}

	public static void changePosition(String owner,String username, double lat, double lng){
		if(games.get(owner) != null){
			games.get(owner).changePosition(username,lat,lng);
		}
	}

	public static void leaveGame(String owner, String username){
		if(games.get(owner) != null){
			games.get(owner).removePlayer(username);
		}
	}

	public static void deleteGame(String owner){
		if(games.get(owner) != null){
			games.remove(owner);
		}
	}
	//User log in state methods
	public static void loginUser(String username,int userid){
		if(loggedUsers.get(username) != null){
			loggedUsers.remove(username);
		}
		loggedUsers.put(username,new LoginState(userid));
	}

	public static LoginState getUser(String username){
		return loggedUsers.get(username);
	}

	public static boolean userLogged(String username){
		return loggedUsers.get(username) != null;
	}

	public static int getUserId(String username){
		LoginState st = loggedUsers.get(username);
		if(st != null){
			return st.userid;
		}else return -1;
	}

	public static String getUserAToken(String username){
		LoginState st = loggedUsers.get(username);
		if(st != null){
			return st.accessToken.toString();
		}else return null;
	}

	public static String getUserRToken(String username){
		LoginState st = loggedUsers.get(username);
		if(st != null){
			return st.refreshToken.toString();
		}else return null;
	}

	public static boolean validToken(String username){
		LoginState st = loggedUsers.get(username);
		if(st != null){
			return !st.accessTokenExpired();
		}else return false;
	}

	public static boolean validRefreshToken(String username,String token){
		LoginState st = loggedUsers.get(username);
		if(st != null){
			return st.refreshToken.equals(token);
		}else return false;
	}

	public static void logoutUser(String username){
		if(loggedUsers.get(username) != null)
			loggedUsers.remove(username);
	}

	public static void refreshTokens(String username){
		LoginState st = loggedUsers.get(username);
		if(st != null){
			st.accessToken = new Token(15*60);
			st.refreshToken = new Token(24*60*60);
		}
	}
}