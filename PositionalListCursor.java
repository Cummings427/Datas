/** An instance of an implementing class serves as a cursor within a
**  list represented by an instance of a class that implements the
**  PositionalListWithCursors interface.  A cursor is used for navigating
**  among and accessing the items in such a list.
*/

public interface PositionalListCursor<T> extends Cloneable {
   

   /****  observers  ****/

   /** Reports whether or not the cursor is at the front of its list. 
   */
   boolean atFront();


   /** Reports whether or not the cursor is at the rear of its list.
   */
   boolean atRear();


   /** Returns the item associated to the node at which the cursor is
   **  positioned.
   **  excep: if atRear(), throws PositionalListCursorException 
   */
   T getItem() throws PositionalListCursorException;


   /** Reports whether or not the cursor is at the same position as the
   **  specified cursor.
   */
   boolean equals(PositionalListCursor<T> cur);    // <T>?


   /** Returns (a reference to) the list within which the cursor lies.
   */
   PositionalListWithCursors<T> getList();     // <T>?


   /****  navigation mutators  ****/


   /** Places the cursor at the front of its list and returns a reference
   **  to itself.
   **  post: atFront()
   */
   PositionalListCursor<T> toFront(); 


   /** Places the cursor at the rear of its list and returns a reference
   **  to itself.
   **  post: atRear()
   */
   PositionalListCursor<T> toRear();


   /** Moves the cursor one position towards the rear of its list and
   **  returns a reference to itself.
   **  excep: if atRear(), throws PositionalListCursorException 
   */
   PositionalListCursor<T> toNext() throws PositionalListCursorException;


   /** Moves the cursor one position towards the front of its list
   **  and returns a reference to itself.
   **  excep: if atFront(), throws PositionalListCursorException 
   */
   PositionalListCursor<T> toPrev() throws PositionalListCursorException;


   /** Moves this cursor to the same position as the one specified
   **  and returns a reference to itself.
   **  post: equals(cur)
   **  excep: if getList() != cur.getList(), throws IllegalArgumentException
   */
   PositionalListCursor<T> setTo(PositionalListCursor<T> cur);


   /** Signals that this cursor logically has ceased to exist.
   */
   void dispose();


   /***  list mutation  ****/


   /** Inserts into this cursor's list a new node that becomes the predecessor
   **  of this cursor's position and with which is associated the specified
   **  item.
   */
   void insert(T item);


   /** Removes from this cursor's list the node at the cursor's position
   **  and returns the item associated to that node; the cursor's new
   **  position is what had been its successor.
   **  excep: if atRear(), throws PositionalListCursorException 
   */
   T remove() throws PositionalListCursorException;


   /** Replaces the item associated to the node at this cursor's position by
   **  the specified item and returns (a reference to) the replaced item.
   **  excep: if atRear(), throws PositionalListCursorException 
   */
   T replace(T item) throws PositionalListCursorException;


   /** Swaps the items at the nodes indicated by this cursor and the
   **  specified cursor.
   **  excep: if atRear() or c.atRear(), throws PositionalListCursorException 
   */
   void swapItems(PositionalListCursor<T> c);


   /****  clone  ****/

   /** Returns a new cursor that is identical to this one.
   */
   PositionalListCursor<T> clone();
}

