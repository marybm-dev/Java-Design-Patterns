/* btree.c
   
   compile: gcc -o btree btree.c
   run    : btree <btree.dat
*/

#include <stdio.h>
#include <stdlib.h>

#define MAX 4
#define MIN 2
#define MAXD 10
#define MAXWD 50
#define FALSE 0
#define TRUE 1

typedef char Key_type;
typedef int Bool;

typedef struct node_tag{
  int count;
  Key_type key[MAX+1];
  struct node_tag *branch[MAX+1];
}Node_type;

int main(int argc, char *argv[]) {
    if (argc == 1)
    fp = stdin;
    else {
        fp = fopen(argv[1],"r");
        if (fp == NULL) {
            printf("No File: %s\n",argv[1]);
            exit(0);
        }
    }
    root = NULL;
    interact();
    printf("\n");
    
    return(0);
    
}

int step=0;
int cnt[MAXD];
int levels;
int loc[MAXD][MAXWD];
Node_type *ptrs[MAXD][MAXWD];
Node_type *root;
int silent = 0;
FILE *fp;

void Error(char *msg)
{
  printf("%s\n",msg);
}

void DFS(Node_type *p, int depth)
{
    int i;
    
    if (p != NULL) {
        
        if (depth>levels) levels=depth;
        ptrs[depth][cnt[depth]] = p;
        cnt[depth]++;
        
        for (i=0; i<=p->count; i++)
        DFS(p->branch[i],depth+1);
    }
}

void interact()
{
    char cmd,blank,key,xtra;
    int position,i;
    Node_type *node;
    
    root = NULL;
    
    printf("> ");
    
    while (fscanf(fp,"%c",&cmd) != EOF) {
        switch (cmd) {
            case 'q' : exit(0);
            case 'h' : printf("h(elp; q(uit; i(nsert x; d(elete x; f(ind x; p(rint;  A(scending; Descending; C(ount\n");
            fscanf(fp,"%c",&xtra);
            break;
            case 'i' : fscanf(fp,"%c%c%c",&blank,&key,&xtra);
            printf("INSERT: %c\n",key);
            root=Insert(key,root);
            break;
            case 'd' : fscanf(fp,"%c%c%c",&blank,&key,&xtra);
            printf("DELETE: %c\n",key);
            root=Delete(key,root);
            break;
            case 'f' : fscanf(fp,"%c%c%c",&blank,&key,&xtra);
            printf("FIND: %c\n",key);
            node=Search(key,root,&position);
            break;
            case 'p' : fscanf(fp,"%c",&xtra);
            if (silent)
            ListAllNodes(root);
            else {
                printf("B-TREE:\n");
                PrintAllNodes(root);
            }
            break;
            case 'A' : break;
            case 'D' : break;
            case 'C' : break;
        }
        printf("> ");
    }
}

// Convert #1
void ListNode(int depth,int cnt,Node_type *p)
{
    int i;
    
    printf("%d %d ",depth,cnt);
    for (i=1; i<=p->count; i++)
    printf("%c ",p->key[i]);
    printf("\n");
}

// Convert #2
void SetLoc()
{
    int i,depth,pos,base,first,last,dist;
    int x;
    
    depth = levels;
    pos = 0;
    for (i=0; i<cnt[depth]; i++){
        loc[depth][i] = pos;
        pos += (ptrs[depth][i]->count)*2+2;
    }
    for (depth=levels-1; depth>=0; depth--){
        base = 0;
        for (i=0; i<cnt[depth]; i++){
            first = base;
            last  = first+(ptrs[depth][i]->count);
            dist = (loc[depth+1][last] - loc[depth+1][first] + 2*ptrs[depth+1][last]->count)/2;
            loc[depth][i] = loc[depth+1][first]+dist-ptrs[depth][i]->count;
            base += ptrs[depth][i]->count+1;
        }
        
    }
}

// Convert #3
/* MoveRight: move a key to the right. */
void MoveRight(Node_type *p,int k)
{
    int c;
    Node_type *t;
    
    t = p->branch[k];
    for (c=t->count; c>0; c--){
        /* Shift all keys in the right node one position. */
        t->key[c+1]=t->key[c];
        t->branch[c+1]=t->branch[c];
    }
    t->branch[1] = t->branch[0];  /* Move key from parent to right node. */
    t->count++;
    t->key[1]=p->key[k];
    t=p->branch [k-1];  /* Move last key of left node into parent. */
    p->key[k]=t->key[t->count];
    
    
    p->branch[k]->branch[0]=t->branch[t->count];
    t->count--;
}

// Convert #4
/* MoveLeft: move a key to the left. */
void MoveLeft(Node_type *p,int k)
{
    int c;
    Node_type *t;
    
    /*Move key from parent into left node. */
    t = p->branch[k-1];
    t->count++;
    t->key[t->count] = p->key[k];
    t->branch[t->count] = p->branch[k]->branch[0];
    
    /* Move key from right node into parent. */
    t = p->branch[k];
    p->key[k] = t->key[1];
    t->branch[0] = t->branch[1];
    t->count--;
    for (c=1; c<=t->count; c++){
        /* Shift all keys in right node one position leftward. */
        t->key[c] = t->key[c+1];
        t->branch[c] = t->branch[c+1];
    }
}

// Convert #5
/* Combine: combine adjacent nodes. */
void Combine(Node_type *p,int k)
{
    int c;
    Node_type *q; /*points to the right nodei, which will be emptied and deleted*/
    Node_type *l;
    
    q = p->branch[k];
    l = p->branch[k-1];  /* Work with the left node. */
    l->count++;          /* Insert the key from the parent. */
    l->key[l->count] = p->key[k];
    l->branch[l->count] = q->branch[0];
    for (c=1; c<=q->count; c++){  /* Insert all keys from right node. */
        l->count++;
        l->key[l->count] = q->key[c];
        l->branch[l->count] = q->branch[c];
    }
    for (c=k; c<p->count; c++){ /* Delete key from parent node. */
        p->key[c] = p->key[c+1];
        p->branch[c] = p->branch[c+1];
    }
    p->count--;
    free(q);    /* Dispose of the empty right node. */
}

// Convert #6
/* Restore: finds a key and inserts it into p->branch[k] */
void Restore(Node_type *p, int k)
{
    if (k==0)                /* case: leftmost key */
    if (p->branch[1]->count >MIN)
    MoveLeft(p,1);
    else
    Combine(p,1);
    else if (k==p->count)    /* case: rightmost key */
    if (p->branch[k-1]->count >MIN)
    MoveRight(p,k);
    else
    Combine(p,k);
    
    else if (p->branch[k-1]->count>MIN) /*remaining cases */
    MoveRight(p,k);
    else if (p->branch[k+1]->count>MIN)
    MoveLeft(p,k+1);
    else
    Combine(p,k);
}

// Convert #7
/* Remove: removes key[k] and branch[k] from *p */
void Remove(Node_type *p,int k)
{
    int i;  /* index to move entries */
    for (i=k+1; i<=p->count; i++){
        p->key[i-1] = p->key[i];
        p->branch[i-1] = p->branch[i];
    }
    p->count--;
}

// Convert #8
/* Successor: replaces p->key[k] by its immediate successor under
 natural order */
void Successor(Node_type *p, int k)
{
    Node_type *q;  /* used to move down the tree to a leaf */
    for (q=p->branch[k]; q->branch[0]; q=q->branch[0]) 
    ;
    p->key[k]=q->key[1];
}

// Convert #9
/* PushIn: inserts key x and pointer xr into node p at position k;
 requires that the node was not previously full. */
void PushIn(Key_type x,Node_type *xr,Node_type *p, int k)
{
    int i;		/* index to move keys to make room for x */
    
    for (i = p->count; i > k; i--) {
        /* Shift all the keys and branches to the right. */
        p->key[i+1] = p->key[i];
        p->branch[i+1] = p->branch[i];
    }
    p->key[k+1] = x;
    p->branch[k+1] = xr;
    p->count++;
}

// Convert #10
void PrintNode(Node_type *p, int depth, int i, int *pos)
{
  int j;
  while ((*pos)<loc[depth][i]){
    printf(" ");
    (*pos)++;
  }
  printf("[");
  for (j=1; j<p->count; j++){
    printf("%c,",p->key[j]);
    (*pos) += 2;
  }
  printf("%c]",p->key[j]);
  (*pos)+=3;
}

// Convert #11
void PrintTop(Node_type *p, int depth, int i, int *pos, int *child)
{
  int j,strt,mid;

  mid = loc[depth][i]+p->count;
  for (j=0; j<=p->count; j++)
  {
    
    strt = loc[depth+1][*child]+1;
    if (strt>=mid)
      strt += 2*p->branch[j]->count-2;

    while ((*pos)<strt){
      printf(" ");
      (*pos)++;
    }
  
    if (strt<=mid)
      printf("/");
    else
      printf("\\");
    (*pos)++; 
  
    (*child)++;
  }
}

// Convert #12
void PrintLine(Node_type *p, int depth, int i, int *pos, int *child)
{
  int strt,stop,mid;

  strt = loc[depth+1][*child]+2;
  
  stop = loc[depth+1][*child+p->count]+p->branch[p->count]->count*2-2;
  
  mid = loc[depth][i]+p->count;

  *child += p->count+1;

  while ((*pos)<strt){
    printf(" ");
    (*pos)++;
  }
  
  while ((*pos)<=stop){
    if (*pos==mid) 
      printf("|");
    else
      printf("_");
    (*pos)++;
  }
}

// Convert #13
void PrintAllNodes(Node_type *root)
{
  int i,depth,pos,child;

  if (root==NULL)
    printf("EMPTY\n");
  else{

  for (depth=0; depth<MAXD; depth++) {
    cnt[depth]=0;
  }
      
  levels=0; 
  DFS(root,0);
  SetLoc();
  printf("L\n");
      
  for (depth=0; depth<=levels; depth++){
    printf("%d",depth);
    pos=-1;
      
    for (i=0; i<cnt[depth]; i++)
      PrintNode(ptrs[depth][i],depth,i,&pos);
      
    printf("\n");
      
    if (depth < levels){
      pos=-2;
      child=0;
        
      for (i=0; i<cnt[depth]; i++)
        PrintLine(ptrs[depth][i],depth,i,&pos,&child);
        
      printf("\n");
      pos=-2;
      child=0;
        
      for (i=0; i<cnt[depth]; i++)
        PrintTop(ptrs[depth][i],depth,i,&pos,&child);
        
      printf("\n");
    } 
  } 
  printf("\n");
  }
}

// Convert #14
/* SeqSearch: SEQUENTIAL SEARCH - searches keys in node p for target;
 returns location k of target, or branch on which to continue search */
Bool SeqSearch(Key_type target,Node_type *p, int *k)
{
    
    if (target < p->key[1]) {
        *k = 0;
        return FALSE;
    }
    else {
        
        /* Sequential Search */
        *k = p->count;
        while ((target<p->key[*k]) && *k > 1){
            (*k)--;
            step++;
        }
        
        
        return (target==p->key[*k]);
    }
}

// Convert #15
/*Split: splits node *p with key x and pointer xr at position k into
 nodes *p and *yr with median key y */
void Split(Key_type x, Node_type *xr, Node_type *p, int k, Key_type *y, Node_type **yr)
{
    int i;		/* used for copying from *p to new node  */
    int median;
    
    if (k <= MIN)
    median = MIN;
    else
    median = MIN + 1;
    /* Get a new node and put it on the right. */
    *yr = (Node_type *)malloc(sizeof(Node_type));
    for (i = median+1; i <= MAX; i++) {  /* Move half the keys. */
        (*yr)->key[i-median] = p->key[i];
        (*yr)->branch[i-median] = p->branch[i];
    }
    (*yr)->count = MAX - median;
    p->count = median;
    if (k <= MIN)	/* Push in the new key. */
    PushIn(x,xr,p,k);
    else
    PushIn(x,xr,*yr,k - median);
    *y = p->key[p->count];
    (*yr)->branch[0] = p->branch[p->count];
    p->count--;
}

// Convert #16
/* PushDown: recursively move down tree searching for newkey. */
Bool PushDown(Key_type newkey,Node_type *p,Key_type *x,
              Node_type **xr)
{
    int k;   /* branch on which to continue the search    */
    if (p == NULL) {  /* cannot insert into empty tree; terminates */
        *x = newkey;
        *xr = NULL;
        return TRUE;
    } else {          /* Search the current node. */
        if (SeqSearch(newkey,p,&k))
        Error("inserting duplicate key");
        if (PushDown(newkey,p->branch[k],x,xr))
            /* Reinsert median key.*/
            if (p->count < MAX) {
                PushIn(*x,*xr,p,k);
                return FALSE;
            } else {
                Split(*x,*xr,p,k,x,xr);
                return TRUE;
            }
        return FALSE;
    }
}

// Convert #17
/*RecDelete: look for target to delete*/
Bool RecDelete(Key_type target,Node_type *p)
{
    int k;  /* location of target or of branch on which to search*/
    Bool found;
    
    if (p==NULL)
    return FALSE;    /*Hitting an empty tree is an error */
    else{
        
        found=SeqSearch(target,p,&k);
        if (found)
        if (p->branch[k-1]){     /* test for NULL??? */
            Successor(p,k);  /*replaces key[k] by its successor*/
            if (!(found=RecDelete(p->key[k],p->branch[k])))
            /* We know that the new key[k] is in the leaf. */
            Error("Key not found.");
        }else
        Remove(p,k); /*removes key from position k of *p */
        else                 /*Target was not found in current node.*/
        found=RecDelete(target,p->branch[k]);
        /* At this point, the function has returned from a recursive call.*/
        if (p->branch[k] != NULL)
        if (p->branch[k]->count<MIN)
        Restore(p,k);
        return found;
    }
}

// Convert #18
/* Delete: deletes the key target from the B-tree with the given root */
Node_type *Delete(Key_type target, Node_type *root)
{
    Node_type *p,*t;      /* used to dispose of an empty root */
    
    t = root;
    if (!RecDelete(target, t))
    Error("Target was not in the B-tree.");
    else
    if (root->count == 0) {  /*root is empty. */
        p = root;
        root = root->branch[0];
        free(p);
    }
    return root;
}

// Convert #19
/* Insert: inserts newkey into the B-tree with the given root;
 requires that newkey is not already present in the tree */
Node_type *Insert(Key_type newkey, Node_type *root)
{
    Key_type x;	/* node to be reinserted as new root	*/
    Node_type *xr;	/* subtree on right of x 		*/
    Node_type *p;	/* pointer for temporary use		*/
    Bool pushup; /* Has the height of the tree increased? */
    
    pushup = PushDown(newkey,root,&x,&xr);
    if (pushup) {	/* Tree grows in height.*/
        /* Make a new root: */
        p = (Node_type *)malloc(sizeof(Node_type));
        p->count = 1;
        p->key[1] = x;
        p->branch[0] = root;
        p->branch[1] = xr;
        return p;
    }
    return root;
}


void ListAllNodes(Node_type *root)
{
  int i,j,cnt;

  if (root != NULL) 
  {
    ListNode(0,1,root);
    cnt = 1;
    for (i=0; i<=root->count; i++) 
      if (root->branch[i] != NULL)
        ListNode(1,cnt++,root->branch[i]);

    cnt = 1;
     for (i=0; i<=root->count; i++)  
       if (root->branch[i] != NULL) {
         for (j=0; j<=root->branch[i]->count; j++) 
           if (root->branch[i]->branch[j] != NULL)
             ListNode(2,cnt++,root->branch[i]->branch[j]);
          
         cnt += MAX - root->branch[i]->count;
       }
  }
  printf("-1\n");
  fflush(stdout);
}

/* Search: traverse B-tree looking for target. */
Node_type *Search(Key_type target, Node_type *root, int *targetpos)
{
  if (root == NULL)
    return NULL;
  else 
    if (SeqSearch(target,root,targetpos))
      return root;
    else
      return Search(target,root->branch[*targetpos],targetpos);
}




