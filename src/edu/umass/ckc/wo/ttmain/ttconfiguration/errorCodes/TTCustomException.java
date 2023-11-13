package edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes;

import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by nsmenon on 6/16/2017.
 * Frank 11-08-23	Re-implemented using ResourceBundle and mathspring.properties.  Bundle location changed to match other translated resources in WEB-INF/classes folder.
 */
public class TTCustomException extends Exception {

    private static final long serialVersionUID = 1L;

	private ResourceBundle rb = null;

	private String errorCode;

    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;

    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public TTCustomException(String errorCode) {

    	Locale loc = new Locale("en","US");	

        try {
           	rb = ResourceBundle.getBundle("MathSpring",loc);
            showErrorMessage(errorCode, loc);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        	rb = null;
        }
    }

    public TTCustomException(String errorCode, Locale loc) {

        try {
           	rb = ResourceBundle.getBundle("MathSpring",loc);
            showErrorMessage(errorCode, loc);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        	rb = null;
        }
    }
    
    
    public String showErrorMessage(String errorCode, Locale loc) {
        this.errorCode = errorCode;
        this.errorMessage = rb.getString(errorCode);
        return rb.getString(errorCode);
    }
}
