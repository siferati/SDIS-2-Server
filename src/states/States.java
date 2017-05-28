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
	public static boolean createGame(String owner,String map){
		if(games.get(owner) != null){
			games.remove(owner);
			games.put(owner, new GameState(map));
		}else games.put(owner, new GameState(map));
		System.out.println("Game create by " + owner + " on map " + map);
		return true;
	}

	public static boolean joinGame(String owner,String username, String position){
		if(games.get(owner) != null){
			games.get(owner).addPlayer(username,position);
		}else return false;
		System.out.println("User " + username + " has joined the game hosted by " + owner);
		return true;
	}

	public static boolean changePosition(String owner,String username, String position){
		if(games.get(owner) != null){
			return games.get(owner).changePosition(username,position);
		}else return false;
	}

	public static boolean leaveGame(String owner, String username){
		boolean success = false;
		if(games.get(owner) != null){
			success = games.get(owner).removePlayer(username);
		}
		if(success)
			System.out.println("User " + username + " has left the game hosted by " + owner);
		return success;
	}

	public static boolean deleteGame(String owner){
		if(games.get(owner) != null){
			games.remove(owner);
		}else return false;
		System.out.println("Game hosted by " + owner + " has been ended");
		return true;
	}
	//User log in state methods
	public static void loginUser(String username,int userid){
		if(loggedUsers.get(username) != null){
			loggedUsers.remove(username);
		}
		loggedUsers.put(username,new LoginState(userid));
		System.out.println("User " + username + " has logged in");
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