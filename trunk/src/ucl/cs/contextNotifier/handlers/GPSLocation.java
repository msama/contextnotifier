/**
 *
 */
package ucl.cs.contextNotifier.handlers;

import java.util.Hashtable;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.location.Coordinates;
import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.QualifiedCoordinates;

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

/**
 * Temporary implementation for LOCATION HANDLER
 * */
public class GPSLocation extends ContextHandler implements LocationListener{

	protected Criteria _criteria=new Criteria();
	protected LocationProvider _locationProvider=null;
	protected Location _currentLocation=null;
	
	protected int _interval=-1; //default
	protected int _timeout=-1; //default
	protected int _maxAge=-1; //default
	
	protected GPSLocationFrontend _frontend=new GPSLocationFrontend("GPSLocation"/*,Choice.IMPLICIT*/);
	
	/**
	 * 
	 */
	public GPSLocation() {
		this._criteria.setAddressInfoRequired(false);
		this._criteria.setAltitudeRequired(true);
		this._criteria.setCostAllowed(true);
		this._criteria.setHorizontalAccuracy(Criteria.NO_REQUIREMENT);
		this._criteria.setPreferredPowerConsumption(Criteria.NO_REQUIREMENT);
		this._criteria.setPreferredResponseTime(Criteria.NO_REQUIREMENT);
		this._criteria.setSpeedAndCourseRequired(false);
		this._criteria.setVerticalAccuracy(Criteria.NO_REQUIREMENT);
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
		try {
			this._locationProvider=LocationProvider.getInstance(this._criteria);
		} catch (LocationException e) {
			Alert alert=new Alert("LocationProvider",e.getMessage(),null,AlertType.ERROR);
			alert.setTimeout(Alert.FOREVER);
			Display.getDisplay(ContextHandlerManager.getInstance().getCurrentMIDlet()).setCurrent(alert, Display.getDisplay(ContextHandlerManager.getInstance().getCurrentMIDlet()).getCurrent());
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
		if(this._locationProvider==null)
		{
			Alert alert=new Alert("LocationProvider","LocationProvider is null!",null,AlertType.ERROR);
			alert.setTimeout(Alert.FOREVER);
			Display.getDisplay(ContextHandlerManager.getInstance().getCurrentMIDlet()).setCurrent(alert, Display.getDisplay(ContextHandlerManager.getInstance().getCurrentMIDlet()).getCurrent());
			//ContextHandlerManager.getInstance().notifyAsUnused(this);
			//return;
			throw new RuntimeException("LocationProvider is null!");
		}
		
		this._locationProvider.setLocationListener(this, this._interval, this._timeout, this._maxAge);
		
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.ContextHandler#stopHandling()
	 */
	public void stopHandling() {
		if(this._locationProvider!=null){
			this._locationProvider.reset();
			this._locationProvider=null;
		}
		this._currentLocation=null;

	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.ContextHandler#updateStatus()
	 */
	public void updateStatus() {
		if(this._currentLocation==null||this._currentLocation.isValid()==false)
		{
			for(int i=0;i<this._registeredConstraints.size();i++)
			{
				ConstraintNode c=(ConstraintNode)this._registeredConstraints.elementAt(i);
				c.setActualValue(new FuzzyLogicBoolean());
			}
			return;
		}
		
		QualifiedCoordinates actualCoordinates=this._currentLocation.getQualifiedCoordinates();
		for(int i=0;i<this._registeredConstraints.size();i++)
		{
			ConstraintNode c=(ConstraintNode)this._registeredConstraints.elementAt(i);
			if(c instanceof GPSConstraintNode){
				GPSConstraintNode gps=(GPSConstraintNode)c;
				float distance=actualCoordinates.distance(gps.getCoordinates());
				//System.out.println(this.getClass().getName()+" updateStatus() distance="+distance+" type="+c.getCheckType());
				boolean flag=false;
				
				
				switch(c.getCheckType())
				{
					case ConstraintNode.EQUALS:
					{ 
						flag=(distance==0);
						break;
					}
					case ConstraintNode.LESSER:
					{ 
						flag=(distance<gps.getRadius());
						break;
					}
					case ConstraintNode.LESSER_EQUAL:
					{ 
						flag=(distance<=gps.getRadius());
						break;
					}
					case ConstraintNode.NOT_EQUALS:
					{ 
						flag=(distance!=gps.getRadius());
						break;
					}
					case ConstraintNode.GREATER:
					{ 
						flag=(distance>gps.getRadius());
						break;
					}
					case ConstraintNode.GREATER_EQUAL:
					{ 
						flag=(distance>=gps.getRadius());
						break;
					}
					default:
					{
						flag=false;
					}
				}
				FuzzyLogicBoolean bool=new FuzzyLogicBoolean();
				if(flag)
				{
					bool.setTrue();
				}else
				{
					bool.setFalse();
				}
				gps.setActualValue(bool);
			}else
			{
				c.setActualValue(new FuzzyLogicBoolean());
			}		
		}
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.ContextHandler#registerConstraint(ucl.cs.contextNotifier.ConstraintNode)
	 */
	public void registerConstraint(ConstraintNode c) {
		super.registerConstraint(c);
		c.setActualValue(new FuzzyLogicBoolean());
	}

	public void locationUpdated(LocationProvider provider, Location location) {
		this._currentLocation=location;
		
		this._frontend.pushLocation(location);
		this.updateStatus();
	}

	public void providerStateChanged(LocationProvider provider, int newState) {
		// TODO Auto-generated method stub
		
	}

	
	public static ConstraintNode loadRedifinedConstraintNode(Rule r, String value, int checkType, String contextHandlerName, Hashtable params) {
		//new ConstraintNode(r,value,checkType,contextHandlerName);
		float radius=0;
		double latitude=0;
		double longitude=0;
		float altitude=0;
		try {
			radius = Float.parseFloat((String)params.get("Radius"));
		} catch (NumberFormatException e) {
			System.err.println("GPSLocation.loadRedifinedConstraintNode: Radius="+params.get("Radius"));
		}
		
		try {
			altitude = Float.parseFloat((String)params.get("Altitude"));
		} catch (NumberFormatException e) {
			System.err.println("GPSLocation.loadRedifinedConstraintNode: Altitude="+params.get("Altitude"));
		}
		
		try {
			latitude = Coordinates.convert((String)params.get("Latitude")); 
		} catch (IllegalArgumentException e) {
			System.err.println("GPSLocation.loadRedifinedConstraintNode: Latitude="+params.get("Latitude"));
		}  
		
		try {
			longitude = Coordinates.convert((String)params.get("Longitude"));
		} catch (IllegalArgumentException e) {
			System.err.println("GPSLocation.loadRedifinedConstraintNode: Longitude="+params.get("Longitude"));
		}
		
		Coordinates coord=new Coordinates(latitude,longitude,altitude);
		GPSConstraintNode node=new GPSConstraintNode(r,value,checkType,contextHandlerName,coord,radius);
		return node;
	}
}



