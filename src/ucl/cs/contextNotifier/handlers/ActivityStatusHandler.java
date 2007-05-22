/**
 *
 */
package ucl.cs.contextNotifier.handlers;

import java.util.Hashtable;

import ucl.cs.contextNotifier.Activity;
import ucl.cs.contextNotifier.ConstraintNode;
import ucl.cs.contextNotifier.ContextHandler;
import ucl.cs.contextNotifier.ContextHandlerManager;
import ucl.cs.contextNotifier.Frontend;
import ucl.cs.contextNotifier.FuzzyLogicBoolean;
import ucl.cs.contextNotifier.Rule;

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
public class ActivityStatusHandler extends ContextHandler {

	private ActivityStatusFrontend _frontend=new ActivityStatusFrontend();
	public final static int DISPOSED=2;
	public final static int ACTIVE=1;
	public final static int VALID=0;
	
	
	/**
	 * 
	 */
	public ActivityStatusHandler() {
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
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.ContextHandler#updateStatus()
	 */
	public void updateStatus() {
		for(int i=0;i<this._registeredConstraints.size();i++)
		{
			ConstraintNode cn=(ConstraintNode)this._registeredConstraints.elementAt(i);
			if(cn instanceof CheckStatusConstraintNode)
			{
				this.updateCheckStatusConstraintNode((CheckStatusConstraintNode)cn);
			}else if(cn instanceof CompareRuleConstraintNode)
			{
				this.updateCompareRuleConstraintNode((CompareRuleConstraintNode)cn);
			}else{		
				this.updateConstraintNode(cn);
			}
			
		}
	}
	
	
    private void updateCompareRuleConstraintNode(CompareRuleConstraintNode node) {
    	FuzzyLogicBoolean a=new FuzzyLogicBoolean();
    	FuzzyLogicBoolean b=new FuzzyLogicBoolean();
    	
		switch(node.getAttributeToCheck())
		{
			case ActivityStatusHandler.ACTIVE:
			{
				if(node.getTargetRuleA().isActive())
				{
					a.setTrue();
				}else
				{
					a.setFalse();
				}
				if(node.getTargetRuleB().isActive())
				{
					b.setTrue();
				}else
				{
					b.setFalse();
				}
				break;
			}
			case ActivityStatusHandler.DISPOSED:
			{
				if(node.getTargetRuleA().isDisposed())
				{
					a.setTrue();
				}else
				{
					a.setFalse();
				}
				if(node.getTargetRuleB().isDisposed())
				{
					b.setTrue();
				}else
				{
					b.setFalse();
				}
				break;
			}
			case ActivityStatusHandler.VALID:
			{
				a=node.getTargetRuleA().isActualValue();
				b=node.getTargetRuleB().isActualValue();
				break;
			}
		}
		
		FuzzyLogicBoolean value=new FuzzyLogicBoolean();
		switch(node.getCheckType())
		{
			//EQUALS means that the remote rule value is equals to true
			case ConstraintNode.EQUALS:
			{ 
				if(a.isNotValid()||b.isNotValid())
				{
					value.isNotValid();
				}else if(a.equals(b))
				{
					value.setTrue();
				}else
				{
					value.setFalse();
				}
				break;
			}
			//NOT_EQUALS means that the remote rule value is not equals to true
			case ConstraintNode.NOT_EQUALS:
			{ 
				if(a.isNotValid()||b.isNotValid())
				{
					value.isNotValid();
				}else if(!a.equals(b))
				{
					value.setTrue();
				}else
				{
					value.setFalse();
				}
				break;
			}
			//LESSER means that the other action is not active or disposed or not existent
			case ConstraintNode.LESSER:
			case ConstraintNode.LESSER_EQUAL:
			{ 
				if(a.isNotValid()||b.isNotValid())
				{
					value.isNotValid();
				}else if(a.isFalse()&&b.isTrue())
				{
					value.setTrue();
				}else
				{
					value.setFalse();
				}
				break;
			}
			//GREATER means that the other action is active
			case ConstraintNode.GREATER:
			case ConstraintNode.GREATER_EQUAL:
			{ 
				if(a.isNotValid()||b.isNotValid())
				{
					value.isNotValid();
				}else if(a.isTrue()&&b.isFalse())
				{
					value.setTrue();
				}else
				{
					value.setFalse();
				}
				break;
			}
		}
		
		node.setActualValue(value);
		
	}

	private void updateCheckStatusConstraintNode(CheckStatusConstraintNode node) {
		switch(node.getAttributeToCheck())
		{
			case ActivityStatusHandler.ACTIVE:
			{
				FuzzyLogicBoolean bool=new FuzzyLogicBoolean();
				if(node.getTargetRule().isActive()==node.isExpectedValue())
				{
					bool.setTrue();
				}else
				{
					bool.setFalse();
				}
				node.setActualValue(bool);
				break;
			}
			case ActivityStatusHandler.DISPOSED:
			{
				FuzzyLogicBoolean bool=new FuzzyLogicBoolean();
				if(node.getTargetRule().isDisposed()==node.isExpectedValue())
				{
					bool.setTrue();
				}else
				{
					bool.setFalse();
				}
				node.setActualValue(bool);
				break;
			}
			case ActivityStatusHandler.VALID:
			{
				FuzzyLogicBoolean bool=new FuzzyLogicBoolean();
				if(node.getTargetRule().isActualValue().isNotValid())
				{
					bool.setNotValid();
				}else if(node.getTargetRule().isActualValue().isTrue()==node.isExpectedValue()) //Node is not NotValid so if true is false then false is true (this make sense!)
				{
					bool.setTrue();
				}else
				{
					bool.setFalse();
				}
				node.setActualValue(bool);
				//node.setActualValue(  node.getTargetRule().isActualValue()==node.isExpectedValue() );
				break;
			}
		}
		
	}

	private void updateConstraintNode(ConstraintNode cn) {
    	Activity act=cn.getRule().getActivity();
		Rule target=act.getRuleByName(cn.getValue());
		
		FuzzyLogicBoolean value=new FuzzyLogicBoolean();
		if(target==null||target.isActualValue().isNotValid())
		{
			value.setNotValid();
		}else{
			switch(cn.getCheckType())
			{
				//EQUALS means that the remote rule value is equals to true
				case ConstraintNode.EQUALS:
				{ 
					if(target.isActualValue().isTrue())
					{
						value.setTrue();
					}
					else
					{
						value.setFalse();
					}
					break;
				}
				//NOT_EQUALS means that the remote rule value is not equals to true
				case ConstraintNode.NOT_EQUALS:
				{ 
					if(target.isActualValue().isFalse())
					{
						value.setTrue();
					}
					else
					{
						value.setFalse();
					}
					break;
				}
				//LESSER means that the other action is not active or disposed but existent
				case ConstraintNode.LESSER:
				case ConstraintNode.LESSER_EQUAL:
				{ 
					if(target.isActive()==false)
					{
						value.setTrue();
					}
					else
					{
						value.setFalse();
					}
					break;
				}
				//GREATER means that the other action is active
				case ConstraintNode.GREATER:
				case ConstraintNode.GREATER_EQUAL:
				{ 
					if(target.isActive()==true)
					{		
						value.setTrue();
					}
					else
					{
						value.setFalse();
					}
					break;
				}
				default:
				{
					value.setNotValid();
				}
			}
		}
		cn.setActualValue(value);
	}

	public static ConstraintNode loadRedifinedConstraintNode(Rule r,String value, int checkType, String contextHandlerName, Hashtable params)
    {
    	String actionType=(String)params.get("ActionType");//CheckStatus | Compare
    	if(actionType.equals("CheckStatus"))
    	{
    		String targetRule=(String)params.get("TargetRule");
    		String attributeToCheck=(String)params.get("AttributeToCheck");
    		String expectedValue=(String)params.get("ExpectedValue");
    		
    		Activity act=r.getActivity();
    		Rule target=act.getRuleByName(targetRule);
    		
    		int attribute=-1;
    		if(attributeToCheck.equals("Valid"))
    		{
    			attribute=ActivityStatusHandler.VALID;
    		}else if(attributeToCheck.equals("Disposed"))
    		{
    			attribute=ActivityStatusHandler.DISPOSED;
    		}else if(attributeToCheck.equals("Active"))
    		{
    			attribute=ActivityStatusHandler.ACTIVE;
    		}
    		
    		boolean expected=expectedValue.equalsIgnoreCase("true");
    		/*if(target==null)
    		{
    			throw new RuntimeException("TargetRule param missing in redefined ConstraintNode");
    		}*/
    		
    		return new CheckStatusConstraintNode(r, value, checkType, contextHandlerName, target, attribute, expected);
    	}else if(actionType.equals("Compare"))
    	{
    		String targetRuleA=(String)params.get("TargetRuleA");
    		String targetRuleB=(String)params.get("TargetRuleB");
    		String attributeToCheck=(String)params.get("AttributeToCheck");
    		String constraint=(String)params.get("Constraint"); //as a normal constrain
    		

    		Activity act=r.getActivity();
    		Rule targetA=act.getRuleByName(targetRuleA);
    		Rule targetB=act.getRuleByName(targetRuleB);

    		
       		int attribute=-1;
    		if(attributeToCheck.equals("Valid"))
    		{
    			attribute=ActivityStatusHandler.VALID;
    		}else if(attributeToCheck.equals("Disposed"))
    		{
    			attribute=ActivityStatusHandler.DISPOSED;
    		}else if(attributeToCheck.equals("Active"))
    		{
    			attribute=ActivityStatusHandler.ACTIVE;
    		}
    		
    		int k=0;
    		if(constraint.equals("Equals"))
    		{
    			k=ConstraintNode.EQUALS;
    		}else if(constraint.equals("NotEquals"))
    		{
    			k=ConstraintNode.NOT_EQUALS;
    		}else if(constraint.equals("Greater"))
    		{
    			k=ConstraintNode.GREATER;
    		}else if(constraint.equals("GreaterEqual"))
    		{
    			k=ConstraintNode.GREATER_EQUAL;
    		}else if(constraint.equals("Lesser"))
    		{
    			k=ConstraintNode.LESSER;
    		}else if(constraint.equals("LesserEqual"))
    		{
    			k=ConstraintNode.LESSER_EQUAL;
    		}
    		
    		return new CompareRuleConstraintNode(r, value, checkType, contextHandlerName, targetA, targetB, attribute, k);
    	}else{
    	
    		return null;
    	}
    	//return new ConstraintNode(r,value,checkType,contextHandlerName);
    }

}
