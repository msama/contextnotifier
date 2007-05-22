/**
 *
 */
package ucl.cs.contextNotifier.handlers;

import javax.microedition.location.Coordinates;
import javax.microedition.location.Location;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.ProximityListener;

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
public class GPSConstraintNode extends ConstraintNode {
	
	
	
	private Coordinates _coordinates=null;
	private float _radius=0;
	/**
	 * @param r
	 * @param value
	 * @param checkType
	 * @param contextHandlerName
	 */
	public GPSConstraintNode(Rule r, String value, int checkType, String contextHandlerName, Coordinates coordinates, float radius) {
		super(r, value, checkType, contextHandlerName);
		this._coordinates=coordinates;
		this._radius=radius;
	}
	/**
	 * @param _radius the _radius to set
	 */
	public void setRadius(float _radius) {
		this._radius = _radius;
	}
	/**
	 * @return the _radius
	 */
	public float getRadius() {
		return _radius;
	}
	/**
	 * @param _coordinates the _coordinates to set
	 */
	public void setVoordinates(Coordinates _coordinates) {
		this._coordinates = _coordinates;
	}
	/**
	 * @return the _coordinates
	 */
	public Coordinates getCoordinates() {
		return _coordinates;
	}

}
