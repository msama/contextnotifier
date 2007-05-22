/**
 *
 */
package ucl.cs.contextNotifier;

/**
 * @author -Michele Sama- aka -RAX-
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
public class NotConstraintNode extends AbstractConstraintNode {

	
	/**
	 * @param r
	 */
	public NotConstraintNode(Rule r) {
		super(r);
	}


	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.AbstractConstraintNode#isLeaf()
	 */
	public boolean isLeaf() {
		return false;
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.AbstractConstraintNode#isTrue()
	 */
	public FuzzyLogicBoolean getLogicValue() {
		FuzzyLogicBoolean flag=new FuzzyLogicBoolean(); //starting value is NotValid
		if(this.innerNodes.size()==1)
		{
			AbstractConstraintNode node=(AbstractConstraintNode)this.innerNodes.elementAt(0);
			if(node.isDisposed())
			{
				this.dispose();
				flag.setNotValid();
			}else if(node.isActive()==false)
			{
				flag.setNotValid();
			}
			if(node.getLogicValue().isNotValid())
			{
				flag.setNotValid();
			}else if(node.getLogicValue().isTrue())
			{
				flag.setFalse();
			}else if(node.getLogicValue().isFalse())
			{
				flag.setTrue();
			}
		}else{
			//if a NotNode is empty or contains more than 1 child.
			flag.setNotValid();
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.AbstractConstraintNode#setActive(boolean)
	 */
	public void setActive(boolean _active) {
		this._active=_active;
		if(this.innerNodes.size()==1)
		{
			((AbstractConstraintNode)this.innerNodes.elementAt(0)).setActive(_active);
		}
	}
	
	
	public void setInnerNode(AbstractConstraintNode node)
	{
		if(this.innerNodes.size()>0)
		{
			((AbstractConstraintNode)this.innerNodes.elementAt(0)).dispose();
			this.innerNodes.removeAllElements();
		}
		if(node!=null){
			this.innerNodes.addElement(node);
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
