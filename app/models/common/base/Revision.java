package models.common.base;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import org.joda.time.LocalDateTime;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import models.common.Operator;

/**
 * @author marco
 *
 */
@Entity
@RevisionEntity(ExtendedRevisionListener.class)
public class Revision {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @RevisionNumber
    public int id;

    @RevisionTimestamp
    public long timestamp;

    @Transient
    public Date getRevisionDate() {
        return new Date(timestamp);
    }

    @Transient
    public LocalDateTime getRevisionLocalDateTime() {
    	return new LocalDateTime(timestamp);
    }

	@ManyToOne(optional=true)
	public Operator owner;

	// ip address
	public String ipaddress;

    @Override
    public boolean equals(Object o) {

    	if (o instanceof Revision) {
    		final Revision other = (Revision) o;
    		return id == other.id;
    	} else {
    		return false;
    	}
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", id)
				.add("date", getRevisionDate())
				.add("owner", owner)
				.add("ipaddress", ipaddress)
				.omitNullValues()
				.toString();
	}
}
