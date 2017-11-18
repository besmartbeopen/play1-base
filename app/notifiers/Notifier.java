package notifiers;

import common.data.InConfiguration;
import models.common.Operator;
import models.common.RecoveryRequest;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.i18n.Messages;

/**
 * @author marco
 *
 */
public class Notifier extends play.mvc.Mailer {

	static final Logger log = LoggerFactory.getLogger(Notifier.class);

	@Inject
	static InConfiguration configuration;

	public static void operatorRegistered(Operator operator, String code) {
		setFrom(configuration.generalFrom);
		setSubject(Messages.get("site.title", "registration"));
		addRecipient(operator.email);
		send(operator, code);
	}

	public static void passwordResetRequest(RecoveryRequest recoveryRequest) {
		setFrom(configuration.generalFrom);
		setSubject(Messages.get("site.title", "password.request"));
		addRecipient(recoveryRequest.operator.email);
		send(recoveryRequest);
	}
}
