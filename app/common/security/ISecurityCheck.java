package common.security;

import com.google.inject.ImplementedBy;

/**
 * @author marco
 *
 */
@ImplementedBy( SecurityRules.class)
public interface ISecurityCheck {

	/**
	 * Controlla se è possibile eseguire il metodo corrente sull'oggetto
	 * instance, e in caso negativo riporta un accesso negato.
	 *
	 * @param instance
	 */
	void checkIfPermitted(Object instance);

	/**
	 * Corrisponde alla secure.check implicitamente eseguita su tutti i metodi.
	 */
	void checkIfPermitted();

	/**
	 * @param instance
	 * @return true se l'azione corrente è permessa sull'istanza fornita, false altrimenti.
	 */
	boolean check(Object instance);

	/**
	 * @param action
	 * @param instance
	 * @return true se l'azione fornita è permessa sull'istanza fornita, false altrimenti.
	 */
	boolean check(String action, Object instance);

	/**
	 * @param action
	 * @param role
	 * @return true se l'azione corrente è permessa, false altrimenti.
	 */
	boolean checkAction();

	/**
	 * @param action
	 * @return true se l'azione fornita è permessa, false altrimenti.
	 */
	boolean checkAction(String action);

}