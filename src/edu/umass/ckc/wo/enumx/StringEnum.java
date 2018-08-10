package edu.umass.ckc.wo.enumx;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public abstract class StringEnum {

        private final String name_;
        protected String getName() { return name_;}


        protected StringEnum (String name) {
          name_ = name;
        }

        /**
        Object method. final so that it can't be redefined.
        */
        public final boolean equals(Object that) {
                return super.equals(that);
        }


        /**
        Object method. final so that it can't be redefined.
        */
        public final int hashCode() {
                return super.hashCode();
        }


        /**
        Object method.
        */
        public String toString() {
                return name_;
        }
}