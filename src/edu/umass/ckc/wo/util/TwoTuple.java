package edu.umass.ckc.wo.util;

/**
 * a simple class to hold two java objects as a pair.  Useful for storing
 * pair objects in container classes (e.g. Hashtable).
 */
public class TwoTuple
{
    /** the first object */
    public Object first_=null;
    /** the second object */
    public Object second_=null;

    public int firstInt_;
    public int secondInt_;

    /**
     * construct a pair with two null objects
     */
    public TwoTuple()
    {
        first_ = null;
        second_ = null;
    }

    public TwoTuple (int i, int j) {
        firstInt_ = i;
        secondInt_ = j;
    }
    /**
     * construct a pair given using the args (the arguments are not
     * copied, just referenced)
     * @param first the first object
     * @param second the second object
     */
    public TwoTuple(Object first, Object second)
    {
        first_ = first;
        second_ = second;
    }

    public int getFirstInt () { return firstInt_; }
    public int getSecondInt () { return secondInt_; }
    /** get the first object
     */
    public Object getFirst() { return first_; }
    /** get the second object
     */
    public Object getSecond() { return second_; }
    /** set the first object (this does not make a copy of the argument)
     * @param value the first object
     */
    /** get the first object as a string
     */
    public String getFirstString() { return (String)first_; }
    /** get the second object as a string
     */
    public String getSecondString() { return (String)second_; }
    /** set the first object (this does not make a copy of the argument)
     * @param value the first object
     */
    public void setFirst(Object value) { first_ = value; }
    /** set the first object (this does not make a copy of the argument)
     * @param value the first object
     */
    public void setSecond(Object value) { second_ = value; }
    /** set the first and second object (this does not make a copy of the argument)
     * @param value the first object
     * @param value the second object
     */
    public void set(Object first, Object second)
    {
        first_ = first;
        second_ = second;
    }

    public String toString () {
        if (first_ == null)
            return "<"+firstInt_+","+secondInt_+">";
        else
            return "<"+first_+","+second_+">";
    }

    public boolean equals (TwoTuple t) {
        return t.first_ == this.first_ &&
            t.second_ == this.second_ &&
            t.firstInt_ == this.firstInt_ &&
            t.secondInt_ == this.secondInt_  ;
    }

}
