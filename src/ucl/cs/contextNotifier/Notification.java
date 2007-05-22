/**
 * 
 */
package ucl.cs.contextNotifier;

import java.util.Hashtable;
import java.util.Vector;

/**
 * @author -Michele Sama- aka -RAX-
 * 
 *
 * University College London
 * Dept. of Computer Science
 * Gower Street
 * London WC1E 6BT
 * United Kingdom
 *
 * Email: M.Sama (at) cs.ucl.ac.uk
 *
 * Group:
 * Software Systems Engineering
 *
 */
public class Notification {

	private String _notifierName="";
	private boolean _removeActivity=false;
	private boolean _removeRule=false;
	public Hashtable _params=new Hashtable();

	/**
	 * @param name
	 * @param activity
	 * @param rule
	 */
	public Notification(String name, boolean activity, boolean rule) {
		super();
		_notifierName = name;
		_removeActivity = activity;
		_removeRule = rule;
	}
	/**
	 * @param _notifierName the _notifierName to set
	 */
	public void setNotifierName(String _notifierName) {
		this._notifierName = _notifierName;
	}
	/**
	 * @return the _notifierName
	 */
	public String getNotifierName() {
		return _notifierName;
	}
	/**
	 * @param _removeActivity the _removeActivity to set
	 */
	public void setRemoveActivity(boolean _removeActivity) {
		this._removeActivity = _removeActivity;
	}
	/**
	 * @return the _removeActivity
	 */
	public boolean isRemoveActivity() {
		return _removeActivity;
	}
	/**
	 * @param _removeRule the _removeRule to set
	 */
	public void setRemoveRule(boolean _removeRule) {
		this._removeRule = _removeRule;
	}
	/**
	 * @return the _removeRule
	 */
	public boolean isRemoveRule() {
		return _removeRule;
	}
	
}
