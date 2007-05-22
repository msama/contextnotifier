package ucl.cs.contextNotifier;


//import java.util.Timer;
import java.util.Vector;

import javax.microedition.lcdui.Choice;


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
public class DecisionEngine /*implements TimerTarget*/{
	
	private static DecisionEngine _INSTANCE=null;
	
	private ActivityFrontend _activityList=new ActivityFrontend("Activity list",Choice.IMPLICIT);
	
	//All the loaded activities
	private Vector _currentActivity=new Vector();
	
	//All the loaded Rules both active and inactive
	private Vector _currentRules=new Vector();
	
	//private Timer _innerTimer=new Timer();
	
	protected DecisionEngine()
	{
	}
	
	public static DecisionEngine getInstance()
	{
		if(DecisionEngine._INSTANCE==null)
		{
			DecisionEngine._INSTANCE=new DecisionEngine();
		}
		return DecisionEngine._INSTANCE;
	}
	
	public void notifyActivity(Activity activity, Rule triggeredRule, Notification notification)
	{
		Notifier notifier=NotifierFactory.getNotifierByName(notification.getNotifierName());
		notifier.setActiveMIDlet(ContextHandlerManager.getInstance().getCurrentMIDlet());
		notifier.notifyActivity(activity, triggeredRule, notification);
		if(notification.isRemoveActivity())
		{
			System.out.println("DecisionEngine.notifyActivity remove activity");
			this.removeActivity(activity);
		}else if(notification.isRemoveRule())
		{
			System.out.println("DecisionEngine.notifyActivity remove rule");
			activity.rules.removeElement(triggeredRule);
			this._currentRules.removeElement(triggeredRule);
			triggeredRule.dispose();
		}
	}


	public ActivityFrontend getActivityFrontend()
	{
		return this._activityList;
	}
	
	public void addActivity(Activity act)
	{
		int k=0;
		String newName=act.getName(); 
		while(this.getActivityByName(newName)!=null)
		{
			k++;
			newName=act.getName()+"("+k+")";
		}
		act.setName(newName);
		
		
		this._currentActivity.addElement(act);
		this._activityList.append(act.getName(),null);
		
		//schedulo l'attivita' di modo che venga rimossa una volta scaduta
		//TimeNotification not=new TimeNotification(this,act);
		//this._innerTimer.schedule(not, act.getDatetimeDisposing());
		/*
		long time=System.currentTimeMillis();
		long activationTime;
		long deactivationTime;
		*/
		for(int i=0;i<act.rules.size();i++)
		{
			Rule r=(Rule)act.rules.elementAt(i);
			this._currentRules.addElement(r);
			this._activityList.append(r);
			/*
			activationTime=r.getActivationMsec()+act.getDatetimeLoading();
			deactivationTime=r.getDeactivationMsec()+act.getDatetimeLoading();
			if(time>=activationTime&&time<=deactivationTime)
			{
				//if a rule can be activated it is started directly
				r.setActive(true);
				System.out.println("DecisionEngine.addActivity rule activated.");
			}else if(time<activationTime)
			{
				//if it is to early for a rule it is scheduled
				not=new TimeNotification(act,r);
				this._innerTimer.schedule(not, activationTime-time);
				System.out.println("DecisionEngine.addActivity rule scheduled after: "+(activationTime-time)+" msec.");
			}
			*/
			r.checkActualStatus();
		}
	}
	
	public boolean removeActivity(Activity act)
	{
		boolean b=this._currentActivity.removeElement(act);
		/*for(int i=0;i<act.rules.size();i++)
		{
			Rule r=(Rule)act.rules.elementAt(i);
			this._currentRules.removeElement(r);
		}*/
		
		//dispose of the activity
		act.dispose();
		
		if(b==true)
		{
			//remove from the frontend
			this._activityList.remove(act);
			
			//remove from the _innerTimer if still present
			//The implementation of the java.utilTimer does not allows to unschedule events
			//this._innerTimer.removeAllNotificationForSource(act);
			//this._innerTimer.removeAllNotificationForTarget(act);
		}
		return b;
	}
	
	public Activity getActivityByName(String name)
	{
		if(name==null)
		{
			return null;
		}
		for(int i=0;i<this._currentActivity.size();i++)
		{
			Activity act=(Activity)this._currentActivity.elementAt(i);
			if(act.getName().endsWith(name))
			{
				return act;
			}
		}
		return null;
	}
	
	
	public Rule getRuleByName(String name)
	{
		if(name==null)
		{
			return null;
		}
		for(int i=0;i<this._currentRules.size();i++)
		{
			Rule r=(Rule)this._currentRules.elementAt(i);
			if(r.getName().endsWith(name))
			{
				return r;
			}
		}
		return null;
	}
	
	public boolean removeRule(Rule rule)
	{
		boolean b=this._currentRules.removeElement(rule);
		this._activityList.remove(rule);
		rule.dispose();
		return b;
	}

	/*
	public void alert(Object source) {
		if(source instanceof Activity)
		{
			Activity act=(Activity)source;
			if(act.isDisposed()==false){
				this.removeActivity((Activity)source);
			}
		}
	}*/
	
}
