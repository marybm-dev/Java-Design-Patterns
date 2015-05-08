
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 8 Assignment
 * Winter 2015
 * 02/25/2015
 *
 * BSFNode.java
 * Node implementation for use with Queue and BFS
 */

public class BFSNode {
	// Member variables
    public String letter;
    public int depth;

	// Constructor with letter as parameter
    public BFSNode(String l) {
        letter = l;
    }
}