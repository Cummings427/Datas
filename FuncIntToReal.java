import java.util.Iterator;

/** An instance of a class implementing this interface represents a function
**  mapping integers to reals.  Integers are modeled using values of type int
**  and reals using values of type double.
**
**  Note: in postconditions of mutator methods, old_this refers to the
**  state of "this" object (i.e., the object upon which the method was
**  applied) before execution of the method.
*/

public interface FuncIntToReal{


   /****  o b s e r v e r s  ****/

   /** Reports whether or not the specified integer (k) is in the domain
   **  of this function (i.e., whether or not it makes sense to apply
   **  this function to k).
   */
   boolean isInDomain(int k);


   /** Returns the real value obtained by applying this function to k
   **  (i.e., the value to which this function maps k).
   **  pre: isInDomain(k)
   */
   double applyTo(int k);



   /** Returns the scalar product (borrowing a concept from vectors) 
   **  of this function and the specified function (f).
   **  By definition, the scalar product of functions f and g is the sum,
   **  over all i, of f(i)*g(i).
   **  (Exactly how to deal with values of i in the domain of exactly one
   **  of f or g is left to the implementing class.)
   */
   double scalarProduct(FuncIntToReal f);


   
   /****  g e n e r a t o r s  ****/

   /** Returns the function that is the sum of this function and the
   **  specified function.
   **  By definition, the sum f+g of functions f and g satisfies the
   **  condition that, for all i, (f+g)(i) = f(i) + g(i).
   **  (Exactly how to deal with values of i in the domain of exactly one
   **  of f or g is left to the implementing class.)
   */
   FuncIntToReal sum(FuncIntToReal f);


   /** Returns an iterator that iterates through this function's domain.
   */
   Iterator<Integer> domainIter();


   /****  m u t a t o r s  ****/

   /** Changes this function so that it maps k to the real value specified.
   **  post: this.isInDomain(k)  &&  val == this.applyTo(k) &&
   **        for all m!=k, this.isInDomain(m) == old_this.isInDomain(m) &&
   **        this.applyTo(m) == old_this.applyTo(m) 
   */
   void put(int k, double val);


   /** Changes this function so that k is no longer in its domain.
   **  post: !this.isInDomain(k)  &&  
   **        for all m!=k, this.isInDomain(m) == old_this.isInDomain(m)
   */
   void remove(int k);


   /** Modifies this function by "absorbing" f, meaning that for any i
   **  not (initially) in the domain of this function, the modified
   **  function is put into agreement with f by making it map i to f(i).
   **
   **  post: for all k satisfying this.isInDomain(k) && !f.isInDomain(k),
   **        this.applyTo(k) == old_this.applyTo(k)  &&
   **        for all k satisfying !this.isInDomain(k) && f.isInDomain(k),
   **        this.applyTo(k) == f.applyTo(k)  &&
   **        for all k, this.isInDomain(k) == 
   **                      old_this.isInDomain(k) || f.isInDomain(k)
   */
   void absorb(FuncIntToReal f);

}
