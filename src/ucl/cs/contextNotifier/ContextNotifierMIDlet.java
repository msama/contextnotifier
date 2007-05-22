package ucl.cs.contextNotifier;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

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
public class ContextNotifierMIDlet extends MIDlet implements CommandListener, FrontendContainer{
	
	
	public final static String EXIT="Exit";
	
	
	private Vector _frontends=new Vector();
	private Vector _commands=new Vector();

	public ContextNotifierMIDlet() {
		//Creates new streams
		
		
		
		//register the MIDlet in the ContextHandlerManager
		ContextHandlerManager.getInstance().setCurrentMIDlet(this);
		
		//add default command to the vector
		Command c=new Command(ContextNotifierMIDlet.EXIT, ContextNotifierMIDlet.EXIT, Command.EXIT, 1);
		this._commands.addElement(c);
		
		//Add the ActivityList to the MIDlet
		ActivityFrontend list=DecisionEngine.getInstance().getActivityFrontend();
		this.addFrontend(list);
		Display.getDisplay(this).setCurrent(list);
		
	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean flag) throws MIDletStateChangeException {
		
	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		
		ContextHandlerManager.getInstance().setCurrentMIDlet(this);
		ActivityLoader.getInstance().setCurrentMIDlet(this);
		ActivityLoader.getInstance().loadActivities();
		/*Activity activity=new Activity("TestBlueTooth","Test activity for the BlueTooth ContextHandler.",new Date().getTime());
		Rule r=new Rule("Proximity with laptop.",activity,new Notification(NotifierFactory.VIBRATION_NOTIFIER,true));
		
		ConstraintNode node=new ConstraintNode(r,this.getAppProperty("BT_NAME"),ConstraintNode.EQUALS,ContextHandlerManager.getInstance().getContextHandlerByName(ContextHandlerManager.BT_FINDER));
		r.setConstraintRootNode(node);
		DecisionEngine.getInstance().addActivity(activity);
		*/
	}
	
	
	/**
	 * This method is used by the context manager in order to add or remove ContextHandlers frontends to the MIDlet
	 * @param f a Frontend whom needs to be added to the MIDlet
	 */
	public synchronized void addFrontend(Frontend f)
	{
		//set this midled as command listener
		f.getBaseDisplayable().setCommandListener(this);
		
		//add to this frontend all other available commands
		//add command for viewing this frontend to all other frontends
		Command command=f.getFrontendCommand();
		for(int i=0;i<this._frontends.size();i++)
		{
			Frontend frt=(Frontend)this._frontends.elementAt(i);
			f.getBaseDisplayable().addCommand(frt.getFrontendCommand());
			frt.getBaseDisplayable().addCommand(command);
		}
		
		//add this forntend to the vector
		this._frontends.addElement(f);
		
		//add all the external command
		for(int i=0;i<this._commands.size();i++)
		{
			Command c=(Command)this._commands.elementAt(i);
			f.getBaseDisplayable().addCommand(c);
		}
	}
	
	/**
	 * @param f a Frontend that needs to be removed from the MIDlet
	 * @return true if the Frontend has been removed, false if it was not already in the MIDlet
	 */
	public synchronized boolean removeFrontend(Frontend f)
	{
		
		
		//remove frontend from inner vector
		boolean b=this._frontends.removeElement(f);
		if(b==false){return false;}
		
		
		//remove commands
		Command command=f.getFrontendCommand();
		for(int i=0;i<this._frontends.size();i++)
		{
			Frontend frt=(Frontend)this._frontends.elementAt(i);
			f.getBaseDisplayable().removeCommand(frt.getFrontendCommand());
			frt.getBaseDisplayable().removeCommand(command);
		}
		
		//remove command listener
		f.getBaseDisplayable().setCommandListener(null);
		
		//remove all the external command
		for(int i=0;i<this._commands.size();i++)
		{
			Command c=(Command)this._commands.elementAt(i);
			f.getBaseDisplayable().removeCommand(c);
		}
		
		
		//if the current Frontend is on screen it must be removed
		Display disp=Display.getDisplay(this);
		System.out.println("AdaptiveReminderMidlet.removeFrontend on screen:"+disp.getCurrent()+" to remove "+f.getBaseDisplayable());
		if(disp.getCurrent().equals(f.getBaseDisplayable()))
		{
			System.out.println("AdaptiveReminderMidlet.removeFrontend frontend to remove is on screen.");
			if(this._frontends.size()>0)
			{
				Frontend frnd=(Frontend)this._frontends.elementAt(0);
				disp.setCurrent(frnd.getBaseDisplayable());
				System.out.println("AdaptiveReminderMidlet.removeFrontend now on screen "+frnd.getBaseDisplayable());
			}else
			{
				disp.setCurrent(null);
				System.out.println("AdaptiveReminderMidlet.removeFrontend now on screen null");
			}
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command cmd, Displayable dsp) {
		
		//default actions
		String action=cmd.getLabel();
		if(action.equals(ContextNotifierMIDlet.EXIT))
		{
			try {
				this.destroyApp(true);
			} catch (MIDletStateChangeException e) {
				e.printStackTrace();
			}
			notifyDestroyed();
			return;
		}
		
		//check if the events is one of the display-frontend ones
		for(int i=0;i<this._frontends.size();i++)
		{
			Frontend frt=(Frontend)this._frontends.elementAt(i);
			if(cmd.equals(frt.getFrontendCommand()))
			{
				Display.getDisplay(this).setCurrent(frt.getBaseDisplayable());
				return;
			}
		}
		//invoke the opportune listener
		for(int i=0;i<this._frontends.size();i++)
		{
			Frontend frt=(Frontend)this._frontends.elementAt(i);
			if(dsp.equals(frt.getBaseDisplayable()))
			{
				CommandListener lst=frt.getDefaultCommandListener();
				lst.commandAction(cmd, dsp);
				return;
			}
		}
	}
	
	
	

}
