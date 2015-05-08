/* Btree.java
 
 source : Btree.java
 compile: javac Btree.java
 run    : java Btree <btree.dat
 NOTE   : needs IO.java and IO.class from same directory
 
 compare results with C version:
 
 source : btree.c
 run    : btree <btree.dat
 
 Assignment:
 
 Convert the commented-out C code here into Java.
 Uncomment as you go along.
 Search on Convert # for recommended order of conversion
 */

class CharRef { public char key = 0; };

class IntRef { public int number = 0; };

class NodeRef { public Node_type node = new Node_type(); };

// Converted
class Node_type {
    public int count = 0;
    public char key[] = new char[Btree.MAX+1];
    public Node_type branch[] = new Node_type[Btree.MAX+1];
};

public class Btree {
    
    // Converted
    public static void main (String args[]) {
        
        if (args.length == 0)
            io = new IO(System.in,System.out);
        else
            io = new IO(args[0],System.out);
        
        interact();
    }
    
    static final int MAX = 4;
    static final int MIN = 2;
    static final int MAXD = 10;
    static final int MAXWD = 50;
    
    // Converted
    static int step=0;
    static int cnt[] = new int[MAXD];
    static int levels = 0;
    static int loc[][] = new int[MAXD][MAXWD];
    static Node_type ptrs[][] = new Node_type[MAXD][MAXWD];
    static Node_type root;
    static IO io;
    
    // Converted
    static void Error(String msg)
    {
        io.println(msg);            // note that there is an io.print(String) also
    }
    
    // Converted
    static void DFS(Node_type p, int depth)
    {
        if (p != null) {
            if (depth > levels)
                levels = depth;
            
            ptrs[depth][cnt[depth]] = p;
            cnt[depth]++;
            
            for (int i = 0; i <= p.count; i++) {
                DFS(p.branch[i], depth + 1);
            }
        }
    }
    
    // Converted except to uncomment Delete and PrintAllNodes
    static void interact()
    {
        char key;
        root = new Node_type();
        
        for (;;) {
            io.print("> ");
            String s = io.getLine();
            if (s.length() > 0) {
                char cmd = s.charAt(0);
                switch (cmd) {
                    case 'q' : return;
                    case 'h' : io.println("h(elp; q(uit; i(nsert x; d(elete x; p(rint\n");
                        break;
                    case 'i' : key = s.charAt(2);
                        io.println("INSERT: " + key);
                        root = Insert(key,root);
                        break;
                    case 'd' : key = s.charAt(2);
                        io.println("DELETE: " + key);
                        root = Delete(key,root);
                        break;
                    case 'p' : io.println("B-TREE:");
                        PrintAllNodes(root);
                        break;
                } // end switch
            } // end if
        } // end for
    }
    
    
    
    // Convert #1
    static void ListNode(int depth, int cnt, Node_type p)
    {
        io.print(depth + " " + cnt + " ");
        
        for (int i = 1; i <= p.count ; i++ ) {
            io.print( p.key[i] + " ");
        }
        
        io.print("\n");
    }
    
    
    // Convert #2
    static void SetLoc()
    {
        int i, depth, pos, base, first, last, dist, x;
        
        depth = levels;
        pos = 0;
        
        for (i = 0; i < cnt[depth] ; i++){
            loc[depth][i] = pos;
            pos += (ptrs[depth][i].count)*2+2;
        }
        
        for (depth = levels-1; depth >= 0; depth--){
            base = 0;
            for (i = 0; i < cnt[depth]; i++){
                first = base;
                last  = first + (ptrs[depth][i].count);
                dist = ( loc[depth+1][last] - loc[depth+1][first] + 2 * ptrs[depth+1][last].count ) / 2;
                loc[depth][i] = loc[depth+1][first] + dist - ptrs[depth][i].count;
                base += ptrs[depth][i].count+1;
            }
        }
    }
    
    
    // Convert #3
    static void MoveRight(Node_type p, int k)
    {
        int c;
        Node_type t;
        
        t = p.branch[k];
        for (c = t.count ; c > 0 ; c--){
            t.key[c+1] = t.key[c];
            t.branch[c+1] = t.branch[c];
        }
        
        t.branch[1] = t.branch[0];
        t.count++;
        t.key[1] = p.key[k];
        t = p.branch[k-1];
        p.key[k] = t.key[t.count];
        
        p.branch[k].branch[0] = t.branch[t.count];
        t.count--;
    }
    
    
    // Convert #4
    static void MoveLeft(Node_type p,int k)
    {
        int c;
        Node_type t;
        
        t = p.branch[k-1];
        t.count++;
        t.key[t.count] = p.key[k];
        t.branch[t.count] = p.branch[k].branch[0];
        
        t = p.branch[k];
        p.key[k] = t.key[1];
        t.branch[0] = t.branch[1];
        t.count--;
        
        for (c = 1; c <= t.count; c++){
            t.key[c] = t.key[c+1];
            t.branch[c] = t.branch[c+1];
        }
    }
    
    
    // Convert #5
    static void Combine(Node_type p,int k)
    {
        int c;
        Node_type q, l;
        
        q = p.branch[k];
        l = p.branch[k-1];
        l.count++;
        l.key[l.count] = p.key[k];
        l.branch[l.count] = q.branch[0];
        
        for (c = 1; c <= q.count; c++){
            l.count++;
            l.key[l.count] = q.key[c];
            l.branch[l.count] = q.branch[c];
        }
        
        for (c = k; c < p.count; c++){
            p.key[c] = p.key[c+1];
            p.branch[c] = p.branch[c+1];
        }
        
        p.count--;
    }
    
    
    // Convert #6
    static void Restore(Node_type p, int k)
    {
        if (k==0) {
            if (p.branch[1].count > MIN)
                MoveLeft(p, 1);
            else
                Combine(p, 1);
        }
        else if (k == p.count) {
            if (p.branch[k-1].count > MIN)
                MoveRight(p, k);
            else
                Combine(p, k);
        }
        else if (p.branch[k-1].count > MIN)
            MoveRight(p, k);
        else if (p.branch[k+1].count > MIN)
            MoveLeft(p, k+1);
        else
            Combine(p, k);
    }
    
    
    // Convert #7
    static void Remove(Node_type p, int k)
    {
        for (int i = k+1; i <= p.count; i++){
            p.key[i-1] = p.key[i];
            p.branch[i-1] = p.branch[i];
        }
        
        p.count--;
    }
    
    // Convert #8
    static void Successor(Node_type p, int k)
    {
        Node_type q;
        
        for (q = p.branch[k]; q.branch[0] != null;  q = q.branch[0]) {
            ;
        }
        
        p.key[k]=q.key[1];
    }
    
    
    // Convert #9
    static void PushIn(char x, Node_type xr, Node_type p, int k)
    {
        for (int i = p.count; i > k; i--) {
            p.key[i+1] = p.key[i];
            p.branch[i+1] = p.branch[i];
        }
        
        p.key[k+1] = x;
        p.branch[k+1] = xr;
        p.count++;
    }
    
    
    // Convert #10
    // How can parameters be call-by-ref in Java? Not allowed to make global.
    // Try to think of a simple - but good OO - way to handle all of these
    // call-by-ref params in the source. You do not want to make a mistake
    // or the program will be impossible to debug.
    
    static void PrintNode(Node_type p, int depth, int i, IntRef pos)  // CALL-BY-REF
    {
        int j;
        while (pos.number < loc[depth][i]) {
            io.print(" ");
            pos.number++;
        }
        
        io.print("[");
        for (j = 1 ; j < p.count ; j++){
            io.print(p.key[j] + ", ");
            pos.number += 2;
        }
        
        io.print(p.key[j] + "]");
        pos.number += 3;
    }
    
    // Convert #11
    static void PrintTop(Node_type p, int depth, int i, IntRef pos, IntRef child) // CALL-BY-REF
    {
        int j, strt, mid;
        
        mid = loc[depth][i] + p.count;
        for (j = 0 ; j <= p.count ; j++) {
            strt = loc[depth+1][child.number]+1;
            
            if (strt >= mid)
                strt += 2 * p.branch[j].count - 2;
            
            while (pos.number < strt) {
                io.print(" ");
                pos.number++;
            }
            
            if (strt <= mid)
                io.print("/");
            else
                io.print("\\");
            pos.number++;
            child.number++;
        }
    }
    
    // Convert #12
    static void PrintLine(Node_type p, int depth, int i, IntRef pos, IntRef child) // CALL-BY-REF
    {
        int strt, stop, mid;
        
        strt = loc[depth+1][child.number] + 2;
        stop = loc[depth+1][child.number+p.count] + p.branch[p.count].count * 2 - 2;
        
        mid = loc[depth][i]+p.count;
        child.number += p.count+1;
        
        while (pos.number < strt) {
            io.print(" ");
            pos.number++;
        }
        
        while (pos.number <= stop) {
            if (pos.number == mid)
                io.print("|");
            else
                io.print("_");
            pos.number++;
        }
    }
    
    // Convert #13
    static void PrintAllNodes(Node_type root)
    {
        int i, depth;
        IntRef pos = new IntRef();
        IntRef child = new IntRef();
        
        if (root == null)
            io.print("EMPTY\n");
        
        else {
            for (depth = 0; depth < MAXD; depth++) {
                cnt[depth] = 0;
            }
            
            levels = 0;
            DFS(root, 0);
            SetLoc();
            io.print("L\n");
            
            for (depth = 0; depth <= levels; depth++) {
                io.print(Integer.toString(depth));
                pos.number =- 1;
                
                for (i=0; i < cnt[depth]; i++)
                    PrintNode(ptrs[depth][i], depth, i, pos);
                
                io.print("\n");
                
                if (depth < levels) {
                    pos.number =- 2;
                    child.number = 0;
                    
                    for (i=0; i < cnt[depth]; i++)
                        PrintLine(ptrs[depth][i], depth, i, pos, child);
                    
                    io.print("\n");
                    pos.number =- 2;
                    child.number = 0;
                    
                    for (i = 0; i < cnt[depth]; i++)
                        PrintTop(ptrs[depth][i], depth, i, pos, child);
                    
                    io.print("\n");
                }
            }
            io.print("\n");
        }
    }
    
    // Convert #14
    static Boolean SeqSearch(char target, Node_type p, IntRef k)  // CALL-BY-REF
    {
        if (target < p.key[1]) {
            k.number = 0;
            return false;
        }
        else {
            k.number = p.count;
            while ((target < p.key[k.number]) && k.number > 1) {
                k.number--;
                step++;
            }
            return (target == p.key[k.number]);
        }
    }
    
    // Convert #15
    static void Split(char x, Node_type xr, Node_type p, int k, CharRef y, NodeRef yr) // CALL-BY-REF
    {
        int median;
        
        if (k <= MIN)
            median = MIN;
        else
            median = MIN + 1;
        
        yr.node =  new Node_type();
        for (int i = median + 1; i <= MAX; i++) {
            yr.node.key[i-median] = p.key[i];
            yr.node.branch[i-median] = p.branch[i];
        }
        
        yr.node.count = MAX - median;
        p.count = median;
        
        if (k <= MIN){
            PushIn(x, xr, p, k);
        }
        else {
            PushIn(x, xr, yr.node, k - median);
        }
        
        y.key = p.key[p.count];
        yr.node.branch[0] = p.branch[p.count];
        p.count--;
        
        
    }
    
    // Convert #16
    static Boolean PushDown(char newkey, Node_type p, CharRef x, NodeRef xr) // CALL-BY-REF
    {
        IntRef k = new IntRef();
        
        if (p == null) {
            x.key = newkey;
            xr.node = null;
            return true;
        }
        else {
            if (SeqSearch(newkey, p, k))
                Error("inserting duplicate key");
            
            if ( PushDown(newkey, p.branch[k.number], x, xr) ) {
                if (p.count < MAX) {
                    PushIn(x.key, xr.node, p, k.number);
                    return false;
                }
                else {
                    Split(x.key, xr.node, p, k.number, x, xr);
                    return true;
                }
            }
            
            return false;
        }
    }
    
    // Convert #17
    static Boolean RecDelete(char target, Node_type p)
    {
        IntRef k = new IntRef();
        Boolean found;
        
        if (p == null) {
            return false;
        }
        else {
            found = SeqSearch(target, p, k);
            if (found) {
                if (p.branch[k.number-1] != null) {
                    Successor(p, k.number);
                    if ( !(found = RecDelete(p.key[k.number], p.branch[k.number])) )
                        Error("Key not found.");
                }
                else
                    Remove(p, k.number);
            }
            else
                found = RecDelete(target, p.branch[k.number]);
            
            if (p.branch[k.number] != null) {
                if (p.branch[k.number].count < MIN)
                    Restore(p, k.number);
            }
            return found;
        }
    }
    
    
    // Convert #18
    static Node_type Delete(char target, Node_type root)
    {
        Node_type p,t;
        
        t = root;
        if (!RecDelete(target, t))
            Error("Target was not in the B-tree.");
        else {
            if (root.count == 0) {
                p = root;
                root = root.branch[0];
            }
        }
        return root;
    }
    
    // Convert #19
    static Node_type Insert(char newkey, Node_type root)
    {
        CharRef x = new CharRef();
        NodeRef xr = new NodeRef();
        Node_type p;
        Boolean pushup;
        
        pushup = PushDown(newkey, root, x, xr);
        if (pushup) {
            p = new Node_type();
            p.count = 1;
            p.key[1] = x.key;
            p.branch[0] = root;
            p.branch[1] = xr.node;
            return p;
        }
        return root;
    }
    
} // end Btree class
