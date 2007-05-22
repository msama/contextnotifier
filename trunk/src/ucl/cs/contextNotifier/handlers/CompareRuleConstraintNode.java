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
public class CompareRuleConstraintNode extends ConstraintNode {

	private Rule _targetRuleA=null;
	private Rule _targetRuleB=null;
	private int _attributeToCheck=-1;
	private int _constraint=-1;
	
	/**
	 * @param r
	 * @param value
	 * @param checkType
	 * @param contextHandlerName
	 * @param ruleA
	 * @param ruleB
	 * @param attributeToCheck
	 * @param constraint
	 */
	public CompareRuleConstraintNode(Rule r, String value, int checkType, String contextHandlerName, Rule ruleA, Rule ruleB, int attributeToCheck, int constraint) {
		super(r, value, checkType, contextHandlerName);
		_targetRuleA = ruleA;
		_targetRuleB = ruleB;
		this._attributeToCheck = attributeToCheck;
		this._constraint = constraint;
	}

	/**
	 * @param _targetRuleA the _targetRuleA to set
	 */
	public void setTargetRuleA(Rule _targetRuleA) {
		this._targetRuleA = _targetRuleA;
	}

	/**
	 * @return the _targetRuleA
	 */
	public Rule getTargetRuleA() {
		return _targetRuleA;
	}

	/**
	 * @param _targetRuleB the _targetRuleB to set
	 */
	public void setTargetRuleB(Rule _targetRuleB) {
		this._targetRuleB = _targetRuleB;
	}

	/**
	 * @return the _targetRuleB
	 */
	public Rule getTargetRuleB() {
		return _targetRuleB;
	}

	/**
	 * @param _attributeToCheck the _attributeToCheck to set
	 */
	public void setAttributeToCheck(int _attributeToCheck) {
		this._attributeToCheck = _attributeToCheck;
	}

	/**
	 * @return the _attributeToCheck
	 */
	public int getAttributeToCheck() {
		return _attributeToCheck;
	}
	
}
