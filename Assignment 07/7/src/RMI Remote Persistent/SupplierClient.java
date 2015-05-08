
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 7 Assignment - Ex 7
 * Winter 2015
 * 02/21/2015
 *
 * SupplierClient.java
 * This is ran on the client to get instance on the server
 *
 */

package RMI;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

public class SupplierClient {
    public static void main(String[] args) {
        
        // security layer to allow download of remote objects
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new RMISecurityManager());
        
        // host address
        String url = "rmi://localhost:1099/";
        
        try {
            // get the remote objects and display their information
            Supplier c1 = (Supplier) Naming.lookup(url+"S1");
            Supplier c2 = (Supplier) Naming.lookup(url+"S2");
            
            System.out.println(c1.getNumber() + " " +
                               c1.getName() + " " +
                               c1.getStatus() + " " +
                               c1.getCity()
                               );
            System.out.println(c2.getNumber() + " " +
                               c2.getName() + " " +
                               c2.getStatus() + " " +
                               c2.getCity()
                               );
            
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
