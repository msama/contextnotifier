/**
 *
 */
package ucl.cs.contextNotifier.handlers;

import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.List;

import ucl.cs.contextNotifier.ConstraintNode;
import ucl.cs.contextNotifier.ContextHandler;
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
public class FileSystemHandler extends ContextHandler implements Runnable{

	private FileSystemFrontend _frontend=new FileSystemFrontend("FileSystemHandler",List.IMPLICIT);
	

	private Thread _updateThread=null;
	private long _updateTime=600000;
	private boolean _started=false;
	
	/**
	 * 
	 */
	public FileSystemHandler() {
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
			this._updateThread=new Thread(this,"FileSystemHandler update thread");
			this._started=true;
			this._updateThread.start();
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.ContextHandler#stopHandling()
	 */
	public void stopHandling() {
		this._started=false;
		this._updateThread=null;
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.ContextHandler#updateStatus()
	 */
	public void updateStatus() {
		for(int i=0; i<this._registeredConstraints.size(); i++)
		{
			ConstraintNode node=(ConstraintNode)this._registeredConstraints.elementAt(i);
			this.updateNode(node);
		}

	}
	
	
	protected void updateNode(ConstraintNode node)
	{
		System.out.println("FileSystemHandler.updateNode");
		FuzzyLogicBoolean fuzzy=new FuzzyLogicBoolean();
		try {
			
			FileConnection conn=(FileConnection) Connector.open(node.getValue(), Connector.READ);
			
			if(node.getCheckType()==ConstraintNode.EQUALS)
			{
				if(conn.exists()==true)
				{
					fuzzy.setTrue();
				}else
				{
					fuzzy.setFalse();
				}
			}else if(node.getCheckType()==ConstraintNode.NOT_EQUALS)
			{
				if(conn.exists()==false)
				{
					fuzzy.setTrue();
				}else
				{
					fuzzy.setFalse();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			fuzzy.setNotValid();
		}finally
		{
			node.setActualValue(fuzzy);
		}
	}

	
	public static ConstraintNode loadRedifinedConstraintNode(Rule r, String value, int checkType, String contextHandlerName, Hashtable params) {		
		ConstraintNode node=new ConstraintNode(r,value,checkType,contextHandlerName);
		return node;
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.ContextHandler#registerConstraint(ucl.cs.contextNotifier.ConstraintNode)
	 */
	public synchronized void registerConstraint(ConstraintNode c) {
		super.registerConstraint(c);
		this._frontend.append(c.getValue(), null);
		this.updateNode(c);
	}

	/* (non-Javadoc)
	 * @see ucl.cs.contextNotifier.ContextHandler#unregisterConstraint(ucl.cs.contextNotifier.ConstraintNode)
	 */
	public synchronized void unregisterConstraint(ConstraintNode c) {
		super.unregisterConstraint(c);
		for(int i=this._frontend.size()-1;i>-1;i--)
		{
			String s=this._frontend.getString(i);
			if(s.equals(c.getValue()))
			{
				this._frontend.delete(i);
				break;
			}
		}
	}

	public void run() {
		while(this._started==true && Thread.currentThread()==this._updateThread)
		{
			this.updateStatus();
			try {
				Thread.sleep(this._updateTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
