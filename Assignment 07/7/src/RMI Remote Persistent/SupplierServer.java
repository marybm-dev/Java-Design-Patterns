
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 7 Assignment - Ex 7
 * Winter 2015
 * 02/21/2015
 *
 * SupplierServer.java
 * This is ran on the server and creates the remote objects
 * 
*/

package RMI;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

public class SupplierServer {
    public static void main(String srgs[]) {

        // security layer to allow client to dowload remote objects
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new RMISecurityManager());

        try {
            // create the remote objects
            SupplierImpl s1 = new SupplierImpl("S1");
            SupplierImpl s2 = new SupplierImpl("S2");

            // bind their names so client can find these objects
            Naming.rebind("S1",s1);
            Naming.rebind("S2",s2);

        } catch (Exception e) {
           System.out.println("Error: " + e);
        }
  }
}
