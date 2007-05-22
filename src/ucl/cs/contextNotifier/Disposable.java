/**
 * 
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
public interface Disposable {

	public void dispose();
	
	public boolean isDisposed();
	
	public void childDispose(Disposable child);
}
