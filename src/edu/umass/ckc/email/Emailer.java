package edu.umass.ckc.email;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
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
 */

public class Emailer {

    private static File mailLog=null;

  public Emailer() {
  }

    public Emailer(File mailLog) {
        this.mailLog = mailLog;
    }

    /**
 * send an email to user with password info
 * @param userName
 * @param password
 * @param email
 * @return boolean
 */
 public static boolean sendPassword(String techEmail, String emailHost,String userName, String password, String email)
 {
  String body="Your login is: "+userName+'\n'+
              "Your password is: "+password;
  boolean mailSent = Mailer.send(email, techEmail,
          "Mathspring message ", body, emailHost);
  return mailSent;
 }

    public static boolean sendEmail(String toAddress, String fromAddress, String emailHost, String subject, String msg) throws IOException {
       Date date = new Date(System.currentTimeMillis());
       Calendar calendar = new GregorianCalendar();
       StringBuffer errorMsg = new StringBuffer();
       errorMsg.append("Error occurred on: ").append(date.toString()).append(" at: ");
       errorMsg.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":");
       errorMsg.append(calendar.get(Calendar.MINUTE)).append(":");
       errorMsg.append(calendar.get(Calendar.SECOND)).append('\n');
       errorMsg.append("message: ");
       errorMsg.append(msg).append('\n');

       boolean mailSent = Mailer.send(toAddress, fromAddress,
               subject, errorMsg.toString(), emailHost);
       writeSuccess(toAddress, errorMsg.toString());
       return mailSent;
     }

    private static void writeException (String to, Exception e) throws IOException {
        if (mailLog != null) {
        PrintWriter fw = new PrintWriter(new FileWriter(mailLog,true));
        fw.printf("Error sending email to %s",to);
        e.printStackTrace(fw);
        fw.close();
        }
    }

    private static void writeSuccess(String to, String subj) throws IOException {
        if (mailLog != null) {
        PrintWriter fw = new PrintWriter(new FileWriter(mailLog,true));
        Date d = new Date(System.currentTimeMillis());
        fw.printf("Sent Email to %s subj=%s at %s",to,subj, d.toString());
        fw.close();
        }
    }

  /**
  * send an email to tech support with error info
  *
   * @param subject
   * @param exception
   * @return boolean
  */
  public static boolean sendErrorEmail(String techEmail, String emailHost, String subject, String msg, Throwable exception)
  {
    Date date = new Date(System.currentTimeMillis());
    Calendar calendar = new GregorianCalendar();
    StringBuffer errorMsg = new StringBuffer();
    errorMsg.append("Error occurred on: ").append(date.toString()).append(" at: ");
    errorMsg.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":");
    errorMsg.append(calendar.get(Calendar.MINUTE)).append(":");
    errorMsg.append(calendar.get(Calendar.SECOND)).append('\n');
    errorMsg.append("message: ");
    errorMsg.append(msg).append('\n');
    if(exception!=null){
      errorMsg.append(exception.getMessage()).append('\n');
      errorMsg.append(getErrorMsg(exception));
    }
    boolean mailSent = Mailer.send(techEmail, techEmail,
            subject, errorMsg.toString(), emailHost);
    return mailSent;
  }

  /**
  * get the error info out of the exception
  * @param exception
  * @return boolean
  */
  public static String getErrorMsg(Throwable exception)
  {
    StringBuffer errorMsg=new StringBuffer();
     StackTraceElement[] elems = exception.getStackTrace();
     for(int i=0;i<elems.length;i++)
     {
       errorMsg.append(elems[i]).append('\n');
     }
    return errorMsg.toString();
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
