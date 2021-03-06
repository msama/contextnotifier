/**
 * 
 */
package ucl.cs.contextNotifier;

import javax.microedition.midlet.MIDlet;

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
public interface Notifier {
	
	public void notifyActivity(Activity act, Rule rule, Notification not);

	public void setActiveMIDlet(MIDlet dlet);
}
