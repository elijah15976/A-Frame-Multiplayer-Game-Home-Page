import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Arrays;
import java.security.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

class Main {
  private static Logger lg = new Logger();
  public static String[] gameURL = {"https://48b09dc7-e9b7-4a1c-b3f7-f27f96ed5e29-00-2lap5e65rigp1.kirk.repl.co"};
  public static void main(String[] args) throws IOException{
    int port = 8080;
    Database db = new Database("jdbc:sqlite:accounts.db");
    /*
    Username = Text, Not Null, Unique
    Password = Text, Not Null (Hashed)
    Token = Text, Not Null, Unique
    UserID = Integer, Not Null, Unique
    PacmanWins = Integer, Not Null
    GhostWins = Integer, Not Null
    */

    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

    server.createContext("/favicon.ico", new RouteHandler("Website/Images/favicon.ico", "image/ico", "/favicon.ico"));
    
    server.createContext("/", new RouteHandler("Website/index.html", "text/html", "/"));
    server.createContext("/script.js", new RouteHandler("Website/script.js", "text/javascript", "/script.js"));
    server.createContext("/style.css", new RouteHandler("Website/style.css", "text/css", "/style.css"));
    server.createContext("/homepage.css", new RouteHandler("Website/homepage.css", "text/css", "/homepage.css"));
    server.createContext("/login", new RouteHandler("Website/login.html", "text/html", "/login"));
    server.createContext("/login.js", new RouteHandler("Website/login.js", "text/javascript", "/login.js"));
    server.createContext("/login.css", new RouteHandler("Website/login.css", "text/css", "/login.css"));
    server.createContext("/dropdown.js", new RouteHandler("Website/dropdown.js", "text/javascript", "/dropdown.js"));
    server.createContext("/dropdown.css", new RouteHandler("Website/dropdown.css", "text/css", "/dropdown.css"));
    server.createContext("/components.js", new RouteHandler("Website/components.js", "text/javascript", "/components.js"));
    server.createContext("/components.css", new RouteHandler("Website/components.css", "text/css", "/components.css"));

    //Assets
    server.createContext("/banner.jpg", new RouteHandler("Website/Images/Banner.jpg", "image/jpg", "/banner.jpg"));

    server.createContext("/pacman.gif", new RouteHandler("Website/Images/Pacman.gif", "image/jpg", "/pacman.gif"));

    server.createContext("/josh.jpg", new RouteHandler("Website/Images/Josh.jpg", "image/jpg", "/josh.jpg"));

    server.createContext("/pacty.png", new RouteHandler("Website/Images/Pacty.png", "image/jpg", "/pacty.png"));

    server.createContext("/back2.jpg", new RouteHandler("Website/Images/Back2.jpg", "image/jpg", "/back2.jpg"));

    server.createContext("/ghost.png", new RouteHandler("Website/Images/Ghost.png", "image/jpg", "/ghost.png"));

    server.createContext("/blinky.png", new RouteHandler("Website/Images/Blinky.png", "image/jpg", "/blinky.png"));
    
    server.createContext("/ghost.gif", new RouteHandler("Website/Images/Ghost.gif", "image/jpg", "/ghost.gif"));

    server.createContext("/bomb.png", new RouteHandler("Website/Images/Bomb.png", "image/jpg", "/bomb.png"));

    server.createContext("/glass.png", new RouteHandler("Website/Images/Glass.png", "image/jpg", "/glass.png"));

    server.createContext("/maze.png", new RouteHandler("Website/Gallery/Maze.png", "image/jpg", "/maze.png"));

    server.createContext("/maze2.png", new RouteHandler("Website/Gallery/Maze2.png", "image/jpg", "/maze2.png"));

    server.createContext("/maze3.png", new RouteHandler("Website/Gallery/Maze3.png", "image/jpg", "/maze3.png"));

    server.createContext("/lobby.png", new RouteHandler("Website/Gallery/Lobby.png", "image/jpg", "/lobby.png"));

    server.createContext("/lobby2.png", new RouteHandler("Website/Gallery/Lobby2.png", "image/jpg", "/lobby2.png"));
    
    server.createContext("/get-leaderboard", new HttpHandler(){
      public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
    
        String response = db.runSQL("SELECT Username, PacmanWins, GhostWins FROM Accounts", "json");

        RouteHandler.send(response, exchange, "/get-leaderboard");
      }
    });

    server.createContext("/get-user", new HttpHandler(){
      public void handle(HttpExchange exchange) throws IOException {
        String requestOrigin;
        try{
          requestOrigin = exchange.getRequestHeaders().get("Origin").get(0);
        }
        catch(Exception e){
          requestOrigin = "null";
        }
        for(int i = 0; i < gameURL.length; i++){
          if(gameURL[i].equals(requestOrigin)){
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", requestOrigin);
            break;
          }
          else if(i == gameURL.length - 1){
              exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
          }
        }
        exchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");
        exchange.getResponseHeaders().set("Content-Type", "application/json");

        String response = secToUser(exchange, db, "/get-user");

        RouteHandler.send(response, exchange, "/get-user");
      }
    });

    server.createContext("/update-leaderboard", new HttpHandler(){
      public void handle(HttpExchange exchange) throws IOException {
        String requestOrigin;
        try{
          requestOrigin = exchange.getRequestHeaders().get("Origin").get(0);
        }
        catch(Exception e){
          requestOrigin = "null";
        }
        for(int i = 0; i < gameURL.length; i++){
          if(gameURL[i].equals(requestOrigin)){
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", requestOrigin);
            break;
          }
          else if(i == gameURL.length - 1){
              exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
          }
        }
        exchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        Map<String, Object> parameters = RouteHandler.parseParameters("post",exchange);

        leaderboardUpdate(exchange, db, "/update-leaderboard", parameters);

        String response = db.runSQL("SELECT Username, PacmanWins, GhostWins FROM Accounts", "json");

        RouteHandler.send(response, exchange, "/update-leaderboard");
      }
    });

    server.createContext("/log-in", new HttpHandler(){
      public void handle(HttpExchange exchange) throws IOException {
        Map<String, Object> parameters = RouteHandler.parseParameters("post",exchange);
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        String response;
        String dbres = "";
        String username = "";
        String password = "";
        String num = "";
        String token = "";
        try{
          username = parameters.get("username").toString().toLowerCase().trim();
          password = parameters.get("password").toString();
          num = parameters.get("num").toString();
        }
        catch(Exception e){
          username = "";
          password = "";
          num = "";
        }

        try{
          if((!username.isEmpty()) &&
             (!password.isEmpty()) &&
             (!num.isEmpty())){
            if(num.equals("0")){
              dbres = db.runSQL("SELECT HashedPW FROM Accounts WHERE Username='"+username+"'", "csv");
              dbres = dbres.substring(dbres.indexOf(",")+2, dbres.length()-2);
              if(PasswordHasher.compareHash(dbres, password)){
                token = db.runSQL("SELECT Token FROM Accounts WHERE Username='"+username+"'", "csv");
                token = token.substring(token.indexOf(",")+2, token.length()-2);
                ManagerCookie.setValue(exchange, "token", token);
                response = "success";
              }
              else{
                response = "Password Invalid";
              }
            }
            else if(num.equals("1")){
              if(db.runSQL("SELECT Username FROM Accounts WHERE Username='"+username+"'", "json").equals("[]")){
                lg.dispMessage("New User: "+username, "info");
                String code = randomLetters();
                while(!db.runSQL("SELECT Username FROM Accounts WHERE Token='"+code+"'", "json").equals("[]")){
                  //System.out.println(db.runSQL("SELECT Email FROM Verification WHERE verificationCode='"+code+"'", "json"));
                  code = randomLetters();
                }
                String numCode = userIDGenerator();
                while(!db.runSQL("SELECT Username FROM Accounts WHERE UserID='"+numCode+"'", "json").equals("[]")){
                  //System.out.println(db.runSQL("SELECT Email FROM Verification WHERE verificationCode='"+code+"'", "json"));
                  numCode = userIDGenerator();
                }
                boolean regStatus = db.runSQL("INSERT INTO Accounts (Username, HashedPW, Token, UserID, PacmanWins, GhostWins) VALUES ('"+username+"', '"+PasswordHasher.hash(password)+"', '"+code+"', "+numCode+", 0, 0)");
                if(regStatus){
                  response = "User created";
                }
                else{
                  response = "Internal Error";
                }
              }
              else{
                response = "User Already Registered";
              }
            }
            else{
              response = "Internal Error";
            }
          }
          else{
            response = "Bad Parameters";
          }
        }
        catch(StringIndexOutOfBoundsException e){
          if(num.equals("0")){
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            lg.dispMessage(writer.toString() + " ---- Main.java", "error");
            response = "User Not Found";
          }
          else{
            response = "There is literally no way to get here. If you manage to get here, congratulations";
          }
        }
        catch(NoSuchAlgorithmException e){
          Writer writer = new StringWriter();
          e.printStackTrace(new PrintWriter(writer));
          lg.dispMessage(writer.toString() + " ---- Main.java", "crit");
          response = "Critical";
        }
        RouteHandler.send(response, exchange, "/log-in");
      }
    });
    
    server.start();
    lg.dispMessage("Server is listening on port "+ port, "info");
  }
  
  public static String userIDGenerator(){
    SecureRandom r = new SecureRandom();
    String generated = Integer.toString(r.nextInt(99999))+Integer.toString(r.nextInt(99999));
    while(generated.length() < 10){
      generated = "0"+generated;
    }
    return generated;
  }
  
  public static String randomLetters(){
    SecureRandom r = new SecureRandom();
    char[] codeLetters = new char[12];
    for(int i = 0; i<codeLetters.length; i++){
      codeLetters[i] = (char)(r.nextInt(25) + 'a');
    }
    return new String(codeLetters);
  }

  public static String secToUser(HttpExchange exchange, Database db, String route){
    //NOTE: Function already checks for verification
    String response;
    String checkLogOut = ManagerCookie.getValue(exchange, "logOut");
    try{
      if(!checkLogOut.equals("1")){
        throw null;
      }
      else{
        response = "success";
      }
    }
    catch(Exception e){
      try{
        String cookieValue = ManagerCookie.getValue(exchange, "token");
        if(cookieValue == null){
          response = "{}";
        }
        else{
          String username = db.runSQL("SELECT Username FROM Accounts WHERE Token='" + cookieValue +"'", "csv");
          String userID = db.runSQL("SELECT UserID FROM Accounts WHERE Token='" + cookieValue +"'", "csv");
          String usernameResponse = username.substring(username.indexOf(",")+2, username.length()-2);
          String userIDResponse = userID.substring(userID.indexOf(",")+2, userID.length()-2);
          response = "{\"username\": \""+usernameResponse+"\", \"userID\": \""+userIDResponse+"\"}";
        }
        lg.dispMessage("Response: " + response, "info");
      }
      catch(Exception egg){
        response = "";
        Writer writer = new StringWriter();
        egg.printStackTrace(new PrintWriter(writer));
        lg.dispMessage(writer.toString() + " ~ " + route, "warn");
      }
    }
    return response;
  }

  public static boolean leaderboardUpdate(HttpExchange exchange, Database db, String route, Map<String, Object> para){
    boolean response;
    try{
      String cookieValue = ManagerCookie.getValue(exchange, "token");
      // System.out.println(cookieValue);
      // System.out.println(para.get("winState").toString());
      // System.out.println(para.get("winState").toString().equals("0"));
      // System.out.println(para.get("winState").toString().equals("1"));
      if(cookieValue == null){
        response = false;
      }
      else{
        //Ghost Win
        if(para.get("winState").toString().equals("0")){
          response = db.runSQL("UPDATE Accounts SET GhostWins = GhostWins + 1 WHERE Token='" + cookieValue +"'");
        }
        //Pacman Win
        else if(para.get("winState").toString().equals("1")){
          response = db.runSQL("UPDATE Accounts SET PacmanWins = PacmanWins + 1 WHERE Token='" + cookieValue +"'");
        }
        else{
          response = false;
        }
      }
      lg.dispMessage("Response: " + response, "info");
    }
    catch(Exception egg){
      response = false;
      Writer writer = new StringWriter();
      egg.printStackTrace(new PrintWriter(writer));
      lg.dispMessage(writer.toString() + " ~ " + route, "warn");
    }
    return response;
  }
}