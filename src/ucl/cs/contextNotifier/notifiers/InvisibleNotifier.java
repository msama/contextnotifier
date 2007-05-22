/**
 * This Notifiers executes actions in backgrounds. 
 * All the child of this Notifier are invisible.
 */
package ucl.cs.contextNotifier.notifiers;

import javax.microedition.midlet.MIDlet;

import ucl.cs.contextNotifier.Activity;
import ucl.cs.contextNotifier.Notification;
import ucl.cs.contextNotifier.Notifier;
import ucl.cs.contextNotifier.Rule;

/**
 * @author -Michele Sama- aka -RAX-
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
public class InvisibleNotifier implements Notifier, Runnable {

	MIDlet _activeMidlet=null;
	
	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.Notifier#notifyActivity(java.lang.String, java.lang.String)
	 */
	public void notifyActivity(Activity act, Rule rule, Notification notification) {
		Thread th=new Thread(this,act.getName()+": "+rule.getName());
		th.start();
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.Notifier#setActiveMIDlet(javax.microedition.midlet.MIDlet)
	 */
	public void setActiveMIDlet(MIDlet dlet) {
		this._activeMidlet=dlet;
	}

	public void run() {
		// Does Nothing
	}

}
