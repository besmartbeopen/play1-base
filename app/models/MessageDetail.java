package models;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import models.base.BaseModel;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import play.data.validation.Required;

import com.google.common.base.Function;
import com.google.common.collect.Sets;

/**
 * Ogni operatore ha una serie di dettagli impostabili
 * per i messaggi ricevuti.
 * Es. letto/non letto, starred, tags, etc.
 * 
 * @author cristian
 *
 */
@Entity @Audited
public class MessageDetail extends BaseModel {

	private static final long serialVersionUID = 4422003378371822039L;

	@Required @NotNull
	@ManyToOne(optional=false)
	public Message message;
	
	@Required @NotNull
	@ManyToOne(optional=false)
	public Operator operator;
	
	public boolean starred = false;
	
	public boolean read = false;
	
	/**
	 * I tag personali di ogni operatore a cui è indirizzato
	 * il messaggio
	 */
	@NotAudited
	@ElementCollection
	public Set<String> tags = Sets.newHashSet();
	
	public MessageDetail() { super(); }
	
	public MessageDetail(Message message, Operator operator,
			Set<String> tags) {
		this.message = message;
		this.operator = operator;
		this.tags = tags;
	}

	public enum toOperator implements Function<MessageDetail, Operator> {
		INSTANCE;
		
		@Override
		public Operator apply(MessageDetail detail) {
			return detail.operator;
		}
	}
}
