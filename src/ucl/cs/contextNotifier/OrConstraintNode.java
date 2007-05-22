/*
 * OrConstraintNode.java
 *
 * Created on January 9, 2007, 7:06 PM
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
public class OrConstraintNode extends AbstractConstraintNode {
    
    /** Creates a new instance of OrConstraintNode */
    public OrConstraintNode(Rule r) {
        super(r);
    }

    public FuzzyLogicBoolean getLogicValue() {
    	FuzzyLogicBoolean flag=new FuzzyLogicBoolean(); //starting value is NotValid
        if(this.innerNodes.size()>0){
            for(int i=0;i<this.innerNodes.size();i++)
            {
                AbstractConstraintNode node=(AbstractConstraintNode)this.innerNodes.elementAt(i);
                if(node.isDisposed()==false&&node.isActive()==true){
                	if(node.getLogicValue().isTrue())
                	{
                		flag.setTrue();
                		break;
                	}else if(node.getLogicValue().isFalse())
                	{
                		flag.setFalse();
                	}else if(node.getLogicValue().isNotValid())
                	{
                		//nothing to do.
                	}
                }
            }
        }else
        {
        	//if node is empty is value is NotValid
            flag.setNotValid();
        }
        
        return flag;
    }
    
    public boolean isLeaf() {
        return false;
    }


	public void setActive(boolean _active) {
		this._active=_active;
        for(int i=0;i<this.innerNodes.size();i++)
        {
            AbstractConstraintNode node=(AbstractConstraintNode)this.innerNodes.elementAt(i);
            node.setActive(_active);
        }
	}

	public void childDispose(Disposable child) {
		this.innerNodes.removeElement(child);
		if(this.innerNodes.size()==0)
		{
			this.dispose();
		}
	}
}
