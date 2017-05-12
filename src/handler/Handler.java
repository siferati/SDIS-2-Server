import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class Handler implements HttpHandler {

    String response;

    @Override
    public void handle(HttpExchange t) { 
         
        String method = t.getRequestMethod();
        String path = t.getRequestURI().getPath();

    }


}