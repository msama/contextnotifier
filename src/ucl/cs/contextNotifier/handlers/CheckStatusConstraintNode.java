/**
 *
 */
package ucl.cs.contextNotifier.handlers;

import ucl.cs.contextNotifier.ConstraintNode;
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
public class CheckStatusConstraintNode extends ConstraintNode {

	private Rule _targetRule=null;
	private int attributeToCheck=-1;
	private boolean expectedValue=false;
	
	/**
	 * @param r
	 * @param value
	 * @param checkType
	 * @param contextHandlerName
	 * @param rule
	 * @param attributeToCheck
	 * @param expectedValue
	 */
	public CheckStatusConstraintNode(Rule r, String value, int checkType, String contextHandlerName, Rule rule, int attributeToCheck, boolean expectedValue) {
		super(r, value, checkType, contextHandlerName);
		_targetRule = rule;
		this.attributeToCheck = attributeToCheck;
		this.expectedValue = expectedValue;
	}

	/**
	 * @param expectedValue the expectedValue to set
	 */
	public void setExpectedValue(boolean expectedValue) {
		this.expectedValue = expectedValue;
	}

	/**
	 * @return the expectedValue
	 */
	public boolean isExpectedValue() {
		return expectedValue;
	}

	/**
	 * @param attributeToCheck the attributeToCheck to set
	 */
	public void setAttributeToCheck(int attributeToCheck) {
		this.attributeToCheck = attributeToCheck;
	}

	/**
	 * @return the attributeToCheck
	 */
	public int getAttributeToCheck() {
		return attributeToCheck;
	}

	/**
	 * @param _targetRule the _targetRule to set
	 */
	public void settargetRule(Rule _targetRule) {
		this._targetRule = _targetRule;
	}

	/**
	 * @return the _targetRule
	 */
	public Rule getTargetRule() {
		return _targetRule;
	}



}
