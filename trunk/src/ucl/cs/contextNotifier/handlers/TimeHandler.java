/**
 * 
 */
package ucl.cs.contextNotifier.handlers;

import java.util.Date;
import java.util.Hashtable;
import java.util.Timer;
import java.util.Vector;
import javax.microedition.lcdui.Choice;

import ucl.cs.contextNotifier.ConstraintNode;
import ucl.cs.contextNotifier.ContextHandler;
import ucl.cs.contextNotifier.Frontend;
import ucl.cs.contextNotifier.FuzzyLogicBoolean;
import ucl.cs.contextNotifier.Rule;
import ucl.cs.contextNotifier.TimeNotification;
import ucl.cs.contextNotifier.TimerTarget;

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
public class TimeHandler extends ContextHandler implements TimerTarget {

	private TimeHandlerFrontend _frontend=new TimeHandlerFrontend("TimeHandlerFrontend",Choice.IMPLICIT);
	
	private Timer _innerTimer=new Timer();
	
	private Object _lock=new Object();
	
	private Vector _pendingNotification=new Vector();
	/**
	 * 
	 */
	public TimeHandler() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.ContextHandler#getFrontEnd()
	 */
	public Frontend getFrontEnd() {
		return this._frontend;
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.ContextHandler#startHandling()
	 */
	public void startHandling() {
		this.updateStatus();
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.ContextHandler#stopHandling()
	 */
	public void stopHandling() {
	}

	/**
	 * This method should not be invocathed. It just refresh the status of each node that should already be correct.
	 */
	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.ContextHandler#updateStatus()
	 */
	public synchronized void updateStatus() {
		for(int i=0;i<this._registeredConstraints.size();i++)
		{
			this.validateNode((ConstraintNode)this._registeredConstraints.elementAt(i));
		}
	}
	
	private void validateNode(ConstraintNode node)
	{
		long time=System.currentTimeMillis();
		long nodeTime=Long.parseLong(node.getValue())+node.getRule().getActivity().getDatetimeLoading();
		boolean flag=false;
		switch(node.getCheckType())
		{
			case ConstraintNode.EQUALS:
			case ConstraintNode.LESSER:
			case ConstraintNode.LESSER_EQUAL:
			{ 
				if(time<=nodeTime)
				{
					flag=true;
				}
				break;
			}
			case ConstraintNode.NOT_EQUALS:
			case ConstraintNode.GREATER:
			case ConstraintNode.GREATER_EQUAL:
			{ 
				if(time>=nodeTime)
				{
					flag=true;
				}
				break;
			}
		}
		FuzzyLogicBoolean bool=new FuzzyLogicBoolean();
		if(flag)
		{
			bool.setTrue();
		}else
		{
			bool.setFalse();
		}
		node.setActualValue(bool);
	}

	public void registerConstraint(ConstraintNode c) {
		super.registerConstraint(c);
		this.validateNode(c);
		
		String symbol="";
		switch(c.getCheckType())
		{
			case ConstraintNode.EQUALS:
			{ 
				symbol="=";
				break;
			}
			case ConstraintNode.LESSER:
			{ 
				symbol="=";
				break;
			}
			case ConstraintNode.LESSER_EQUAL:
			{ 
				symbol="<=";
				break;
			}
			case ConstraintNode.NOT_EQUALS:
			{ 
				symbol="!=";
				break;
			}
			case ConstraintNode.GREATER:
			{ 
				symbol=">";
				break;
			}
			case ConstraintNode.GREATER_EQUAL:
			{ 
				symbol=">=";
				break;
			}
		}
		String label=symbol+" "+(new Date(Long.parseLong(c.getValue())+c.getRule().getActivity().getDatetimeLoading()).toString());
		
		long l=Long.parseLong(c.getValue()) + c.getRule().getActivity().getDatetimeLoading() - System.currentTimeMillis();
		TimeNotification not=new TimeNotification(this,c);
		
		
		synchronized (this._lock) {
			this._frontend.append(label, null);
			this._pendingNotification.addElement(not);
			this._innerTimer.schedule(not, l);
		}
		
		System.out.println("TimeHandler.registerConstraint: "+c.getValue());
		
	}

	public synchronized void unregisterConstraint(ConstraintNode c) {
		super.unregisterConstraint(c);
		//retrieve TimeNotification
		//for(int i=0;i<this._pendingNotification.size();i++)
		
		synchronized (this._lock) {
			for(int i=this._pendingNotification.size()-1;i>-1;i--)
			{
				TimeNotification not=(TimeNotification)this._pendingNotification.elementAt(i);
				if(not!=null&&not.getSource().equals(c))
				{
					//this._innerTimer.removeTimeNotification(not);
					//java.util.timer does not allow to unschedule tasks
					this._frontend.delete(i);
					this._pendingNotification.removeElementAt(i);
					break;
				}
			}
		}
	}

	public synchronized void alert(Object source) {
		ConstraintNode c=(ConstraintNode)source;
		//remove the notification from the pending array
		//because java.util.timer does not support unscheduling of events we do it manually.
		synchronized (this._lock) {
			for(int i=0;i<this._pendingNotification.size();i++)
			{
				TimeNotification not=(TimeNotification)this._pendingNotification.elementAt(i);
				if(not.getSource().equals(c))
				{
					//this._innerTimer.removeTimeNotification(not);
					this.validateNode(c);
					//this._frontend.delete(i);
					//this._pendingNotification.removeElementAt(i);
					break;
				}
			}
		}
		
		
	}

	
	public static ConstraintNode loadRedifinedConstraintNode(Rule r, String value, int checkType, String contextHandlerName, Hashtable params) {
		return new ConstraintNode(r,value,checkType,contextHandlerName);
	}
}
