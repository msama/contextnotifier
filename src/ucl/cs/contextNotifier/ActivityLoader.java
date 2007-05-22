/**
 * 
 */
package ucl.cs.contextNotifier;

import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;


import org.kxml2.io.KXmlParser;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


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
public class ActivityLoader {
	
	//public static final String ACTIVITY_CLASS="AdaptiveReminderActivity";
	
	public static final String TNS="http://www.ucl.cs.ac.uk/ContextNotifier/ActivitySchema";
	//public static final String eTNS="";

	private static ActivityLoader INSTANCE=null;
	
	private MIDlet _currentMIDlet=null;

	/**
	 * 
	 */
	protected ActivityLoader() {
	}
	
	
	/**
	 * Singleton Pattern
	 * @return
	 */
	public static ActivityLoader getInstance()
	{
		if(ActivityLoader.INSTANCE==null)
		{
			ActivityLoader.INSTANCE=new ActivityLoader();
		}
		return ActivityLoader.INSTANCE;
	}
	
	
	/**
	 * This methods loads all the activites from file in a specified path
	 * */
	public void loadActivities()
	{	
		String path=null;
		path=System.getProperty("XmlRootFolder");
		if(path==null){
			//path="file://localhost///E:/ContextNotifier/";
			path="file:///E:/ContextNotifier";
		}
		 try {
		     FileConnection fconn = (FileConnection)Connector.open(path);
		     // If no exception is thrown, then the URI is valid, but the file may or may not exist.
		     if (fconn==null||!fconn.exists())
		     {
		    	System.err.println("ActivityLoader.loadActivities. XmlActivitiesRoot \""+path+"\" is missing.");
		    	Alert alert=new Alert("ActivityLoader.loadActivities. XmlActivitiesRoot \""+path+"\" is missing.");
				Display.getDisplay(this._currentMIDlet).setCurrent(alert, Display.getDisplay(this._currentMIDlet).getCurrent());
		    	return;
		     }
		     Enumeration enumeration=fconn.list();
		     fconn.close();
		     int k=0;
		     
		     while(enumeration.hasMoreElements())
			{
				String filename=path+System.getProperty("file.separator")+(String) enumeration.nextElement();
				try {
					if(filename.endsWith(".xml")){
						System.out.println("ActivityLoader.loadActivities. Loading:"+filename);
						this.loadActivity(filename);
						k++;
					}
				} catch (RuntimeException e) {
					System.err.println("ActivityLoader.loadActivities. Probably "+filename+" is not a valid Activity XML file.");
					e.printStackTrace();
					//System.err.println(e.getMessage());
					Alert alert=new Alert("ActivityLoader.loadActivities.","Probably "+filename+" is not a valid Activity XML file.",null,AlertType.ERROR);
					Display.getDisplay(this._currentMIDlet).setCurrent(alert, Display.getDisplay(this._currentMIDlet).getCurrent());
				}
			}
		    System.out.println("ActivityLoader.loadActivities. Loaded Activities: "+k+".");
		 }
		 catch (IOException e) {
			//e.printStackTrace();
			System.err.println(e.getMessage());
			Alert alert=new Alert("ActivityLoader.loadActivities.",e.getMessage(),null,AlertType.ERROR);
			Display.getDisplay(this._currentMIDlet).setCurrent(alert, Display.getDisplay(this._currentMIDlet).getCurrent());
		 }
	

	}
	
	/*
	public void loadActivitiesTest()
	{
		this.loadEvent("file://localhost///NewRoot0/SampleActivity0.xml");
		//this.loadEvent("SampleActivity1.xml");
	}*/


	public Activity loadActivity(String filename) {
		
		Document doc=new Document();
		KXmlParser parser;
		
		//try to open the file
		try {
			FileConnection fconn = (FileConnection)Connector.open(filename);
			if(fconn.exists()==false)
			{
				return null;
			}
			parser=new KXmlParser();
			parser.setFeature (XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
			//parser.setFeature (XmlPullParser.FEATURE_REPORT_NAMESPACE_ATTRIBUTES, true);
			//parser.setFeature(XmlPullParser.FEATURE_VALIDATION, true);
			try {
				parser.setInput(new InputStreamReader(fconn.openInputStream()));
			} catch (XmlPullParserException e) {
				e.printStackTrace();
				return null;
			}
			
			//parser=new XmlParser(new InputStreamReader(ActivityLoader.class.getResourceAsStream(filename)));
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		//try to load document
		try {
			doc.parse(parser);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		}
		
		Element root=doc.getRootElement();
		//root must be an activity
		if(!root.getName().equals("Activity"))
		{
			System.err.println("ActivityLoader.loadActivities."+filename+" is not an activity file.");
			return null;
		}
		
		//leggo gli attributi
		String name=root.getAttributeValue("","Name");
		String description=root.getAttributeValue("","Description");
		long date=Long.parseLong(root.getAttributeValue("","Date"));
		
		String sample=root.getAttributeValue("","Sample");
		if(sample!=null && sample.equalsIgnoreCase("true"))
		{
			date=date+System.currentTimeMillis();
		}
		
		Activity activity=new Activity(name,description,date);
		//long disposingTime=0;
		//rule parsing
		for(int i=0;i<root.getChildCount();i++)
		{
			Object c=root.getChild(i);
			if(c instanceof Element){
				Element ruleEl=(Element)c;
				//if(ruleEl.getName().equals(arg0))
				Rule r=this.loadRule(ruleEl, activity);
				/*
				if(disposingTime<r.getDeactivationMsec())
				{
					disposingTime=r.getDeactivationMsec();
				}
				*/
			}
		}
		
		//activity.setDatetimeDisposing(disposingTime);
		
		DecisionEngine.getInstance().addActivity(activity);
		return activity;
		/*
		String name=evt.getString(Event.SUMMARY, 0);
		String description=evt.getString(Event.NOTE, 0);
		long date=evt.getDate(Event.START, 0);
		
		Activity activity=new Activity(name,description,date);
		String location=evt.getString(Event.LOCATION, 0);
		this.loadRules(location, activity);
		DecisionEngine.getInstance().addActivity(activity);
		*/
	}
	
	/*
	public void loadRules(String msg,Activity activity)
	{
		Document doc=new Document();
		XmlParser parser;
		try {
			parser=new XmlParser(new StringReader(msg));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		try {
			doc.parse(parser);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		Element root=doc.getRootElement();
		//root must be an activity
		if(!root.getName().equals("Activity"))
		{
			return;
		}
		for(int i=0;i<root.getChildCount();i++)
		{
			Element ruleEl=root.getElement(i);
			this.loadRule(ruleEl, activity);
		}
	}*/
	
	protected Rule loadRule(Element ruleEl, Activity act)
	{
		//System.out.println(ruleEl);
		String name=ruleEl.getAttributeValue("","Name");
		//System.out.println("Name");
		
		//long activationMsec=Long.parseLong(ruleEl.getAttribute("ActivationMsec").getValue());
		//System.out.println("ActivationMsec");
		//long deactivationMsec=Long.parseLong(ruleEl.getAttribute("DeactivationMsec").getValue());
		//System.out.println("DeactivationMsec");
		
		long occurrenceMsec=Long.parseLong(ruleEl.getAttributeValue("","OccurrenceMsec"));
		//System.out.println("OccurrenceMsec");
		Notification notification=this.loadNotification(ruleEl.getElement(TNS,"Notification"));
		//System.out.println("Notification");
		Rule rule=new Rule(name, act, notification, /*activationMsec, deactivationMsec,*/ occurrenceMsec);
		
		Element tree=null;
		try {
			tree=ruleEl.getElement(TNS,"NotificationTree");
			if(tree==null)
			{
				throw new RuntimeException("");
			}
			rule.setNotificationRootNode(this.loadConstraintTree(tree, rule));
		} catch (RuntimeException e) {
			throw new RuntimeException("A Rule must contain a NotificationTree element!");
		}finally
		{
			tree=null;
		}
		
		try {
			tree=ruleEl.getElement(TNS,"ActivationTree");
			rule.setActivationRootNode(this.loadConstraintTree(tree, rule));
		} catch (RuntimeException e) {
		}finally
		{
			tree=null;
		}
		
		try {
			tree=ruleEl.getElement(TNS,"DeactivationTree");
			rule.setDeactivationRootNode(this.loadConstraintTree(tree, rule));
		} catch (RuntimeException e) {
		}finally
		{
			tree=null;
		}
		
		/*
		Element rootNode=null;
		try {
			rootNode=ruleEl.getElement("ConstraintNode");
		} catch (RuntimeException e) {
		}
		if(rootNode==null)
		{
			try {
				rootNode=ruleEl.getElement("AndConstraintNode");
			} catch (RuntimeException e) {
			}
		}
		if(rootNode==null)
		{
			try {
				rootNode=ruleEl.getElement("OrConstraintNode");
			} catch (RuntimeException e) {
			}
		}
		AbstractConstraintNode node=this.loadAbstractConstraintNode(rootNode, rule);
		//System.out.println("ActivityLoader.loadRule node: "+node);
		rule.setNotificationRootNode(node);*/
		return rule;
	}

	
	protected AbstractConstraintNode loadConstraintTree(Element element, Rule rule)
	{
		for(int i=0;i<element.getChildCount();i++)
		{
			Object n=element.getChild(i);
			if(n instanceof Element){
				Element child=(Element)n;
				if(child.getName().endsWith("ConstraintNode"))
				{
					AbstractConstraintNode constraintRoot= this.loadAbstractConstraintNode(child, rule, null);
					//constraint node loaded from this method are root nodes
					constraintRoot.setRoot(true);
					return constraintRoot;
				}
			}
		}
		return null;
	}


	protected Notification loadNotification(Element element) {
		String notifier=element.getAttributeValue("","Notifier");
		boolean removeActivity=element.getAttributeValue("","RemoveActivity").equalsIgnoreCase("true");
		boolean removeRule=element.getAttributeValue("","RemoveRule").equalsIgnoreCase("true");
		Notification notification = new Notification(notifier,removeActivity,removeRule);
		for(int i=0;i<element.getChildCount();i++)
		{
			Object c=element.getChild(i);
			if(c instanceof Element){
				Element paramEl=(Element)c;
				if(paramEl.getName().equals("Param")==false)
				{
					continue;
				}
				String nameP=paramEl.getAttributeValue("","Name");
				String valueP=paramEl.getAttributeValue("","Value");
				notification._params.put(nameP,valueP);
			}
		}
		return notification;
	}
	
	protected AbstractConstraintNode loadAbstractConstraintNode(Element element, Rule rule, AbstractConstraintNode parent)
	{
		String type=element.getName();
		//System.out.println("loadAbstractConstraintNode: "+type);
		AbstractConstraintNode node=null;
		if(type.equals("AndConstraintNode"))
		{
			//System.out.println("Loading AndConstraintNode");
			node = this.loadAndConstraintNode(element, rule);
		}else if(type.equals("OrConstraintNode"))
		{
			//System.out.println("Loading OrConstraintNode");
			node = this.loadOrConstraintNode(element, rule);
		}else if(type.equals("NotConstraintNode"))
		{
			//System.out.println("Loading NotConstraintNode");
			node = this.loadNotConstraintNode(element, rule);
		}else if(type.equals("ConstraintNode"))
		{
			//System.out.println("Loading ConstraintNode");
			node = this.loadConstraintNode(element, rule);
		}
		//System.out.println("ActivityLoader.loadAbstractConstraintNode. Loaded: "+node);
		if(parent!=null){
			node.setParent(parent);
		}else
		{
			node.setRoot(true);
		}
		return node;
	}
	
	protected AndConstraintNode loadAndConstraintNode(Element element, Rule rule)
	{
		AndConstraintNode node=new AndConstraintNode(rule);
		int nodeCount=0;
		for(int i=0;i<element.getChildCount();i++)
		{
			Object n=element.getChild(i);
			if(n instanceof Element){
				Element child=(Element)n;
				node.innerNodes.addElement(this.loadAbstractConstraintNode(child, rule, node));
				nodeCount++;
			}
		}
		//post
		if(nodeCount<2)
		{
			throw new java.lang.RuntimeException("An instance of AndConstraintNode must have must have at least 2 children!");
		}
		return node;
	}
	
	protected OrConstraintNode loadOrConstraintNode(Element element, Rule rule)
	{
		OrConstraintNode node=new OrConstraintNode(rule);
		int nodeCount=0;
		for(int i=0;i<element.getChildCount();i++)
		{
			Object n=element.getChild(i);
			if(n instanceof Element){
				Element child=(Element)n;
				node.innerNodes.addElement(this.loadAbstractConstraintNode(child, rule, node));
				nodeCount++;
			}
			
		}
		//post
		if(nodeCount<2)
		{
			throw new java.lang.RuntimeException("An instance of OrConstraintNode must have at least 2 children!");
		}
		return node;
	}
	
	protected NotConstraintNode loadNotConstraintNode(Element element, Rule rule)
	{
		NotConstraintNode node=new NotConstraintNode(rule);
		int nodeCount=0;
		for(int i=0;i<element.getChildCount();i++)
		{
			Object n=element.getChild(i);
			if(n instanceof Element){
				Element child=(Element)n;
				node.innerNodes.addElement(this.loadAbstractConstraintNode(child, rule, node));
				nodeCount++;
			}
			
		}
		//post
		if(nodeCount!=1)
		{
			throw new java.lang.RuntimeException("An instance of NotConstraintNode must have exactly one child!");
		}
		return node;
	}
	
	protected ConstraintNode loadConstraintNode(Element element, Rule rule)
	{
		String value=element.getAttributeValue("","Value");
		String handler=element.getAttributeValue("","Handler");
		String constraint=element.getAttributeValue("","Type");
		ConstraintNode node=null;
		int k=0;
		if(constraint.equals("Equals"))
		{
			k=ConstraintNode.EQUALS;
		}else if(constraint.equals("NotEquals"))
		{
			k=ConstraintNode.NOT_EQUALS;
		}else if(constraint.equals("Greater"))
		{
			k=ConstraintNode.GREATER;
		}else if(constraint.equals("GreaterEqual"))
		{
			k=ConstraintNode.GREATER_EQUAL;
		}else if(constraint.equals("Lesser"))
		{
			k=ConstraintNode.LESSER;
		}else if(constraint.equals("LesserEqual"))
		{
			k=ConstraintNode.LESSER_EQUAL;
		}
		boolean redifined=false;
		
		try {
			//redifined= Boolean.parseBoolean( element.getAttribute("Redifined").getValue());
			redifined= element.getAttributeValue("","Redifined").equalsIgnoreCase("true");
		} catch (RuntimeException e) {
			System.out.println("ActivityLoader.loadConstraintNode: Redifined attribute missing, assuming false.");
		}
		
		if(redifined==true)
		{
			Hashtable params=new Hashtable();
			for(int i=0;i<element.getChildCount();i++)
			{
				Object c=element.getChild(i);
				if(c instanceof Element){
					Element paramEl=(Element)c;
					if(paramEl.getName().equals("Param")==false)
					{
						continue;
					}
					String nameP=paramEl.getAttributeValue("","Name");
					String valueP=paramEl.getAttributeValue("","Value");
					params.put(nameP,valueP);
				}
			}
			node = ContextHandlerManager.loadRedifinedConstraintNode(rule,value,k,handler,params);
		}else
		{
			node = new ConstraintNode(rule,value,k,handler);
		}
		
		return node;
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
}
