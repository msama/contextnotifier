/**
 *
 */
package ucl.cs.contextNotifier.handlers;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;

import ucl.cs.contextNotifier.Frontend;

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
public class FileSystemFrontend extends List implements Frontend, CommandListener {

	private Command _command=new Command("FileSystemFrontend","Frontend for the FileSystem context handler.",Command.ITEM,1);

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public FileSystemFrontend(String arg0, int arg1, String[] arg2, Image[] arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public FileSystemFrontend(String arg0, int arg1) {
		super(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.Frontend#getBaseDisplayable()
	 */
	public Displayable getBaseDisplayable() {
		return this;
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.Frontend#getDefaultCommandListener()
	 */
	public CommandListener getDefaultCommandListener() {
		return this;
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.Frontend#getFrontendCommand()
	 */
	public Command getFrontendCommand() {
		return this._command;
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.Frontend#getTitle()
	 */
	public String getTitle() {
		return "FileSystemHandler";
	}

	public void commandAction(Command arg0, Displayable arg1) {
		// TODO Auto-generated method stub
		
	}

}
