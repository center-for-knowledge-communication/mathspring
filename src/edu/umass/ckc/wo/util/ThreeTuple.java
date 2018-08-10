package edu.umass.ckc.wo.util;

/**
 * a simple class to hold three java objects as a tuple.  Useful for storing
 * pair objects in container classes (e.g. Hashtable).
 */
public class ThreeTuple extends TwoTuple
{
    /** the third object
     */
    public Object third_;
    public int thirdInt_;
    /**
     * construct a three tuple with null objects
     */
    public ThreeTuple()
    {
        first_ = null;
        second_ = null;
        third_ = null;
    }

    public ThreeTuple (int i, int j, int k) {
        super(i,j);
        thirdInt_ = k;
    }
    /**
     * construct a three tuple given using the args (the arguments are not
     * copied, just referenced)
     * @param first the first object
     * @param second the second object
     * @param third the third object
     */
    public ThreeTuple(Object first, Object second, Object third)
    {
        first_ = first;
        second_ = second;
        third_ = third;
    }

    /** get third string
     */
    public String getThirdString() { return (String)third_; }

    /** third int
     */
    public int getThirdInt () { return thirdInt_; }
    /** get the third object
     */
    public Object getThird() { return third_; }
    /** set the third object (this does not make a copy of the argument)
     * @param value the third object
     */
    public void setThird(Object value) { third_ = value; }
    /** set the first, second and third object (this does not make a copy of the argument)
     * @param first the first object
     * @param second the second object
     * @param third the third object
     */
    public void set(Object first, Object second, Object third)
    {
        first_ = first;
        second_ = second;
        third_ = third;
    }

    public String toString () {
        if (first_ == null)
            return "<"+firstInt_+","+secondInt_+","+thirdInt_+">";
        else
            return "<"+first_+","+second_+","+third_+">";
    }

    public boolean equals (ThreeTuple t) {
        return super.equals(t) && t.thirdInt_ == this.thirdInt_ && t.third_ == this.third_;
    }

}
