/*
 * AbstractConstraintNode.java
 *
 * Created on January 9, 2007, 6:23 PM
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
public abstract class AbstractConstraintNode implements Disposable{
    
    protected boolean _root=false;
    protected boolean _active=false;
    protected boolean _disposed=false;
    //AbstractConstraintNode childNodes=null;
    public Vector innerNodes=new Vector();
    protected AbstractConstraintNode _parent=null;
    protected Rule _rule=null;
    
    /**
     * Creates a new instance of AbstractConstraintNode
     */
    public AbstractConstraintNode(Rule r) {
        this._rule=r;
    }
    
    public abstract FuzzyLogicBoolean getLogicValue();
   
    void setRoot(boolean root)
    {
    	this._root=root;
    	if(this._root==true&&this.getParent()!=null)
    	{
    		throw new java.lang.IllegalArgumentException("A root node must have null _parent.");
    	}
    }
    
    public boolean isRoot()
    {
        return this._root;
    }
    
    public abstract boolean isLeaf();     

    public Rule getRule() {
        return _rule;
    }

    void setRule(Rule rule) {
        this._rule = rule;
    }
    
    public final void dispose()
    {
    	if(this._disposed==true)
    	{
    		return;
    	}
    	this.setActive(false);
    	this._disposed=true;
    	if(this._root==false)
    	{
    		this._parent.childDispose(this);
    	}else
    	{
    		this._rule.dispose();
    	}
    }
    
	public boolean isDisposed() {
		return this._disposed;
	}

	/**
	 * @param _active the _active to set
	 */
	public abstract void setActive(boolean _active);

	/**
	 * @return the _active
	 */
	public boolean isActive() {
		return _active;
	}

	/**
	 * @param _parent the _parent to set
	 */
	void setParent(AbstractConstraintNode _parent) {
		this._parent = _parent;
		if(this._parent==null)
		{
			this.setRoot(true);
		}
	}

	/**
	 * @return the _parent
	 */
	public AbstractConstraintNode getParent() {
		return _parent;
	}

    
}
