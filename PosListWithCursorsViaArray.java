/** Java class that implements the PositionalListWithCursors interface, in
**  which the list has an array-based representation.
**
**  @author R. McCloskey
**  @version April 2012
**/

public class PosListWithCursorsViaArray<T> 
   implements PositionalListWithCursors<T> 
{

   /****  instance variables  ****/

   private T[] contents;       // array in which list items are stored
   private int[] prev;         // predecessor pointers
   private int[] next;         // successor pointers
   private int[] refCntr;      // refCntr[i] = # cursors at position i
   private int frontLoc;       // location of front
   private int numItems;       // # items in list
   private int availLoc;       // location of first available location

   private final int INIT_ARY_LEN = 8;
   private final int MIN_ARY_LEN = 8;


   /****  c o n s t r u c t o r  ****/

   public PosListWithCursorsViaArray()
   {
      contents = (T[])(new Object[INIT_ARY_LEN]);
      prev     = new int[INIT_ARY_LEN];
      next     = new int[INIT_ARY_LEN];
      refCntr  = new int[INIT_ARY_LEN];
      numItems = 0;
      frontLoc = 0;
      availLoc = -1;
      prev[0] = -1;
      next[0] = -1;
   }


   /****  o b s e r v e r s  ****/

/*
   // temporary method for debugging purposes
   public void printRefCntrs() {
      int i = 0;  // rear index
      do {
         System.out.print(refCntr[i] + " ");
         i = prev[i];
      } while (i != -1);
      System.out.println();
   }
*/


   public boolean isEmpty() { return numItems == 0; }

   public int lengthOf() { return numItems; }

   public String toString()
   {
      String result;

      if (isEmpty()) { 
         result = "()";
      }
      else {
         result = new String(")");
         PositionalListCursor<T> c = getCursor().toRear().toPrev();

         while (!c.atFront())
         {
            result = ", " + c.getItem().toString() + result;
            c.toPrev();
         }
         result = "(" + c.getItem().toString() + result;
         c.dispose();
      }
      return result;
   }


   /****  c u r s o r   g e n e r a t i o n   ****/

   /** Returns a new cursor positioned at the rear of this list.
   */
   public PositionalListCursor<T> getCursor() { return new Cursor(this); }


   /*  c o n t e n t   m u t a t o r s  */


   /****  Nested class that implements the cursor concept.  ****/
   
   private class Cursor implements PositionalListCursor<T> {

      /**** instance variables ****/

      private PosListWithCursorsViaArray<T> myList;  // this cursor's list
      private int loc;   // array index corresponding to cursor's position
      

      public Cursor(PosListWithCursorsViaArray<T> list)
      {
         myList = list;
         loc = 0;  /* rear */
         myList.refCntr[0]++;  // # of cursors at rear is irrelevant
      }

      // doesn't work as hoped, as even an explicit call to it
      // is not necessarily executed until later
      /*
         public void finalize() {
            System.out.println("executing finalize()");
            if (loc != -1) {
               myList.refCntr[loc]--;
               loc = -1;
            }
         }
      */

      // Records that the cursor is no longer in use; this method should
      // be called whenever a cursor logically ceases to exist, so that
      // reference counts can be kept accurate.
      // (This method is in place of finalize(), because even an explicit
      // call to that method need not be executed until later.)
      //
      public void dispose() {
         if (loc != -1) {
            myList.refCntr[loc]--;
            loc = -1;
         }
      }
      


      /***  observers  ****/

      public boolean atFront() { return loc == myList.frontLoc; }

      public boolean atRear() { return loc == 0; }

      public T getItem() throws PositionalListCursorException
      {
         if (atRear()) {
            throw new PositionalListCursorException(
                      "getItem() requires !atRear()"); 
         }
         return myList.contents[loc];
      }

 
      public boolean equals(PositionalListCursor<T> cur)   // <T>??
      {
         Cursor c;
         try {                    // used this approach because
            c = (Cursor)cur;      // cur instanceof Cursor  won't compile!
         }
         catch (Exception e) { 
            return false;
         }
         return myList == c.myList  &&  loc == c.loc;
      }


      public PositionalListWithCursors<T> getList() { return myList; }


      /* navigation mutators */

      public PositionalListCursor<T> toFront()
      { 
         return changeLocTo(myList.frontLoc);
      }

      public PositionalListCursor<T> toRear() 
      { 
         return changeLocTo(0);
      }

      public PositionalListCursor<T> toNext() 
         throws PositionalListCursorException 
      {
         if (atRear()) { 
            throw new PositionalListCursorException(
                      "toRear() requires !atRear()"); 
         }
         return changeLocTo(myList.next[loc]);
      }

      public PositionalListCursor<T> toPrev()
         throws PositionalListCursorException
      {
         if (atFront()) {
            throw new PositionalListCursorException(
                      "toFront() requires !atFront()"); 
         }
         return changeLocTo(myList.prev[loc]);
      }


      public PositionalListCursor<T> setTo(PositionalListCursor<T> cur)
      {
         Cursor c;

         try {
            c = (Cursor) cur;
         }
         catch (Exception e) {
            throw new IllegalArgumentException(
                      "setTo() requires that cursor argument be of same kind");
         }

         if (myList != c.myList) {
            throw new IllegalArgumentException(
                      "setTo() requires that cursor argument is in same list");
         }
         return changeLocTo(c.loc);
      }



      /* list mutation */

      public void insert(T item) {

         if (myList.numItems + 1 == myList.contents.length)  // if arrays full,
            { myList.changeAryLen(2 * myList.numItems); }    // double lengths
         else
            { }

         int newLoc;   // array location at which to store new item

         if (myList.availLoc == -1)           // no locations on avail chain
            { newLoc = myList.numItems + 1; } // so take first never-used loc
         else {                               
            newLoc = myList.availLoc;      // grab 1st location from avail chain
            myList.availLoc = myList.next[myList.availLoc];
         }

         myList.numItems++;
      
         myList.contents[newLoc] = item;  // fill in contents of new node and
         myList.next[newLoc] = loc;       // make it point to its successor 

         if (atFront()) {                  // new node is at the front
            myList.frontLoc = newLoc;
            myList.prev[newLoc] = -1;
         }
         else {                              
            int prevLoc = myList.prev[loc];  // make the new node and its 
            myList.next[prevLoc] = newLoc;   // predecessor point to each other
            myList.prev[newLoc] = prevLoc; 
         }

         myList.prev[loc] = newLoc;        // new node is cursor's predecessor
      }

  
      public T remove() throws PositionalListCursorException
      {

         T result;
         
         if (atRear()) {
            throw new PositionalListCursorException(
                      "remove() requires !atRear()"); 
         }

         if (myList.refCntr[loc] > 1) {
            throw new PositionalListCursorException(
                      "cannot remove node at which there are multiple cursors");
         }

         result = getItem();  // remember removed item

         int nextLoc = myList.next[loc];  // location of our successor

         // make our successor point back to our predecessor
         myList.prev[nextLoc] = myList.prev[loc];

         if (atFront()) {                // removed node was at front
            myList.frontLoc = nextLoc;   // so its succssor becomes front
         }
         else {                          // make predecessor point to successor
            myList.next[myList.prev[loc]] = nextLoc; 
         }

         // add vacated slot to Avail chain
         myList.next[loc] = availLoc;
         myList.availLoc = loc;
         changeLocTo(nextLoc);  //advance cursor to successor of removed node
         myList.numItems--;

         // cut arrays in half if they are less than a quarter full and
         // if such a cut keeps them above minimum length
         if (myList.numItems < (myList.contents.length / 4)  &&
             myList.contents.length >= (2 * myList.MIN_ARY_LEN))
         { 
            myList.changeAryLen( myList.contents.length / 2 );
         }

         return result;
      }


      public T replace(T item) throws PositionalListCursorException
      {
         T result; 

         if (atRear()) {
            throw new PositionalListCursorException(
                      "replace() requires !atRear()");
         }

         result = myList.contents[loc];
         myList.contents[loc] = item;
         return result;
      }

      public void swapItems(PositionalListCursor<T> c)
      {
         T temp = getItem();
         replace(c.getItem());
         c.replace(temp);
      }

      public PositionalListCursor<T> clone()
      {
         return myList.getCursor().setTo(this);
      }


      /* Changes the cursor to the specified location in the array,
      ** taking care to update reference counters appropriately.
      */
      private PositionalListCursor<T> changeLocTo(int newLoc) {
         myList.refCntr[loc]--;
         loc = newLoc;
         myList.refCntr[loc]++;
         return this;
      }

   } // end Cursor class


   /* Changes lengths of contents[], prev[], and next[] to newLen
   ** pre: newLen >= this.numItems (to avoid loss of data)
   */
   private void changeAryLen(int newLen) {

      T[] newContents = (T[])(new Object[newLen]);
      int[] newPrev = new int[newLen];
      int[] newNext = new int[newLen];
      int[] newRefCntr = new int[newLen];
      for (int i=0; i <= numItems; i++) {
         newContents[i] = contents[i];
         newPrev[i] = prev[i];
         newNext[i] = next[i];
         newRefCntr[i] = refCntr[i];
      }
      contents = newContents;
      prev = newPrev;
      next = newNext;
      refCntr = newRefCntr;
   }

}
