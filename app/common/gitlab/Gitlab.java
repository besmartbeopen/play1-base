package common.gitlab;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import common.gitlab.model.Issue;
import common.gitlab.model.IssueState;
import common.gitlab.model.OrderBy;
import common.gitlab.model.SortDirection;
import common.gitlab.model.Upload;
import common.gitlab.model.UploadFile;
import common.gitlab.pagination.Page;
import common.gitlab.pagination.PaginatedList;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;

@Headers("Accept: application/json")
public interface Gitlab {

  /**
   * @param projectId
   * @param state
   * @param labels
   * @param orderBy
   * @param sort
   * @param perPage
   * @param page
   * @return a page of issues
   */
  @RequestLine("GET /api/v3/projects/{project}/issues?page={page}&per_page={perPage}&state={state}&labels={labels}&sort={sort}&order_by={orderBy}")
  Page<Issue> issuesPaginated(@Param("project") int projectId,
      @Param("state") IssueState state,
      @Param("labels") String labels, // comma separated
      @Param("orderBy") OrderBy orderBy,
      @Param("sort") SortDirection sort,
      @Param("perPage") Integer perPage,
      @Param("page") Integer page);

  default PaginatedList<Issue> issues(int projectId, IssueState state,
      Set<String> labels, OrderBy orderBy, SortDirection sort) {

    return new PaginatedList<Issue>(page -> issuesPaginated(projectId,
        state,
        Optional.ofNullable(labels)
          .map(ll -> Strings.emptyToNull(Joiner.on(',').skipNulls().join(ll)))
          .orElse(null),
        orderBy, sort,
        PaginatedList.DEFAULT_PAGE_SIZE,
        page));
  }

  /**
   * @param projectId required
   * @param title required
   * @param description
   * @param confidential
   * @param assigneeId
   * @param milestoneId
   * @param labels comma separated
   * @param dueDate
   * @return the new issue
   */
  @RequestLine("POST /api/v3/projects/{project}/issues")
  @Headers("Content-Type: multipart/form-data")
  Issue createIssue(@Param("project") int projectId,
      @Param("title") String title,
      @Param("description") String description,
      @Param("confidential") boolean confidential,
      @Param("assignee_id") Integer assigneeId,
      @Param("milestone_id") Integer milestoneId,
      @Param("labels") String labels,
      @Param("due_date") LocalDate dueDate);

  @RequestLine("POST /api/v3/projects/{project}/issues/{issue}/notes")
  Response createIssueNote(@Param("project") int projectId,
      @Param("issue") int issueId, @Param("body") String body);

  @RequestLine("POST /api/v3/projects/{project}/uploads")
  @Headers("Content-Type: multipart/form-data")
  Upload upload(@Param("project") int projectId, @Param("file") UploadFile file);
}
