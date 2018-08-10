package edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by nsmenon on 6/16/2017.
 */
public class TTCustomException extends Exception {

    private static final long serialVersionUID = 1L;

    private String errorCode;

    private String errorMessage;

    private Properties propertyLoader;

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
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = TTCustomException.class.getResourceAsStream("ErrorCodeMessage.properties");
            // load a properties file
            prop.load(input);
            this.propertyLoader = prop;
            showErrorMessage(errorCode);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String showErrorMessage(String errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = propertyLoader.getProperty(errorCode);
        return propertyLoader.getProperty(errorCode);
    }
}
