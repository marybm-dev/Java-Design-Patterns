
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 7 Assignment - Ex 7
 * Winter 2015
 * 02/21/2015
 *
 * SupplierImpl.java (Serialization & RMI)
 * extends UnicastRemoteObject & implements Supplier
 *
 */

package RMI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SupplierImpl extends UnicastRemoteObject implements Supplier {
    public Supp supp;
    
    /** Constructor
     * @param aNumber - a string representation of an ID #
     */
    public SupplierImpl(String aNumber) throws RemoteException {
        supp = new Supp(aNumber);
        loadSupplier();
    }
    
    public void updateSupplier(String aNumber, String aName, String aStatus, String aCity) {
        supp.number = aNumber;
        supp.name = aName;
        supp.status = aStatus;
        supp.city = aCity;
        storeSupplier();
    }
    
    /** loadSupplier() method
     * Reads data from disk
     */
    private void loadSupplier() {
        try {
            FileInputStream f = new FileInputStream(supp.getNumber() + ".dat");
            ObjectInputStream in = new ObjectInputStream(f);
            supp = (Supp) in.readObject();
            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SupplierImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SupplierImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SupplierImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** storeSupplier() method
     * Writes data to disk
     */
    private void storeSupplier() {
        try {
            FileOutputStream f = new FileOutputStream(supp.getNumber() + ".dat");
            ObjectOutputStream out = new ObjectOutputStream(f);
            out.writeObject(supp);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SupplierImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SupplierImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String getNumber() throws RemoteException {
        return (supp.getNumber());
    }
    
    @Override
    public String getName() throws RemoteException {
        return (supp.getName());
    }
    
    @Override
    public String getStatus() throws RemoteException {
        return (supp.getStatus());
    }
    
    @Override
    public String getCity() throws RemoteException {
        return (supp.getCity());
    }
}
