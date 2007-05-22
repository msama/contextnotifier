/*
 * ConstraintNode.java
 *
 * Created on January 9, 2007, 3:39 PM
 *
 * AdaptiveReminder
 * Developed by Michele Sama
 * Computer Science department, UCL
 */

package ucl.cs.contextNotifier;

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
public class ConstraintNode extends AbstractConstraintNode{
    public final static int EQUALS=0;
    public final static int NOT_EQUALS=1;
    public final static int LESSER=2;
    public final static int GREATER=3;
    public final static int LESSER_EQUAL=4;
    public final static int GREATER_EQUAL=5;
    
    
    private String _value=null;
    private int _checkType=-1;
    private FuzzyLogicBoolean _actualValue=new FuzzyLogicBoolean();
    
    private ContextHandler _contextHandler=null;
    private String _contextHandlerName=null;

    
    /**
     * Creates a new instance of ConstraintNode
     */
    public ConstraintNode(Rule r,String value, int checkType, String contextHandlerName) {
        super(r);
        this._value=value;
        this._checkType=checkType;
        //this._contextHandler=hnd;
        this._contextHandlerName=contextHandlerName;
    }

    public String getValue() {
        return _value;
    }

    public int getCheckType() {
        return _checkType;
    }


    public boolean setActualValue(FuzzyLogicBoolean actualValue) 
    {
    	FuzzyLogicBoolean old=this._actualValue;
        this._actualValue = actualValue;
        //if the value is changed thensupernode must be notified
        if(old.equals(this._actualValue)==false)
        {
        	//this.refreshLogicalValue();
            this.getRule().checkActualStatus();
        	return true;
        }else{
        	return false;
        }
    }

    public FuzzyLogicBoolean getLogicValue() {
        return this._actualValue;
    }
    
    public boolean isLeaf() {
        return true;
    }

    public ContextHandler getContextHandler() {
        return _contextHandler;
    }

    public void setContextHandler(ContextHandler contextHandler) {
        this._contextHandler = contextHandler;
    }

    /*
    public void dispose() {
    	this.setActive(false);
    	if(this._contextHandler!=null){
    		this._contextHandler.unregisterConstraint(this);
    		this._contextHandler=null;
    	}
    }*/

	public void setActive(boolean _active) {
		if(this._active==_active)
		{
			//nothing to do!
			return;
		}
		//boolean old=this._active;
		this._active=_active;
		if(/*old==false&&*/this._active==true&&this.isDisposed()==false)
		{
			
			if(this._contextHandler==null)
			{
				this._contextHandler=ContextHandlerManager.getInstance().getContextHandlerByName(this._contextHandlerName);
				if(this._contextHandler==null)
				{
					this.dispose();
				}
			}
			//System.out.println("ConstraintNode.setActive registering constraint to: "+this._contextHandler);
			if(this._contextHandler!=null){
				this._contextHandler.registerConstraint(this);
			}
		}else //if(old==true&&this._active==false)
		{
			if(this._contextHandler!=null)
			{
				//System.out.println("ConstraintNode.setActive UNregistering constraint to: "+this._contextHandler);
				this._contextHandler.unregisterConstraint(this);
				this._contextHandler=null;
			}
		}
	}
	
	public void childDispose(Disposable child) {
		throw new RuntimeException("A ConstraintNode is supposed to be a leaf!");
	}

    
}
