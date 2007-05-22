/**
 * 
 */
package ucl.cs.contextNotifier;

import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;

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
public class ActivityFrontend extends List implements Frontend, CommandListener{

	private Command _command=new Command("Activities","Frontend that list current activities.",Command.ITEM,1);
	private Command _removeActivity=new Command("RemoveActivity", "Remove the currently selected Activity", Command.ITEM, 1);
	
	private Image _activityImage=null;
	private Image _ruleImage=null;
	private Image _ruleInactiveImage=null;
	
	/**
	 * @param arg0
	 * @param arg1
	 */
	public ActivityFrontend(String arg0, int arg1) {
		super(arg0, arg1);
		this.init();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public ActivityFrontend(String arg0, int arg1, String[] arg2, Image[] arg3) {
		super(arg0, arg1, arg2, arg3);
		this.init();
	}
	
	private void init()
	{
		try {
			this._activityImage=Image.createImage(this.getClass().getResourceAsStream("/resources/activity.png"));
			//_activityImage=Image.createImage("note.png");
			this._ruleImage=Image.createImage(this.getClass().getResourceAsStream("/resources/rule.png"));
			this._ruleInactiveImage=Image.createImage(this.getClass().getResourceAsStream("/resources/rule_sleeping.png"));
			
			this.addCommand(this._removeActivity);
		} catch (IOException e) {
			System.out.println("Stream "+e.getMessage());
			e.printStackTrace();
		}
	}

	public Displayable getBaseDisplayable() {
		return this;
	}

	public CommandListener getDefaultCommandListener() {
		return this;
	}

	public Command getFrontendCommand() {
		return this._command;
	}

	public void commandAction(Command cmd, Displayable dsp) {
		//System.out.println("ActivityFrontend.commandAction");
		if(cmd==this._removeActivity)
		{
			/*
			if(super.getImage(super.getSelectedIndex())!=this._activityImage)
			{
				//not an activity
				System.out.println("ActivityFrontend.commandAction: not an activity");
				return;
			}*/
			String name=super.getString(super.getSelectedIndex());
			if(name==null)
			{
				//somehow null
				System.out.println("ActivityFrontend.commandAction: somehow null");
				return;
			}
			Activity act=DecisionEngine.getInstance().getActivityByName(name);
			act.dispose();
		}
	}

	public int append(String arg0, Image arg1) {
		if(arg1==null)
		{
			arg1=this._activityImage;
		}
		return super.append(arg0, arg1);
	}

	public int append(Activity act)
	{
		return this.append(act.getName(),null);
	}
	
	public int append(Rule r)
	{
		String s=r.getActivity().getName();
		for(int i=0;i<this.size();i++)
		{
			if(s.equals(this.getString(i)))
			{
				if(r.isActive()){
					this.insert(i+1, r.getName(), this._ruleImage);
				}else
				{
					this.insert(i+1, r.getName(), this._ruleInactiveImage);
				}
				return i;
			}
		}
		return -1;
	}

	
	public void remove(Activity act)
	{
		for(int i=this.size()-1; i>-1; i--)
		{
			if(this.getString(i).equals(act.getName()))
			{
				this.delete(i);
				break;
			}
		}
	}
	
	public void remove(Rule r)
	{
		for(int i=this.size()-1; i>-1; i--)
		{
			if(this.getString(i).equals(r.getName()))
			{
				this.delete(i);
				break;
			}
		}
	}
}
