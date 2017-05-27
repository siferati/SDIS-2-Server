package server;

import handler.*;

import java.io.IOException;
import java.io.FileInputStream;
import com.sun.net.httpserver.HttpsServer;
import com.sun.net.httpserver.HttpsConfigurator;
import java.net.InetSocketAddress;
import java.sql.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import com.sun.net.httpserver.HttpsParameters;
import java.security.KeyStore;


public class GameServer {

    private static HttpsServer httpsServer;
    private static int port = 8000;
    private static Connection sqlConn;
    private static char[] password = "123456".toCharArray();

    public static void main (String[] args){
    	// Database try
        try {

            //TODO: change path!
            String path = "jdbc:sqlite:../db/maps.db";
            Class.forName("org.sqlite.JDBC");
            sqlConn = DriverManager.getConnection(path);
            if(sqlConn == null){
                System.err.println("Couldn't open database successfully!");
                return;
            }
        } catch (Exception e ){
            System.err.println("Couldn't open database successfully!");
            return;
        }

        System.out.println("Database opened successfully.");

        // Server try
        try {
            
            // Create Server
            httpsServer = HttpsServer.create(new InetSocketAddress(port), 0);
            
            // Initiate SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");

            // Load KeyStore (server.keys) with JKS type
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(new FileInputStream("server.keys"), password);

            // Load TrustStore to trust self-signed cert. (truststore file)
			KeyStore trustStore = KeyStore.getInstance("JKS");
			trustStore.load(new FileInputStream("truststore"),password);

            TrustManagerFactory trustmanagerfactory = TrustManagerFactory.getInstance("SunX509");
            trustmanagerfactory.init(trustStore);

            KeyManagerFactory keymanagerfactory = KeyManagerFactory.getInstance("SunX509");
            keymanagerfactory.init(keystore, password);

            // Initiate ssl context with certificates read
            sslContext.init(keymanagerfactory.getKeyManagers(), trustmanagerfactory.getTrustManagers(), null);
            
            // Config https server
            httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
                public void configure(HttpsParameters params) {
                    try {

                        SSLContext c = SSLContext.getDefault();
                        SSLEngine engine = c.createSSLEngine();

                        params.setNeedClientAuth(false);
                        params.setCipherSuites(engine.getEnabledCipherSuites());
                        params.setProtocols(engine.getEnabledProtocols());

                        SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
                        params.setSSLParameters(defaultSSLParameters);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // Context + Handler
            httpServer.createContext("/users", new UserAccHandler(sqlConn));
            httpServer.createContext("/maps", new MapHandler(sqlConn));
            httpServer.createContext("/tokens", new TokenHandler(sqlConn));
            httpServer.createContext("/tokens", new GameHandler(sqlConn));

            // Create Default Executor
            httpsServer.setExecutor(null);


        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Start the server
        httpsServer.start();
        System.out.println("Started httpsServer on port 8000");
    }


}