/*
 * BlueToothFinder.java
 *
 * Created on January 10, 2007, 2:28 PM
 *
 * AdaptiveReminder
 * Developed by Michele Sama
 * Computer Science department, UCL
 */

package ucl.cs.contextNotifier.handlers;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.bluetooth.*;
import javax.microedition.lcdui.Choice;


import ucl.cs.contextNotifier.ConstraintNode;
import ucl.cs.contextNotifier.ContextHandler;
import ucl.cs.contextNotifier.Frontend;
import ucl.cs.contextNotifier.FuzzyLogicBoolean;
import ucl.cs.contextNotifier.Rule;




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
public class BlueToothFinder extends ContextHandler implements DiscoveryListener, Runnable{
    
	private BlueToothFinderFrontend _frontend=new BlueToothFinderFrontend("BlueToothFinder",Choice.IMPLICIT);
	
	
	private boolean _started=false;
	private long _timeout=60000;
	//private long _sleep=0;//2000;
	private boolean _inquiring=false;
	//private boolean _updated=false;
	
	private Vector _foundDevices=new Vector();
	//private Vector _friendlyNames=new Vector();
	private Vector _deviceClass=new Vector();
    private DiscoveryAgent agent=null;
    
    
    private Thread _inquiryThread;
	
    /** Creates a new instance of BlueToothFinder */
    public BlueToothFinder() {
    }

	/**@Override*/
	public void startHandling() {
		if(this._started==true){return;}
		this._started=true;
		this._inquiryThread=new Thread(this,"BlueToothFinder inquiry cycle.");
		this._inquiryThread.start();
	}

	/**@Override*/
	public void stopHandling() {
		this._started=false;
	}

	/**@Override*/
	public synchronized void updateStatus() {
		int size=this._foundDevices.size();
		String address[]=new String[size];
		String friendlyName[]=new String[size];
		//DeviceClass[] devClass=new DeviceClass[size];
		for(int i=0;i<size;i++)
        {
            RemoteDevice device=(RemoteDevice)this._foundDevices.elementAt(i);
            address[i]=device.getBluetoothAddress();
            try {
                    friendlyName[i] = device.getFriendlyName(false);
            } catch (IOException e) {
                    e.printStackTrace();
            }
        }
		
        //frontend update begin
        this._frontend.setTitle("BlueToothFinder: Found "+size+" Devices");
        this._frontend.deleteAll();
        for(int i=0;i<size;i++)
        {
            this._frontend.append(friendlyName[i]+" {"+address[i]+"}",(DeviceClass)this._deviceClass.elementAt(i) );
            //this._frontend.insert(0,friendlyName[i]+" {"+address[i]+"}",null);
        }
        //frontend update end
            
            
		for(int i=0; i<this._registeredConstraints.size();i++)
		{
			boolean flag=false;
			ConstraintNode node=(ConstraintNode)this._registeredConstraints.elementAt(i);
			for(int j=0; j<size; j++)
			{

				if(node.getValue().equals(address[j])||node.getValue().equals(friendlyName[j]))
				{
					flag=true;
				}
			}
			//checking node constraints
			if(node.getCheckType()==ConstraintNode.EQUALS)
			{
				//nothing to be done
			}else if(node.getCheckType()==ConstraintNode.NOT_EQUALS)
			{
				flag=!flag;
			}
			FuzzyLogicBoolean bool=new FuzzyLogicBoolean();
			if(flag)
			{
				bool.setTrue();
			}else
			{
				bool.setFalse();
			}
			node.setActualValue(bool);
			//System.out.println("node: "+node.getValue()+" constraint: "+node.getCheckType()+" flag: "+flag);
		}
		
		this._inquiring=false;
		this.notifyAll();
	}
	

	public void deviceDiscovered(RemoteDevice dev, DeviceClass dClass) {
		
		synchronized (this) {
			this._foundDevices.addElement(dev);
			this._deviceClass.addElement(dClass);
			
			/*String name="<unknown>";
			try {
				//using getFriendlyName(false) make the discovery faster, getFriendlyName(true) removes buffer problems
				name=dev.getFriendlyName(true);
			} catch (IOException e) {
				e.printStackTrace();
			}finally
			{
				this._friendlyNames.addElement(name);
			}*/
			//this._frontend.setTitle("BlueToothFinder: Scanning ("+this._foundDevices.size()+")");
		}
		/*
		Alert alert=new Alert("class",dClass.getMajorDeviceClass()+"",null,AlertType.INFO);
		Display.getDisplay(ContextHandlerManager.getInstance().getCurrentMIDlet()).setCurrent(alert, Display.getDisplay(ContextHandlerManager.getInstance().getCurrentMIDlet()).getCurrent());
		*/
		
		/*String name;
		try {
			name = dev.getFriendlyName(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this._frontend.append(name+" {"+dev.getBluetoothAddress()+"}", dClass);*/
		//System.out.println("BlueToothFinder.deviceDiscovered: "+dev.getBluetoothAddress());
	}

	public void inquiryCompleted(int res) {
		/*
		if(res==DiscoveryListener.INQUIRY_COMPLETED)
		{
            this.updateStatus();
		} else if(res==DiscoveryListener.INQUIRY_TERMINATED)
		{
			this.updateStatus();
		} else if(res==DiscoveryListener.INQUIRY_ERROR)
		{
			this.updateStatus();
		}else
		{
			this.updateStatus();
		}*/
		this.updateStatus();
		
	}

	public void serviceSearchCompleted(int arg0, int arg1) {
		//TODO this.updateStatus();
	}

	public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
		// TODO Auto-generated method stub
	}

	public void run() {	
		try {
			agent=LocalDevice.getLocalDevice().getDiscoveryAgent();
		} catch (BluetoothStateException e) { 
			e.printStackTrace();
			return;
		}
		int k=0;
		long time;
		
		while(this._started==true && Thread.currentThread()==this._inquiryThread)
		{
			time=System.currentTimeMillis();
            this.inquireCycle(k);
            time=System.currentTimeMillis()-time;
            k++;
			//delay between each scanning
            if(time<this._timeout)
			try {
				Thread.sleep(this._timeout-time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	private synchronized void inquireCycle(int k)
	{
		this._frontend.setTitle("BlueToothFinder: Iteration: "+k);

        	this._foundDevices.removeAllElements();
    		this._deviceClass.removeAllElements();
    		//this._friendlyNames.removeAllElements();
    		//this._foundDevices=new Vector();
    		//this._deviceClass=new Vector();
    		this._inquiring=true;
		
        
		try {
			agent.startInquiry(DiscoveryAgent.GIAC, this);
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
		//monitor until not updated
		while(this._inquiring==true){
			try {
				this.wait(this._timeout);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				this.agent.cancelInquiry(this);
				//only for debug purpose
				//Alert alert=new Alert("Timeout","InquiryCycle timed out.",null,AlertType.INFO);
				//Display.getDisplay(ContextHandlerManager.getInstance().getCurrentMIDlet()).setCurrent(alert, Display.getDisplay(ContextHandlerManager.getInstance().getCurrentMIDlet()).getCurrent());
				//-------
			}
		}
	}

	public Frontend getFrontEnd() {
		return this._frontend;
	}

	public static ConstraintNode loadRedifinedConstraintNode(Rule r, String value, int checkType, String contextHandlerName, Hashtable params) {
		return new ConstraintNode(r,value,checkType,contextHandlerName);
	}
    
}
