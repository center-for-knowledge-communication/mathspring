package edu.umass.ckc.servlet.servbase;

/** A class that encapsulates the parameters passed in to the servlet.  The ServletEvent is meant to
 *  be subclassed by each servlet author so that a suite a events is available and known by the servlet.
 *  The servlet event gives semantics to the parameters passed into the servlet by providing a specific
 *  interface.
 */

public abstract class ServletEvent {
    protected ServletParams params_;

    protected ServletEvent () {}

    protected ServletEvent(ServletParams p) {
        params_ = p;
    }

    public ServletParams getServletParams () { return params_; }
}