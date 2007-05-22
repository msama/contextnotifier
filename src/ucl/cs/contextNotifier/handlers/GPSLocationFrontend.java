/**
 *
 */
package ucl.cs.contextNotifier.handlers;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import javax.microedition.location.Coordinates;
import javax.microedition.location.Location;

import ucl.cs.contextNotifier.Frontend;

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
public class GPSLocationFrontend extends Form implements Frontend, CommandListener {

	private Command _command=new Command("GPSLocation","Frontend for the GPSLocation context handler.",Command.ITEM,1);

	private StringItem _valid=new StringItem("isValid","FALSE");
	private StringItem _method=new StringItem("Method","Unknown");
	private StringItem _longitude=new StringItem("Longitute","Not ready yet!");
	private StringItem _latitude=new StringItem("Latitude","Not ready yet!");
	private StringItem _altitude=new StringItem("Altitude","Not ready yet!");
	private StringItem _speed=new StringItem("Speed","Not ready yet!");
	private StringItem _horizontalAccuracy=new StringItem("H_Accuracy","Not ready yet!");
	private StringItem _verticalAccuracy=new StringItem("V_Accuracy","Not ready yet!");

	/**
	 * @param arg0
	 * @param arg1
	 */
	public GPSLocationFrontend(String arg0, Item[] arg1) {
		super(arg0, arg1);
		this.initialize();
	}

	/**
	 * @param arg0
	 */
	public GPSLocationFrontend(String arg0) {
		super(arg0);
		this.initialize();
	}
	
	public void initialize()
	{
		this._valid.setLayout(StringItem.LAYOUT_2);
		this._valid.setLayout(StringItem.LAYOUT_NEWLINE_AFTER);
		this.append(_valid);
		this.append(_method);
		this.append(_longitude);
		this.append(_latitude);
		this.append(_altitude);
		this.append(_speed);
		this.append(_horizontalAccuracy);
		this.append(_verticalAccuracy);
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.Frontend#getBaseDisplayable()
	 */
	public Displayable getBaseDisplayable() {
		return this;
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.Frontend#getDefaultCommandListener()
	 */
	public CommandListener getDefaultCommandListener() {
		return this;
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.Frontend#getFrontendCommand()
	 */
	public Command getFrontendCommand() {
		return this._command;
	}

	public void commandAction(Command arg0, Displayable arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void pushLocation(Location location)
	{
		
		this._valid.setText(""+location.isValid());
		
		String method;
		switch(location.getLocationMethod())
		{
			case Location.MTY_TERMINALBASED:
			{
				method="MTY_TERMINALBASED";
				break;
			}
			case Location.MTY_NETWORKBASED:
			{
				method="MTY_NETWORKBASED";
				break;
			}
			case Location.MTE_TIMEOFARRIVAL:
			{
				method="MTE_TIMEOFARRIVAL";
				break;
			}
			case Location.MTE_TIMEDIFFERENCE:
			{
				method="MTE_TIMEDIFFERENCE";
				break;
			}
			case Location.MTE_SHORTRANGE:
			{
				method="MTE_SHORTRANGE";
				break;
			}
			case Location.MTE_SATELLITE:
			{
				method="MTE_SATELLITE";
				break;
			}
			case Location.MTE_CELLID:
			{
				method="MTE_CELLID";
				break;
			}
			case Location.MTE_ANGLEOFARRIVAL:
			{
				method="MTE_ANGLEOFARRIVAL";
				break;
			}
			case Location.MTA_UNASSISTED:
			{
				method="MTA_UNASSISTED";
				break;
			}
			case Location.MTA_ASSISTED:
			{
				method="MTA_ASSISTED";
				break;
			}
			default:
			{
				method="Unknown";
			}
		}
		this._method.setText(method);
		this._longitude.setText(Coordinates.convert(location.getQualifiedCoordinates().getLongitude(),Coordinates.DD_MM_SS));
		this._latitude.setText(Coordinates.convert(location.getQualifiedCoordinates().getLatitude(),Coordinates.DD_MM_SS));
		this._altitude.setText(""+location.getQualifiedCoordinates().getAltitude());
		this._speed.setText(""+location.getSpeed());
		this._horizontalAccuracy.setText(""+location.getQualifiedCoordinates().getHorizontalAccuracy());
		this._verticalAccuracy.setText(""+location.getQualifiedCoordinates().getVerticalAccuracy());
	}

}
