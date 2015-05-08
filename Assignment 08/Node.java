
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 8 Assignment
 * Winter 2015
 * 02/25/2015
 *
 * Node.java
 * Node implementation for use with Queue
 */

public class Node {
	
    public Object element = new Object();
	public Node next;
	
	Node(Object obj) {
            element = obj;
	}
}