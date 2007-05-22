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
public class BlueToothFinderFrontend extends List implements Frontend, CommandListener{

	private Command _command=new Command("BlueToothFinder","Frontend for the BlueToothFinder context handler.",Command.ITEM,1);
	
	private Image _btImage=null;
	
	private Image _miscellaneous=null;
	private Image _computer=null;
	private Image _phone=null;
	private Image _lan=null;
	private Image _audio_video=null;
	private Image _pheripherals=null;
	private Image _Imaging=null;
	private Image _weareable=null;
	private Image _toy=null;
	private Image _undefined=null;

	
	
	/**
	 * @param arg0
	 * @param arg1
	 */
	public BlueToothFinderFrontend(String arg0, int arg1) {
		super(arg0, arg1);
		this.init();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public BlueToothFinderFrontend(String arg0, int arg1, String[] arg2, Image[] arg3) {
		super(arg0, arg1, arg2, arg3);
		this.init();
	}
	
	
	public void init()
	{
		try {
			this._btImage=Image.createImage(this.getClass().getResourceAsStream("/resources/bt.jpeg"));//Image.createImage("people.png");
			
			this._miscellaneous=Image.createImage(this.getClass().getResourceAsStream("/resources/miscellaneous.jpeg"));
			this._computer=Image.createImage(this.getClass().getResourceAsStream("/resources/pc.jpeg"));
			this._phone=Image.createImage(this.getClass().getResourceAsStream("/resources/ipod.png"));
			this._lan=Image.createImage(this.getClass().getResourceAsStream("/resources/lan.jpeg"));
			this._audio_video=Image.createImage(this.getClass().getResourceAsStream("/resources/headset.jpeg"));
			this._pheripherals=Image.createImage(this.getClass().getResourceAsStream("/resources/peripheral.jpeg"));
			this._Imaging=Image.createImage(this.getClass().getResourceAsStream("/resources/imaging.jpeg"));
			this._weareable=Image.createImage(this.getClass().getResourceAsStream("/resources/biometric.jpeg"));
			this._toy=Image.createImage(this.getClass().getResourceAsStream("/resources/toy.jpeg"));
			this._undefined=Image.createImage(this.getClass().getResourceAsStream("/resources/question.jpeg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*public ContextHandler getBackend() {
		return ContextHandlerManager.getInstance().getContextHandlerByName(ContextHandlerManager.BT_FINDER);
	}*/

	public Displayable getBaseDisplayable() {
		return this;
	}

	public Command getFrontendCommand() {
		return this._command;
	}

	public CommandListener getDefaultCommandListener() {
		return this;
	}

	public void commandAction(Command arg0, Displayable arg1) {
		// TODO Auto-generated method stub
		
	}

	public int append(String arg0, Image arg1) {
		if(arg1==null)
		{
			arg1=this._btImage;
		}
		return super.append(arg0, arg1);
	}
	
	public int append(String label, javax.bluetooth.DeviceClass dev)
	{
		int type=dev.getMajorDeviceClass();
		
		Image img=null;
		switch(type)
		{
			case 0*256://Miscellaneous
			{
				img=this._miscellaneous;
				break;
			}
			case 1*256://Computer
			{
				img=this._computer;
				break;
			}
			case 2*256://phone
			{
				img=this._phone;
				break;
			}
			case 3*256://lan
			{
				img=this._lan;
				break;
			}
			case 4*256://Audio/video
			{
				img=this._audio_video;
				break;
			}
			case 5*256://Pheripherals
			{
				img=this._pheripherals;
				break;
			}case 6*256://Imaging
			{
				img=this._Imaging;
				break;
			}case 7*256://weareable
			{
				img=this._weareable;
				break;
			}case 8*256://toy
			{
				img=this._toy;
				break;
			}case (16+8+4+2+1)*256://undefined
			{
				img=this._undefined;
				break;
			}
		}
		return this.append(label, img);
	}
	
	

}
