
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 5 Assignment - Ex 6b
 * Winter 2015
 * 02/11/2015
 *
 * Observer.java
 * Interface allows Observer objects to be notified
 * whenever Observable objects change.
 */

public interface Observer {
    
    // method must have empty body
    public void update(Observable o);
    
}
