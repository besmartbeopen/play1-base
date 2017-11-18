package models.common.enums;

import java.util.Map;

import com.google.common.collect.Maps;

import play.mvc.Router;

/**
 * @author marco
 *
 */
public enum NotificationSubject {
	/**
	 * Notifiche di sistema.
	 */
	SYSTEM,
	/**
	 * Attività
	 */
	ACTIVITY,
	/**
	 * Commento
	 */
	COMMENT,
	/**
	 * Messaggio
	 */
	MESSAGE,
	/**
	 * Movimento di magazzino
	 */
	STOCK_MOVEMENT,
	/**
	 * Resoconto mensile
	 */
	WORK_EXPENSES,
	/**
	 * Ordini di vendita
	 */
	SALES_ORDER,
	/**
	 * Dati di vendita disponibili
	 */
	SELLING_INFO;

	private String toUrl(String action, Integer id) {
		if (id == null) {
			return Router.reverse(action).url;
		} else {
			final Map<String, Object> params = Maps.newHashMap();
			params.put("id", id);
			return Router.reverse(action, params).url;
		}
	}

	public boolean isRedirect() {
		return this != SYSTEM;
	}

	public String toUrl(Integer referenceId) {
		switch(this) {
		case ACTIVITY:
			return toUrl("Activities.show", referenceId);
		case COMMENT:
			return toUrl("Comments.show", referenceId);
		case MESSAGE:
			return toUrl("Messages.show", referenceId);
		case STOCK_MOVEMENT:
			return toUrl("Stocks.showMovement", referenceId);
		case WORK_EXPENSES:
			return toUrl("Expenses.show", referenceId);
		case SALES_ORDER:
			return toUrl("SalesOrders.show", referenceId);
		case SELLING_INFO:
			return toUrl("SellingInfos.byId", referenceId);
		// case SYSTEM:
		default:
			throw new IllegalStateException("unknown target: " + this.name());
		}
	}
}
