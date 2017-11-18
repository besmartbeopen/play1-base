package models.common;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;

import com.google.common.base.MoreObjects;

import models.common.base.BaseModel;

/**
 * @author marco
 *
 */
@Entity
public class RecoveryRequest extends BaseModel {

	private static final long serialVersionUID = -6543588624158169652L;

	@Required
	public String code;

	@Required
	public String ipaddress;

	@Required
	@ManyToOne(optional=false)
	public Operator operator;
	
	public static RecoveryRequest createFrom(Operator operator, String ip) {
		final RecoveryRequest rr = new RecoveryRequest();
		rr.code = UUID.randomUUID().toString().replace("-", "");
		rr.ipaddress = ip;
		rr.operator = operator;
		operator.recoveryRequests.add(rr);
		return rr.save();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("operator.id", operator.id)
				.add("code", code)
				.add("ip", ipaddress)
				.toString();
	}
}
