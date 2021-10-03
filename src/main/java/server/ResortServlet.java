package server;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.util.regex.Pattern;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "server.ResortServlet", value = "/server.ResortServlet")
public class ResortServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String urlPath = request.getPathInfo();

    if(urlPath != null && !urlPath.isEmpty() && isUrlValid(urlPath)){
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().write("{\"message\":\" Get success\"}");

    }else{
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("{\"message\":\"not found or invalid url\"}");
    }

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    BufferedReader reader = request.getReader();
    String jsonString = "";
    for(String line; (line = reader.readLine()) != null; jsonString += line);

    response.setContentType("application/json");
    String urlPath = request.getPathInfo();
    if(!isUrlValid(urlPath)){
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("{\"message\": \"not found\"}");
    }else{
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().write("{\"message\":\"ok\"}");
    }

  }

  private boolean isUrlValid(String urlPath) {
    // TODO: validate the request url path according to the API spec
    // String[] urlPath or String urlPath?
    // urlPath  = "/resorts"
    // urlPath  = "/resorts/{resortID}/seasons"

    Pattern resortApiPattern = Pattern.compile("^/resorts/?$");
    return resortApiPattern.matcher(urlPath).matches();
  }


}
