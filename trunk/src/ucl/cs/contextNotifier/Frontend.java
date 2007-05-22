/**
 * 
 */
package ucl.cs.contextNotifier;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

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
public interface Frontend {

	public String getTitle();
	
	//public ContextHandler getBackend();

	public Command getFrontendCommand();

	public Displayable getBaseDisplayable();
	
	public CommandListener getDefaultCommandListener();
}