/**
 * 
 */
package ucl.cs.contextNotifier.handlers;



import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;

import ucl.cs.contextNotifier.Frontend;

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
public class TimeHandlerFrontend extends List implements Frontend, CommandListener {

	private Command _command=new Command("TimeHandler","Frontend for the TimeHandler context handler.",Command.ITEM,1);
	
	private Image _clockImage=null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public TimeHandlerFrontend(String arg0, int arg1, String[] arg2, Image[] arg3) {
		super(arg0, arg1, arg2, arg3);
		this.init();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public TimeHandlerFrontend(String arg0, int arg1) {
		super(arg0, arg1);
		this.init();
	}
	
	
	private void init()
	{
		try {
			this._clockImage=Image.createImage(this.getClass().getResourceAsStream("/resources/clock.jpeg"));//Image.createImage("ActivityIcon.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	
	public int append(String srt, Image img) {
		if(img==null)
		{
			img=this._clockImage;
		}
		return super.append(srt, img);
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command arg0, Displayable arg1) {
		// TODO Auto-generated method stub

	}

}
