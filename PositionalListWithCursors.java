/** An instance of an implementing class represents a list in which one
**  uses a cursor (specifically, an instance a class that implements the
**  interface PositionalListCursor) in order to navigate among (and access 
**  the data items associated to) the nodes in the list.
*/
public interface PositionalListWithCursors<T> {

   /** Reports whether or not the list has any items in it.
   */
   public boolean isEmpty();

   /** Reports the number of items in the list.
   */
   public int lengthOf();

   /** Returns a cursor for the list.
   */
   public PositionalListCursor<T> getCursor();

}
