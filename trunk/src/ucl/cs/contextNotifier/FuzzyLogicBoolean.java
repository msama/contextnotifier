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
public class FuzzyLogicBoolean {

	//TODO the same implementation can be obtained with 2 boolean using logical opertators......
	private boolean _true=false;
	private boolean _false=false;
	private boolean _notvalid=true;
	
	
	/**
	 * @param _true the _true to set
	 */
	public void setTrue() {
		this._true = true;
		this._false=false;
		this._notvalid=false;
	}
	/**
	 * @return the _true
	 */
	public boolean isTrue() {
		return _true;
	}
	/**
	 * @param _false the _false to set
	 */
	public void setFalse() {
		this._false = true;
		this._true=false;
		this._notvalid=false;
	}
	/**
	 * @return the _false
	 */
	public boolean isFalse() {
		return _false;
	}
	/**
	 * @param _valid the _valid to set
	 */
	public void setNotValid() {
		this._notvalid=true;
		this._false=false;
		this._true=false;
	}
	/**
	 * @return the _valid
	 */
	public boolean isNotValid() {
		return _notvalid;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj instanceof FuzzyLogicBoolean)
		{
			FuzzyLogicBoolean bool=(FuzzyLogicBoolean)obj;
			if(this.isNotValid()==bool.isNotValid()&&this.isTrue()==bool.isTrue()&&this.isFalse()==bool.isFalse())
			{
				return true;
			}else
			{
				return false;
			}
		}else{
			return false;
		}
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if(this.isNotValid())
		{
			return "NotValid";
		}else if(this.isFalse())
		{
			return "False";
		}else
		{
			return "True";
		}
	}
	
	
	
}
