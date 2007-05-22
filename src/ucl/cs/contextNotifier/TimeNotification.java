package ucl.cs.contextNotifier;

import java.util.TimerTask;
/**
 * 
 */

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
public class TimeNotification extends TimerTask{

	//private long _time=0;
	private TimerTarget _target=null;
	private Object _source=null;
	

	
	/**
	 * @param _time
	 * @param _target
	 * @param _source
	 */
	public TimeNotification(TimerTarget _target, Object _source) {
		super();
		this._target = _target;
		this._source = _source;
	}
	
	
	/**
	 * @param _target the _target to set
	 */
	public void setTarget(TimerTarget _target) {
		this._target = _target;
	}
	
	/**
	 * @return the _target
	 */
	public TimerTarget getTarget() {
		return _target;
	}

	/**
	 * @param _source the _source to set
	 */
	public void setSource(Object _source) {
		this._source = _source;
	}

	/**
	 * @return the _source
	 */
	public Object getSource() {
		return _source;
	}


	public void run() {
		this._target.alert(this._source);
	}
}
