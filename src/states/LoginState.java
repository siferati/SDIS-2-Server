package states;

import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import java.util.*;
import java.sql.*;
import java.io.*;
import db.*;

public class LoginState{
	public int userid;
	public Token refreshToken;
	public Token accessToken;
	public LoginState(int user){
		//Assign password
		this.userid = userid;
		//Create refresh token with 24 hour expiration
		refreshToken = new Token(60*60*24);
		//Create access token with 15 min expiration
		accessToken = new Token(60*15);
	}

	public boolean accessTokenExpired(){
		return accessToken.isExpired();
	}
	public void renewAccessToken(){
		accessToken = new Token(60*15);
	}
}