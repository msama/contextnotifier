/*
 * ContextHandler.java
 *
 * Created on January 10, 2007, 1:37 PM
 *
 * AdaptiveReminder
 * Developed by Michele Sama
 * Computer Science department, UCL
 */

package ucl.cs.contextNotifier;

import java.util.Hashtable;
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
public abstract class ContextHandler {
    
    
    protected Vector _registeredConstraints=new Vector();
    
    
    /** Creates a new instance of ContextHandler */
    public ContextHandler() {
    }
    
    public abstract void startHandling();
    
    public abstract void stopHandling();
    
    public synchronized void registerConstraint(ConstraintNode c)
    {
    	//System.out.print(this+" Registering "+c);
    	//System.out.print("["+this._registeredConstraints.size());
    	this._registeredConstraints.addElement(c);
        //System.out.println("/"+this._registeredConstraints.size()+"]");
    }
    
    public synchronized void unregisterConstraint(ConstraintNode c)
    {
    	//System.out.print(this+" UNregistering "+c);
    	//System.out.print("["+this._registeredConstraints.size());
        this._registeredConstraints.removeElement(c);
        //System.out.println("/"+this._registeredConstraints.size()+"]");
        
        if(this._registeredConstraints.size()==0)
        {
        	//throw new java.lang.RuntimeException("ContextHandler.unregisterConstraint let's have a look at the stack.");
            this.stopHandling();
            ContextHandlerManager.getInstance().notifyAsUnused(this);
        }
    }
    
    public abstract void updateStatus();
    
    public abstract Frontend getFrontEnd();
    
    public static ConstraintNode loadRedifinedConstraintNode(Rule r,String value, int checkType, String contextHandlerName, Hashtable params)
    {
    	return new ConstraintNode(r,value,checkType,contextHandlerName);
    }
    
  
}
