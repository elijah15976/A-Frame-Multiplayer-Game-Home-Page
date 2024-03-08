//import java.net.*;
//import java.util.List;
import com.sun.net.httpserver.HttpExchange;

//https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies
public class ManagerCookie{
  private static Logger lg = new Logger();
  private static String url = "f2166e48-2864-40b5-b38a-00a461eb243a-00-j15vqjkvw7bk.riker.replit.dev";
  public static String getValue(HttpExchange exchange, String key){
    String cook;
    try{
      cook = exchange.getRequestHeaders().getFirst("Cookie").toString();
    }
    catch(Exception e){
      return null;
    }
    //lg.dispMessage(cook, "info");
    String cooks;
    try{
      if(cook.indexOf(key) != -1){
        cooks = cook.substring(cook.indexOf(key) + key.length() + 1);
        //lg.dispMessage(cooks, "info");
      }
      else{
        throw null;
      }
      try{
        cooks = cooks.substring(0, cooks.indexOf(";"));
        //lg.dispMessage(cooks, "info");
      }
      catch(Exception e){
        return cooks;
      }
    }
    catch(Exception e){
      cooks = null;
    }

    return cooks;
  }
  public static void setValue(HttpExchange exchange, String key, String value){
    String cooks = key + "=" + value + ";Domain=" + url + ";Path=/;Max-Age=604800;SameSite=None;Secure";
    exchange.getResponseHeaders().add("Set-Cookie", cooks);
  }
}