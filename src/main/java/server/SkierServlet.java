package server;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.util.regex.Pattern;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "server.SkierServlet", value = "/server.SkierServlet")
public class SkierServlet extends HttpServlet {

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    res.setContentType("text/plain");
    String urlPath = req.getPathInfo();

    // check we have a URL!
    if (urlPath != null && isUrlValid(urlPath)) {
      res.setStatus(HttpServletResponse.SC_OK);
      res.getWriter().write("{\"message\":\" Get success\"}");

    }else{
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing paramterers");
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)

//    if (!isUrlValid(urlParts)) {
//      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
//    } else {
//      res.setStatus(HttpServletResponse.SC_OK);
//      // do any sophisticated processing with u/skiers/12/seasonss/2019/day/1/skier/123rlParts which contains all the url params
//      // TODO: process url params in `urlParts`
//      res.getWriter().write("It works!");
//    }
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
    BufferedReader reader = req.getReader();
    String jsonString = "";
    try{
      for(String line;(line = reader.readLine()) != null; jsonString += line);
    }catch (IOException e){
      e.printStackTrace();
    }

    res.setContentType("application/json");
    String urlPath = req.getPathInfo();
    if(isUrlValid(urlPath)){
      res.setStatus(HttpServletResponse.SC_CREATED);
      res.getWriter().write("{\"message\":\"ok\"}");
    }else{
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("{\"message\":\"not found\"}");
    }

  }

  private boolean isUrlValid(String urlPath) {
    // TODO: validate the request url path according to the API spec
    // urlPath  = "/skiers/"
    Pattern skierApiPattern = Pattern.compile("^/skiers/?$");
    if(skierApiPattern.matcher(urlPath).matches()){
      return true;
    }
    return false;
  }

}




