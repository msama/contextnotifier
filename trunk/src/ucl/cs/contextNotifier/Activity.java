/*
 * Activity.java
 *
 * Created on January 10, 2007, 2:43 PM
 *
 * AdaptiveReminder
 * Developed by Michele Sama
 * Computer Science department, UCL
 */

package ucl.cs.contextNotifier;

import java.util.Vector;

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
public class Activity implements Disposable/*, TimerTarget*/{
	
	private boolean _disposed=false;
    
    private String _name=null;
    private String _description=null;
    private long _datetimeLoading=0;
    //private long _datetimeDisposing=0;
    
    public Vector rules=new Vector();
    
    /** Creates a new instance of Activity */
    public Activity(String name,String description, long datetime)
    {
        this.setName(name);
        this.setDescription(description);
        this.setDatetimeLoading(datetime);
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public long getDatetimeLoading() {
        return _datetimeLoading;
    }

    public void setDatetimeLoading(long datetime) {
        this._datetimeLoading = datetime;
    }

	public void notifyActivity(Rule triggeredRule, Notification notification) {
		DecisionEngine.getInstance().notifyActivity(this, triggeredRule, notification);
	}

	public void dispose() {
		this._disposed=true;
		for(int i=this.rules.size()-1; i>-1; i--)
		{
			Rule r=(Rule)this.rules.elementAt(i);
			
			r.dispose();
			this.rules.removeElement(r);
		}
		DecisionEngine.getInstance().getActivityFrontend().remove(this);
	}


	/**
	 * @return the _disposed
	 */
	public boolean isDisposed() {
		return _disposed;
	}
	
	
	public Rule getRuleByName(String name)
	{
		for(int i=0;i<this.rules.size();i++)
		{
			Rule r=(Rule)this.rules.elementAt(i);
			if(r.getName().equals(name))
			{
				return r;
			}
		}
		return null;
	}

	public void childDispose(Disposable child) {
		if(child instanceof Rule)
		{
			Rule r=(Rule)child;
			this.rules.removeElement(r);
			if(this.rules.size()==0)
			{
				this.dispose();
			}
		}else
		{
			throw new RuntimeException("Only Rules are supposed to notify the Activity of their disposed status.");
		}
	}

	/*
	public void alert(Object source) {
		//		because the actual implementation of the java.util.timer does not support unscheduling we must check if a rule has been disposed
		if(this.isDisposed())
		{
			return;
		}
		
		if(source instanceof Rule)
		{
			Rule r=(Rule)source;
			
				long time=System.currentTimeMillis();
				
				long activationTime=r.getActivationMsec()+this.getDatetimeLoading();
				long deactivationTime=r.getDeactivationMsec()+this.getDatetimeLoading();
				if(time>=activationTime&&time<=deactivationTime)
				{
					r.setActive(true);
				}else if(time>deactivationTime)
				{
					r.setActive(false);
				}else
				{
					throw new java.lang.RuntimeException("Activity.alert: "+time+" something goes wrong with Rule activation/deactivation ["+activationTime+"-"+deactivationTime+"].");
				}
		}
		
	}*/

	/*
	/**
	 * @param _datetimeDisposing the _datetimeDisposing to set
	 */
	/*
	public void setDatetimeDisposing(long _datetimeDisposing) {
		this._datetimeDisposing = _datetimeDisposing;
	}*/

	/*
	/**
	 * @return the _datetimeDisposing
	 */
	/*
	public long getDatetimeDisposing() {
		return _datetimeDisposing;
	}
    */
    
}
