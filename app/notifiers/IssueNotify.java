package notifiers;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import common.issues.FeedbackData;
import common.notifiers.InlineStreamHandler;
import controllers.Security;
import models.common.Operator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.mail.EmailAttachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Play;
import play.mvc.Mailer;
import play.mvc.Scope;

/**
 * @author marco
 *
 */
public class IssueNotify extends Mailer {

  private static final Logger LOG = LoggerFactory.getLogger(IssueNotify.class);
  private static final Splitter COMMAS = Splitter.on(',').trimResults()
      .omitEmptyStrings();

  public static void feedback(FeedbackData data, Scope.Session session,
      Optional<Operator> operator) {

    // differenziati i destinatari in funzione di chi ha riportato la
    // notifica.
    String propertyName = "feedback.to";
    if (operator.isPresent() &&
        !Security.getRealOperator().isPresent()) {
      propertyName = "feedback.inf.to";
    }
    final List<String> dests = COMMAS.splitToList(Play.configuration
        .getProperty(propertyName, ""));
    if (dests.size() == 0) {
      LOG.error("please set {} in application.conf", propertyName);
      return;
    }
    for (String to : dests) {
      addRecipient(to);
    }
    if (operator.isPresent()) {
      if (Security.getRealOperator().isPresent()) {
        setReplyTo(Security.getRealOperator().get().email);
      } else {
        setReplyTo(operator.get().email);
      }
    }
    setFrom(Play.configuration.getProperty("feedback.from",
        "Feedback <feedback@inphase.it>"));

    setSubject(Play.configuration.getProperty("feedback.subject",
        "InPhase Feedback"));

    try {
      ByteArrayOutputStream htmlGz = new ByteArrayOutputStream();
      try (GZIPOutputStream gz = new GZIPOutputStream(htmlGz)) {
        gz.write(data.html.getBytes());
      };

      URL htmlUrl = new URL(null, "inline:///html",
          new InlineStreamHandler(htmlGz.toByteArray(), "application/gzip"));
      EmailAttachment html = new EmailAttachment();
      html.setDescription("Original HTML");
      html.setName("page.html.gz");
      html.setURL(htmlUrl);
      html.setDisposition(EmailAttachment.ATTACHMENT);
        addAttachment(html);

        URL imgUrl = new URL(null, "inline://image",
            new InlineStreamHandler(data.img, "image/png"));
      EmailAttachment img = new EmailAttachment();
      img.setDescription("Feedback image");
      img.setName("image.png");
      img.setURL(imgUrl);
      img.setDisposition(EmailAttachment.ATTACHMENT);
        addAttachment(img);

      send(operator, data, session);
    } catch (MalformedURLException e) {
      LOG.error("malformed url", e);
    } catch (IOException e) {
      LOG.error("io error", e);
    }

  }
}
