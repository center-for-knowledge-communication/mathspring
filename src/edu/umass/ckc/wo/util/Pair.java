package edu.umass.ckc.wo.util;

/**
 * Created by david on 6/21/2016.
 */
public class  Pair <X,Y> {
    X p1;
    Y p2;

    public Pair(X p1, Y p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public X getP1 () {
        return this.p1;
    }

    public Y getP2 () {
        return this.p2;
    }

    public void setP1 (X p1) {
        this.p1 = p1;
    }

    public void setP2 (Y p2) {
        this.p2 = p2;
    }
}
