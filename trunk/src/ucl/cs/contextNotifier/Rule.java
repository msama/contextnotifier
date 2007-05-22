/**
 * Rule.java
 *
 * Created on January 9, 2007, 3:56 PM
 *
 * AdaptiveReminder
 * Developed by Michele Sama
 * Computer Science department, UCL
 */

package ucl.cs.contextNotifier;


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
public class Rule implements Runnable, Disposable{
    
    private String _name="";
    private Activity _activity=null;
    private FuzzyLogicBoolean _actualValue=new FuzzyLogicBoolean();
    private boolean _active=false;
    private boolean _activating=false;
    private boolean _deactivating=false;
    private boolean _disposed=false;
    
    private Notification _notification=null;
    
    //private long _activationMsec=0;
    //private long _deactivationMsec=Long.MAX_VALUE;
    private long _occurrenceMsec=Long.MAX_VALUE;
    private long _transitoryMsec=500;
    private long _lastValueChangeMsec=0;
    private long _lastActivationMsec=0;
    private long _lastDeactivationMsec=0;
    private Thread _activatorThread=null;
    private Thread _deactivatorThread=null;

    private AbstractConstraintNode _notificationRootNode=null;
    private AbstractConstraintNode _activationRootNode=null;
    private AbstractConstraintNode _deactivationRootNode=null;
            
    
    private Thread _innerThread=null;
    
  
    
    


    /**
	 * @param _name
	 * @param _activity
	 * @param _notification
	 */
	public Rule(String _name, Activity _activity, Notification _notification) {
		super();
		this._name = _name;
		this.setActivity(_activity);
		this._notification = _notification;
	}

	/* 
	/**
	 * @param _name
	 * @param _activity
	 * @param _notification
	 * @param msec
	 * @param msec2
	 */
	/*
	public Rule(String _name, Activity _activity, Notification _notification, long msec, long msec2) {
		super();
		this._name = _name;
		this.setActivity(_activity);
		this._notification = _notification;
		_activationMsec = msec;
		_deactivationMsec = msec2;
	}*/

	/**
	 * @param _name
	 * @param _activity
	 * @param _notification
	 * @param _occurrenceMsec
	 */
	public Rule(String _name, Activity _activity, Notification _notification, long _occurrenceMsec) {
		super();
		this._name = _name;
		this.setActivity(_activity);
		this._notification = _notification;
		this._occurrenceMsec = _occurrenceMsec;
	}

	public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public FuzzyLogicBoolean isActualValue() {
        return _actualValue;
    }

    public void checkActualStatus() {
    	//Activation
    	if(this.isActive()==false&&this.getActivationRootNode()!=null)
    	{
    		if(this.getActivationRootNode().isActive()==false)
    		{
    			this.getActivationRootNode().setActive(true);
    		}
    		FuzzyLogicBoolean activation=this.getActivationRootNode().getLogicValue();
    		if(activation.isTrue())
    		{
    			this.setActive(true);
    			/*if(this._activating==false)
    			{
    				this._activating=true;
    				this._deactivating=false;
    				this._lastActivationMsec=System.currentTimeMillis();
    				if(this._activatorThread==null){
    					this._activatorThread=new Thread("Rule "+this.getName()+" activator")
    					{
    						public void run()
    						{
    							long l;
    							while((l=(System.currentTimeMillis()-_lastActivationMsec))<_transitoryMsec){
    						        try {
    									Thread.sleep(_lastActivationMsec-l);
    								} catch (InterruptedException e) {
    									e.printStackTrace();
    								}
    					    	}
    							if(_activating)
    				    		{
    								setActive(true);
    				    		}
    						}
    					};
        			}
        			if(this._activatorThread.isAlive()==false){
        				this._activatorThread.start();
        			}
    			}*/
    			
    			
    		}/*else
    		{
    			this._activating=false;
    		}*/
    		return;
    	}else if(this.isActive()==false&&this.getActivationRootNode()==null)
    	{
    		this.setActive(true);
    		return;
    	}
    	
    	
    	//Deactivation
    	if(this.isActive()==true&&this.getDeactivationRootNode()!=null)
    	{
    		if(this.getDeactivationRootNode().isActive()==false)
    		{
    			this.getDeactivationRootNode().setActive(true);
    		}
    		FuzzyLogicBoolean deactivation=this.getDeactivationRootNode().getLogicValue();
    		if(deactivation.isTrue())
    		{
    			/*if(this._deactivating==false)
    			{
    				this._activating=false;
    				this._deactivating=true;
    				this._lastDeactivationMsec=System.currentTimeMillis();
    				if(this._deactivatorThread==null){
    					this._deactivatorThread=new Thread("Rule "+this.getName()+" deactivator")
    					{
    						public void run()
    						{
    							long l;
    							while((l=(System.currentTimeMillis()-_lastDeactivationMsec))<_transitoryMsec){
    						        try {
    									Thread.sleep(_lastDeactivationMsec-l);
    								} catch (InterruptedException e) {
    									e.printStackTrace();
    								}
    					    	}
    							if(_deactivating)
    				    		{
    								setActive(false);
    				    		}
    						}
    					};
        			}
        			if(this._deactivatorThread.isAlive()==false){
        				this._deactivatorThread.start();
        			}
    			}*/
    			this.setActive(false);
    		}
    			
    		/*else
    		{
    			this._deactivating=false;
    		}*/
    	}
    	
    	//Notification
    	FuzzyLogicBoolean old=this._actualValue;
        FuzzyLogicBoolean actualValue=this._notificationRootNode.getLogicValue();
        System.out.println(this.getClass().getName()+" checkActualValue() Rule:"+this._name+" old="+old+" new="+actualValue);
        this._actualValue = actualValue;
        if(this._actualValue.equals(old)==false)
        {
        	this._lastValueChangeMsec=System.currentTimeMillis();
        }
        if(this._actualValue.isTrue()&&(old.isTrue()==false)&&this._activity.isDisposed()==false)
        {
        	System.out.println(this.getClass().getName()+" checkActualValue() Starting notification.");
            this._innerThread=new Thread(this, "Rule "+this.getName());//,"Action: "+this._activity.getName()+" Rule: "+this._name);
            this._innerThread.start();
        }
    }

    /*
    public long getActivationMsec() {
        return _activationMsec;
    }

    public void setActivationMsec(long activationMsec) {
        this._activationMsec = activationMsec;
    }

    public long getDeactivationMsec() {
        return _deactivationMsec;
    }

    public void setDeactivationMsec(long deactivationMsec) {
        this._deactivationMsec = deactivationMsec;
    }
     */
    
    public long getOccurrenceMsec() {
        return _occurrenceMsec;
    }

    public void setOccurrenceMsec(long occurrenceMsec) {
        this._occurrenceMsec = occurrenceMsec;
    }

    /**
     * @return The root node for the Notification tree of constraints
     */
    public AbstractConstraintNode getNotificationRootNode() { 
        return _notificationRootNode;
    }

    /**
     * Set the root node for the notification tree of constraints
     * @param constraintRootNode
     */
    public void setNotificationRootNode(AbstractConstraintNode constraintRootNode) {
        this._notificationRootNode = constraintRootNode;
        //TODO node must be root
    }

    public void run() {
    	long l=0;
    	while((l=(System.currentTimeMillis()-this._lastValueChangeMsec))<this._transitoryMsec){
	        try {
				Thread.sleep(this._transitoryMsec-l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
        while(this.isActualValue().isTrue()&&this._activity.isDisposed()==false)
        {
            try {
                //raise activation events
                 this._activity.notifyActivity(this, this._notification);
            	
            	//sleep for the occurrence
                Thread.sleep(this._occurrenceMsec); 
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } 
        }
           
    }

	/**
	 * @param _notification the _notification to set
	 */
	public void setNotification(Notification _notification) {
		this._notification = _notification;
	}

	/**
	 * @return the _notification
	 */
	public Notification getNotification() {
		return _notification;
	}
    
	
	public void dispose()
	{
		//pre - already disposed
		if(this._disposed==true)
		{
			return;
		}
		this._disposed=true;
//		System.out.println("Rule.dispose()");
		
		//The activation tree is disposed before setting the activity inactive
		if(this.getActivationRootNode()!=null)
		{
			this.getActivationRootNode().dispose();
		}
		
		this.setActive(false);
		
		
		this._notificationRootNode.dispose();
		if(this.getDeactivationRootNode()!=null)
		{
			this.getDeactivationRootNode().dispose();
		}
		DecisionEngine.getInstance().removeRule(this);
		this._activity.childDispose(this);
		
	}

	/**
	 * @param _activity the _activity to set
	 */
	public void setActivity(Activity _activity) {
		if(this._activity!=null)
		{
			this._activity.rules.removeElement(this);
		}
		this._activity = _activity;
		if(this._activity!=null)
		{
			this._activity.rules.addElement(this);
		}
	}

	/**
	 * @return the _activity
	 */
	public Activity getActivity() {
		return _activity;
	}

	public boolean isDisposed() {
		return this._disposed;
	}

	/**
	 * @param _active the _active to set
	 */
	public void setActive(boolean _active) {
		if(this._active==_active)
		{
			//nothing to do
			return; 
		}
		this._active = _active;
		if(this._active==true)
		{
			if(this.getActivationRootNode()!=null){
				this.getActivationRootNode().setActive(false);
			}
			DecisionEngine.getInstance().getActivityFrontend().remove(this);
			DecisionEngine.getInstance().getActivityFrontend().append(this);
			
			if(this.getDeactivationRootNode()!=null){
				this.getDeactivationRootNode().setActive(true);
			}
			this.getNotificationRootNode().setActive(true);
		}else
		{
			if(this.getActivationRootNode()!=null){
				this.getActivationRootNode().setActive(true);
			}
			DecisionEngine.getInstance().getActivityFrontend().remove(this);
			DecisionEngine.getInstance().getActivityFrontend().append(this);
			this.getNotificationRootNode().setActive(false);
		
			if(this.getDeactivationRootNode()!=null)
			{
				this.getDeactivationRootNode().setActive(false);
			}
		}
		//this._notificationRootNode.setActive(this._active);
		this.checkActualStatus();
	}

	/**
	 * @return the _active
	 */
	public boolean isActive() {
		return _active;
	}

	/**
	 * @param _activationRootNode the _activationRootNode to set. If null the rule will be activated when the Activity is loaded.
	 */
	public void setActivationRootNode(AbstractConstraintNode _activationRootNode) {
		this._activationRootNode = _activationRootNode;
		//post
		if(this._activationRootNode!=null&&this.isActive()==false)
		{
			this._activationRootNode.setActive(true);
		}
	}

	/**
	 * @return the _activationRootNode
	 */
	public AbstractConstraintNode getActivationRootNode() {
		return _activationRootNode;
	}

	/**
	 * @param _deactivationRootNode the _deactivationRootNode to set. If null the rule will be active untill the whole Activity is active.
	 */
	public void setDeactivationRootNode(AbstractConstraintNode _deactivationRootNode) {
		this._deactivationRootNode = _deactivationRootNode;
	}

	/**
	 * @return the _deactivationRootNode
	 */
	public AbstractConstraintNode getDeactivationRootNode() {
		return _deactivationRootNode;
	}

	public void childDispose(Disposable child) {
		if(child instanceof AbstractConstraintNode)
		{
			AbstractConstraintNode node=(AbstractConstraintNode)child;
			if(node==this.getActivationRootNode())
			{
				this.setActivationRootNode(null);
			}else if(node==this.getNotificationRootNode())
			{
				this.setNotificationRootNode(null);
			}else if(node==this.getDeactivationRootNode())
			{
				this.setDeactivationRootNode(null);
			}
		}else
		{
			throw new RuntimeException("Only AbstractConstraintNode are supposed to notify the Rule of their disposed status.");
		}
		
	}

    
}
