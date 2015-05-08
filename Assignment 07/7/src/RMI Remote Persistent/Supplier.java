
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 7 Assignment - Ex 7
 * Winter 2015
 * 02/21/2015
 *
 * Supplier.java
 * is an interface and extends Remote - subclasses can be accessed from client
 *
 */

package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Supplier extends Remote {
    public String getName() throws RemoteException;
    public String getNumber() throws RemoteException;
    public String getStatus() throws RemoteException;
    public String getCity() throws RemoteException;
}
