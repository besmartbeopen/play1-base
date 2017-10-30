package common.html;

import static j2html.TagCreator.*;

import common.security.SecurityRules;
import controllers.Application;
import j2html.tags.ContainerTag;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.inject.Inject;
import lombok.Builder;
import lombok.Data;
import lombok.val;
import play.mvc.Router;

/**
 * @author marco
 *
 */
public class Menu {

  private final SecurityRules rules;

  @Data
  public static class MenuItem {
    private final String action;
    private final String label;
    private final FontAwesome fa;
  }

  @Data
  public static class Submenu {
    private final String label;
    private final FontAwesome fa;
    private final List<MenuItem> items = new ArrayList<>();
  }

  @Inject
  Menu(SecurityRules rules) {
    this.rules = rules;
  }

  public ContainerTag submenu(String label, FontAwesome fa) {
    return li().withClass("dropdown").with(
        a(label).withClass(".dropdown-toggle").withData("toggle", "dropdown"),
        ul().withClass("dropdown-menu")
    );
  }

  public ContainerTag functionalMenu() {
    return ul(attrs(".nav.navbar-nav"));
//          each(filter(functionalItems(), ))
//          );
  }

  public void functionalItems() {
    val url = Router.reverse("Operators.list").url;
  }
}
