/*
 * ContextHandlerManager.java
 *
 * Created on January 10, 2007, 2:09 PM
 *
 * AdaptiveReminder
 * Developed by Michele Sama
 * Computer Science department, UCL
 *
 *
 * This class is a SingletonFactory for ContextHandlers
 * It is needed until CLDC is not offering classloading by name
 */

package ucl.cs.contextNotifier;

import java.util.Hashtable;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

import ucl.cs.contextNotifier.handlers.ActivityStatusHandler;
import ucl.cs.contextNotifier.handlers.BlueToothFinder;
import ucl.cs.contextNotifier.handlers.FileSystemHandler;
import ucl.cs.contextNotifier.handlers.GPSLocation;
import ucl.cs.contextNotifier.handlers.TimeHandler;

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
public class ContextHandlerManager {
    
	/*References to the abstract class Contexthandler are needed in order to avoid runtime exception when a library is missing*/
	
    /*BT_FINDER*/
    public static String BT_FINDER="BlueToothFinder";
    //private BlueToothFinder _btFinder=null;
    private ContextHandler _btFinder=null;
    
    /*TIME*/
    public static String TIME="TimeHandler";
    //private TimeHandler _timeHandler=null;
    private ContextHandler _timeHandler=null;
    
    /*GPS_LOCATION*/
    public static String GPS_LOCATION="GPSLocation";
    //private GPSLocation _gpsLocation=null;
    private ContextHandler _gpsLocation=null;
    
    /*ACTIVITY_STATUS_HANDLER*/
    public static String ACTIVITY_STATUS_HANDLER="ActivityStatusHandler";
    //private ActivityStatusHandler _activityStatusHandler=null;
    private ContextHandler _activityStatusHandler=null;
    
    /*FILE_SYSTEM_HANDLER*/
    public static String FILE_SYSTEM_HANDLER="FileSystemHandler";
    //private ActivityStatusHandler _activityStatusHandler=null;
    private ContextHandler _fileSystemHandler=null;
    
    private MIDlet _currentMIDlet=null;
	
    
    private static ContextHandlerManager _INSTANCE=null; 
    
    
    /** Creates a new instance of ContextHandlerManager */
    private ContextHandlerManager() {
    }
    
    
    public static ContextHandlerManager getInstance()
    {
        if(ContextHandlerManager._INSTANCE==null)
        {
            ContextHandlerManager._INSTANCE=new ContextHandlerManager();
        }
        return ContextHandlerManager._INSTANCE;
    }
    
    public synchronized void notifyAsUnused(ContextHandler c)
    {
    	ContextHandler toBeRemoved=null;
        if(c.equals(this._btFinder))
        {
        	toBeRemoved=this._btFinder;
            this._btFinder=null;
        }else if(c.equals(this._timeHandler))
        {
        	toBeRemoved=this._timeHandler;
        	this._timeHandler=null;
        }else if(c.equals(this._gpsLocation))
        {
        	toBeRemoved=this._gpsLocation;
        	this._gpsLocation=null;
        }else if(c.equals(this._activityStatusHandler))
        {
        	toBeRemoved=this._activityStatusHandler;
        	this._activityStatusHandler=null;
        }else if(c.equals(this._fileSystemHandler))
        {
        	toBeRemoved=this._fileSystemHandler;
        	this._fileSystemHandler=null;
        }
        
        //remove frontend from the container
        if(toBeRemoved!=null&&this._currentMIDlet instanceof FrontendContainer)
    	{
    		((FrontendContainer)this._currentMIDlet).removeFrontend(toBeRemoved.getFrontEnd());
    	}
    }
    
    public synchronized ContextHandler getContextHandlerByName(String name)
    {
    	ContextHandler toBeCreated=null;
        if(name.equals(ContextHandlerManager.BT_FINDER))
        {
            if(this._btFinder!=null)
            {
            	return this._btFinder;
            }
            try {
				this._btFinder=new BlueToothFinder();
				this._btFinder.startHandling();
	            toBeCreated=this._btFinder;
			} catch (RuntimeException e) {
				//e.printStackTrace();
				this._btFinder=null;
				Alert alert=new Alert(BT_FINDER,"Unable to load "+BT_FINDER+"!\n The computation will continue without that handler.",null,AlertType.ERROR);
				Display.getDisplay(this._currentMIDlet).setCurrent(alert, Display.getDisplay(this._currentMIDlet).getCurrent());
			}
            
        }else if(name.equals(ContextHandlerManager.GPS_LOCATION))
        {
        	if(this._gpsLocation!=null)
            {
               return this._gpsLocation;
            }
        	try {
        		this._gpsLocation=new GPSLocation();
                this._gpsLocation.startHandling();
                toBeCreated=this._gpsLocation;
			} catch (RuntimeException e) {
				//e.printStackTrace();
				this._gpsLocation=null;
				Alert alert=new Alert(GPS_LOCATION,"Unable to load "+GPS_LOCATION+"!\n The computation will continue without that handler.",null,AlertType.ERROR);
				Display.getDisplay(this._currentMIDlet).setCurrent(alert, Display.getDisplay(this._currentMIDlet).getCurrent());
			}

        }else if(name.equals(ContextHandlerManager.TIME))
        {
        	if(this._timeHandler!=null)
            {
                return this._timeHandler;
            }
        	try {
        		this._timeHandler=new TimeHandler();
        		this._timeHandler.startHandling();
                toBeCreated=this._timeHandler;
			} catch (RuntimeException e) {
				//e.printStackTrace();
				this._timeHandler=null;
				Alert alert=new Alert(TIME,"Unable to load "+TIME+"!\n The computation will continue without that handler.",null,AlertType.ERROR);
				Display.getDisplay(this._currentMIDlet).setCurrent(alert, Display.getDisplay(this._currentMIDlet).getCurrent());
			}
            
        }else if(name.equals(ContextHandlerManager.ACTIVITY_STATUS_HANDLER))
        {
        	if(this._activityStatusHandler!=null)
            {
                return this._activityStatusHandler;
            }
        	try {
        		this._activityStatusHandler=new ActivityStatusHandler();
        		this._activityStatusHandler.startHandling();
                toBeCreated=this._activityStatusHandler;
			} catch (RuntimeException e) {
				//e.printStackTrace();
				Alert alert=new Alert(ACTIVITY_STATUS_HANDLER,"Unable to load "+ACTIVITY_STATUS_HANDLER+"!\n The computation will continue without that handler.",null,AlertType.ERROR);
				Display.getDisplay(this._currentMIDlet).setCurrent(alert, Display.getDisplay(this._currentMIDlet).getCurrent());
			}
            
        }else if(name.equals(ContextHandlerManager.FILE_SYSTEM_HANDLER))
        {
        	if(this._fileSystemHandler!=null)
            {
                return this._fileSystemHandler;
            }
        	try {
        		this._fileSystemHandler=new FileSystemHandler();
        		this._fileSystemHandler.startHandling();
                toBeCreated=this._fileSystemHandler;
			} catch (RuntimeException e) {
				//e.printStackTrace();
				Alert alert=new Alert(FILE_SYSTEM_HANDLER,"Unable to load "+FILE_SYSTEM_HANDLER+"!\n The computation will continue without that handler.",null,AlertType.ERROR);
				Display.getDisplay(this._currentMIDlet).setCurrent(alert, Display.getDisplay(this._currentMIDlet).getCurrent());
			}
            
        }else
        {
            //return null;
        }
        
        //add frontend to the container
        if(toBeCreated!=null&&this._currentMIDlet instanceof FrontendContainer)
    	{
    		((FrontendContainer)this._currentMIDlet).addFrontend(toBeCreated.getFrontEnd());
    	}
        return toBeCreated;
    }
    
	/**
	 * @param _currentMIDlet the _currentMIDlet to set
	 */
	public void setCurrentMIDlet(MIDlet _currentMIDlet) {
		this._currentMIDlet = _currentMIDlet;
	}

	/**
	 * @return the _currentMIDlet
	 */
	public MIDlet getCurrentMIDlet() {
		return _currentMIDlet;
	}
	
	
	public static ConstraintNode loadRedifinedConstraintNode(Rule r,String value, int checkType, String contextHandlerName, Hashtable params)
	{
        if(contextHandlerName.equals(ContextHandlerManager.BT_FINDER))
        {
            return BlueToothFinder.loadRedifinedConstraintNode(r,value,checkType,contextHandlerName,params);         
        }else if(contextHandlerName.equals(ContextHandlerManager.GPS_LOCATION))
        {
        	return GPSLocation.loadRedifinedConstraintNode(r,value,checkType,contextHandlerName,params);  
        }else if(contextHandlerName.equals(ContextHandlerManager.TIME))
        {
        	return TimeHandler.loadRedifinedConstraintNode(r,value,checkType,contextHandlerName,params);  
        }else if(contextHandlerName.equals(ContextHandlerManager.ACTIVITY_STATUS_HANDLER))
        {
        	return ActivityStatusHandler.loadRedifinedConstraintNode(r,value,checkType,contextHandlerName,params);  
        }else if(contextHandlerName.equals(ContextHandlerManager.FILE_SYSTEM_HANDLER))
        {
        	return FileSystemHandler.loadRedifinedConstraintNode(r,value,checkType,contextHandlerName,params);  
        }else
        {
            return null;
        }
	}


}
