/**
 *
 */
package ucl.cs.contextNotifier.notifiers;

import java.io.IOException;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.midlet.MIDlet;

import ucl.cs.contextNotifier.Activity;
import ucl.cs.contextNotifier.DecisionEngine;
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
public class RingtoneNotifier implements Notifier, Runnable {

	private MIDlet _currentMIDlet=null;
	private Image _image=null;
	private String _ringtoneFilename="/audio/pattern.mid";
	private Alert _alert=null;
	
	/**
	 * 
	 */
	public RingtoneNotifier() {
		try {
			this._image=Image.createImage(this.getClass().getResourceAsStream("/resources/music_note.jpeg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this._alert=new Alert("RingtoneNotifier","",this._image,AlertType.ALARM); 
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.VibrationNotifier#notifyActivity(java.lang.String, java.lang.String)
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
		
		
        
        //Starting music
        Thread th=new Thread(this);
        th.start();
        
        disp.vibrate(3);

	}

	public void setActiveMIDlet(MIDlet dlet) {
		this._currentMIDlet=dlet;
	}

	public void run() {
		try {
			Player p=Manager.createPlayer(this.getClass().getResourceAsStream(this._ringtoneFilename), "audio/midi");
			
			long duration=p.getDuration();
			if(duration==Player.TIME_UNKNOWN||duration<100)
			{
				duration=7100;
			}else
			{
				//Player.getDuration is in Microseconds not in milliseconds!!!!!!
				duration*=10;
				duration+=100;
			}
			System.out.println("RingtoneNotifier.run: ringtone_duration="+duration);
			p.realize();
			p.setLoopCount(20);
			p.start();
			while(Display.getDisplay(this._currentMIDlet).getCurrent().equals(this._alert))
			{
				Display.getDisplay(this._currentMIDlet).vibrate(1);
				
				
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			p.stop();
			p.deallocate();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MediaException e) {
			e.printStackTrace();
		}
		
	}


}
