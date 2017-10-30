package models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import models.base.BaseModel;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotEmpty;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import play.data.validation.Required;
import play.db.jpa.Blob;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;

/**
 * Messaggi agli operatori
 * 
 * @author cristian
 *
 */
@Entity @Audited
public class Message extends BaseModel {

	private static final long serialVersionUID = 6102438345946554763L;

	@Required 
	@ManyToOne(optional=false)
	public Operator source;
	
	@Length(min=2, max=500, message="validation.lenght")
	@Required
	@NotNull
	public String subject;
	
	public String body;
	
	@NotAudited
	@ElementCollection
	public Set<Blob> attachments = Sets.newHashSet();

	/**
	 * Contiene i destinatari, se l'hanno letto, se l'hanno
	 * marcato come starred, etc.
	 */
	@NotEmpty
	@OneToMany(mappedBy="message", cascade=CascadeType.ALL, orphanRemoval=true)
	public Set<MessageDetail> details = Sets.newHashSet();
	
	/**
	 * I tag impostati da chi crea/gestisce il messaggio
	 */
	@NotAudited
	@ElementCollection
	public Set<String> tags = Sets.newHashSet();
	
	public int detailsReadBy() {
		return FluentIterable.from(details).filter(new Predicate<MessageDetail>() {

			@Override
			public boolean apply(MessageDetail input) {
				return input.read;
			}
		}).size();
	}
	
	public Set<MessageDetail> buildDetails(Set<Operator> destinations) {
		Set<MessageDetail> details = Sets.newHashSet();
		for (Operator operator : destinations) {
			details.add(new MessageDetail(this, operator, tags));
		}
		return details;
	}
	
	public MessageDetail getDetail(Operator operator) {
		for (MessageDetail detail : details) {
			if (detail.operator.equals(operator)) {
				return detail;
			}
		}
		return null;
	}
}
