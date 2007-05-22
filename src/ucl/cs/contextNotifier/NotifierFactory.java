/**
 * 
 */
package ucl.cs.contextNotifier;

import ucl.cs.contextNotifier.notifiers.InvisibleNotifier;
import ucl.cs.contextNotifier.notifiers.RingtoneNotifier;
import ucl.cs.contextNotifier.notifiers.SilentNotifier;
import ucl.cs.contextNotifier.notifiers.VibrationNotifier;

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
public class NotifierFactory {
	
	 
    /* SILENT_NOTIFIER*/
    public static String SILENT_NOTIFIER="SilentNotifier";
    
    /* VIBRATION_NOTIFIER*/
    public static String VIBRATION_NOTIFIER="VibrationNotifier";
    
    /* VIBRATION_NOTIFIER*/
    public static String RINGTONE_NOTIFIER="RingtoneNotifier";
    
    /*INVISIBLE_NOTIFIER*/
    public static String INVISIBLE_NOTIFIER="InvisibleNotifier";
    
    /**
     * @param name String name of the selected Notifier
     * @return an instance of the selected Notifier. Each invocation returns a different instance.
     */
    public static Notifier getNotifierByName(String name)
    {
        if(name.equals(NotifierFactory.SILENT_NOTIFIER))
        {
            return new SilentNotifier();
        }else if(name.equals(NotifierFactory.VIBRATION_NOTIFIER))
        {
            return new VibrationNotifier();
        }else if(name.equals(NotifierFactory.RINGTONE_NOTIFIER))
        {
            return new RingtoneNotifier();
        }else if(name.equals(NotifierFactory.INVISIBLE_NOTIFIER))
        {
            return new InvisibleNotifier();
        }else
        {
            return null;
        }
    }

}
