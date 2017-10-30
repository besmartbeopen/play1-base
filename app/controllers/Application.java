package controllers;

import com.google.common.net.MediaType;
import play.mvc.Controller;

public class Application extends Controller {

  public static void index() {
    render();
  }

  public static void script() {
    response.setContentTypeIfNotSet(MediaType.JAVASCRIPT_UTF_8.toString());
    render("./Application/script.js");
  }
}
