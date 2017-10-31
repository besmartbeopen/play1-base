package controllers;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.gson.GsonBuilder;
import com.querydsl.core.QueryResults;
import common.gitlab.model.Issue;
import common.gitlab.model.IssueState;
import common.issues.FeedbackData;
import common.issues.ImageToByteArrayDeserializer;
import common.issues.IssueManager;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import models.Operator;
import models.enums.Role;
import notifiers.IssueNotify;
import play.Play;
import play.cache.Cache;
import play.mvc.Controller;
import play.templates.TemplateLoader;

/**
 * @author marco
 *
 */
@Slf4j
public class Issues extends Controller {

  private final static int PAGE_SIZE = 10;

  @Inject
  static IssueManager issueManager;

  public static void create() {

    final FeedbackData data = new GsonBuilder()
        .registerTypeHierarchyAdapter(byte[].class,
            new ImageToByteArrayDeserializer()).create()
        .fromJson(new InputStreamReader(request.body),
            FeedbackData.class);

    log.info("issues.create = {}, headers = {}", data, request.headers);
    final Optional<Operator> currentUser = Security.getCurrentUser();
    final Optional<Operator> realUser = Security.getRealOperator();
    // Attenzione: le notifiche sono inviate sempre via email-notifier
    // se non si è in produzione.
    if (Play.mode.isProd() && currentUser.isPresent() && (realUser.isPresent()
        || currentUser.get().roles.contains(Role.administrator))) {

      final Map<String, Object> params = new HashMap<>();
      params.put("clientIp", request.remoteAddress);
      params.put("data", data);
      params.put("session", session);
      params.put("operator", currentUser);
      params.put("sudo", realUser);
      params.put("subscriber", Play.configuration.getProperty("issues.subscriber"));
      final String descr = TemplateLoader.load("/IssueNotify/feedback.md").render(params);

      issueManager.create(descr, data.img, data.html);
    } else {
      IssueNotify.feedback(data, session, currentUser);
    }
    renderJSON(ImmutableMap.of("success", "ok"));
  }

  public static void issues(Integer page) {
    int p = Optional.fromNullable(page).or(1);
    @SuppressWarnings("unchecked")
    List<Issue> issueList = Cache.get("issues", List.class);
    if (issueList == null) {
      // in cache tutti quelli pubblici
      issueList = issueManager.issues(null);
      Cache.set("issues", issueList, "1h");
    }
    Integer closed = Cache.get("noIssuesClosed", Integer.class);
    if (closed == null) {
      closed = issueManager.issues(IssueState.closed).size();
      Cache.set("noIssuesClosed", closed);
    }
    final long total = issueList.size();
    final int offset = (p - 1) * PAGE_SIZE;
    final QueryResults<Issue> results = new QueryResults<>(issueList
        .subList(offset, offset + PAGE_SIZE),
        (long) PAGE_SIZE, (long) offset, total);
    render(results, closed);
  }
}
