package edu.umass.ckc.email;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import edu.umass.ckc.wo.tutor.Settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * <p>Title: Emailer</p>
 * <p>Description: Static methods to send various types of email
 * messages. </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 * 
 * Frank	09-13-2020	issue #242 remove obsolete code
 */

public class Emailer {

    private static File mailLog=null;

  public Emailer() {
  }

    public static boolean isValidEmailAddress(String aEmailAddress){
        if (aEmailAddress == null) return false;
        boolean result = true;
        try {
          InternetAddress emailAddr = new InternetAddress(aEmailAddress);
          if ( ! hasNameAndDomain(aEmailAddress) ) {
            result = false;
          }
        }
        catch (AddressException ex){
          result = false;
        }
        return result;
      }

      private static boolean hasNameAndDomain(String aEmailAddress){
        String[] tokens = aEmailAddress.split("@");
        return
         tokens.length == 2 &&
         tokens[0].length() > 0 &&
         tokens[1].length() > 0 ;
      }

}
