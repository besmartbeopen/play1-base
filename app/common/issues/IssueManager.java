package common.issues;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import common.gitlab.Gitlab;
import common.gitlab.model.Issue;
import common.gitlab.model.IssueState;
import common.gitlab.model.OrderBy;
import common.gitlab.model.SortDirection;
import common.gitlab.model.Upload;
import common.gitlab.model.UploadFile;
import lombok.extern.slf4j.Slf4j;

import play.Play;

/**
 * @author marco
 *
 */
@Slf4j
public class IssueManager {

  private final int projectId;
  private final Gitlab gitlab;
  private final String customer;

  @Inject
  public IssueManager(@Named("issues.projectId") int projectId,
      Gitlab gitlab) {

    this.projectId = projectId;
    this.gitlab = gitlab;
    this.customer = Play.configuration.getProperty("site.customer", "InPhase");
  }

  /**
   * @return tutti le issue pubbliche.
   */
  public List<Issue> issues(IssueState state) {
    return gitlab.issues(projectId, state, ImmutableSet.of("public"),
        OrderBy.updated_at, SortDirection.desc);
  }

  public void create(String description, byte[] img, String html) {
    final Upload uploadedImg = gitlab.upload(projectId,
        new UploadFile("screenshot.png", img));
    final Upload uploadedHtml = gitlab.upload(projectId,
        new UploadFile("page.html", html.getBytes()));
    final Issue issue = gitlab.createIssue(projectId, "Nuova segnalazione",
        Joiner.on("\n").join(description, uploadedImg.getMarkdown(),
            uploadedHtml.getMarkdown()).toString(), false, null, null,
        "from-web," + customer, null);
    log.info("gitlab issue created: {}", issue);
  }
}
