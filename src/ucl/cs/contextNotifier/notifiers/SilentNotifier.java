/**
 * 
 */
package ucl.cs.contextNotifier.notifiers;

import java.io.IOException;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

import ucl.cs.contextNotifier.Activity;
import ucl.cs.contextNotifier.DecisionEngine;
import ucl.cs.contextNotifier.Notification;
import ucl.cs.contextNotifier.Notifier;
import ucl.cs.contextNotifier.Rule;

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
public class SilentNotifier implements Notifier{
	
	private MIDlet _currentMIDlet=null;
	private Image _image=null;
	private Alert _alert=null;
	
	/**
	 * 
	 */
	public SilentNotifier() {
		super();
		try {
			this._image=Image.createImage(this.getClass().getResourceAsStream("/resources/music.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this._alert=new Alert("SilentNotifier","",this._image,AlertType.ALARM); 
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.Notifier#notify(java.lang.String)
	 */
	public void notifyActivity(Activity act, Rule rule, Notification notification) {
		
		Display disp= Display.getDisplay(this._currentMIDlet);

		this._alert.setTitle(act.getName()+": "+rule.getName());
		this._alert.setString(notification._params.get("Message").toString());
		this._alert.setTimeout(Alert.FOREVER);
		
		//In order to solve the frontend dispose bug after an alert we should switch to the activity frontend
		//disp.setCurrent(alert,disp.getCurrent());
		disp.setCurrent(this._alert,DecisionEngine.getInstance().getActivityFrontend());
		//****************
	}


	public void setActiveMIDlet(MIDlet dlet) {
		this._currentMIDlet=dlet;
	}

}
