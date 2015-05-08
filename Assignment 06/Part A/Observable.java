
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 4 Assignment - Ex 6a
 * Winter 2015
 * 02/11/2015
 *
 * Observable.java
 * Objects that can be watched by Observers
 */

import java.util.Vector;

public class Observable {
    
    // Member variables
    private Vector<Observer> observers = new Vector<>();
    private Boolean changed = false;
    
    // Constructor
    public Observable() {}
    
   /* addObserver() method
    * Adds new Observer object into the vector
    * @param Observer object - an object that is observed (Supplier in this particular case)
    */
    public void addObserver(Observer o) {
        observers.add(o);
    }
    
   /* nofityObservers() method
    * Notifies all Observer instances to be updated when changes are made
    */
    public void notifyObservers() {
        
        if (changed) {
            for (Observer o : observers) {
                o.update(this);
            }
            changed = false;
        }
    }   
    
    public void setChanged() {
        changed = true;
    }
    
}
