package edu.umass.ckc.wo.ttmain.ttservice.util;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import com.sun.mail.smtp.SMTPTransport;

import edu.umass.ckc.wo.tutor.Settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;



/**
 * <p>Title: </p>
 * <p>Description: Static methods to send various types of email
 * messages. </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 * 
 * Frank	06-18-2020	issue #135 added method sendHelpEmail
 */

@Service
public class SendEM {


  public SendEM() {
  }


    /**
 * send an msg to user with password info
 * @param userName
 * @param password
 * @param email
 * @return boolean
 */
 public boolean sendPassword(Connection connection, String techEmail, String emailHost,String userName, String password, String email)
 {
  String body="Your login is: "+userName+'\n'+
              "Your password is: "+password;

  boolean mailSent = send(connection, email, techEmail,"Mathspring message ", body, emailHost);

  return mailSent;
 }
 public boolean sendHelpEmail(Connection connection, String helpDeskEmail, String subject, String userEmail, String message)
 {
	boolean mailSent = false;
	String body="My email is: "+userEmail+'\n'+ "My message is: "+message;;
  
	String toAddrs[] = helpDeskEmail.split(",");
	for(int i=0;i<toAddrs.length;i++) {
		mailSent = send(connection, toAddrs[i], "Mathspring@cs.umass.edu", subject, body, Settings.mailServer);
  	}
	  
	return mailSent;
 }
 
    public boolean sendEmail(Connection connection, String toAddress, String fromAddress, String emailHost, String subject, String body) throws IOException {
       Date date = new Date(System.currentTimeMillis());
       Calendar calendar = new GregorianCalendar();
       StringBuffer errorMsg = new StringBuffer();
       errorMsg.append("Error occurred on: ").append(date.toString()).append(" at: ");
       errorMsg.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":");
       errorMsg.append(calendar.get(Calendar.MINUTE)).append(":");
       errorMsg.append(calendar.get(Calendar.SECOND)).append('\n');
       errorMsg.append("message: ");
       errorMsg.append(body).append('\n');

       boolean mailSent = send(connection, toAddress, fromAddress, subject, errorMsg.toString(), emailHost);
       //writeSuccess(toAddress, errorMsg.toString());
       return mailSent;
     }
/**
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
*/
  /**
  * send an email to tech support with error info
  *
   * @param subject
   * @param exception
   * @return boolean
  */
  public boolean sendErrorEmail(Connection connection, String techEmail, String emailHost, String subject, String msg, Throwable exception)
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

    boolean mailSent = send(connection, techEmail, techEmail, subject, errorMsg.toString(),emailHost);
    return mailSent;
  } 
  /**
  * get the error info out of the exception
  * @param exception
  * @return boolean
  */
  public String getErrorMsg(Throwable exception)
  {
    StringBuffer errorMsg=new StringBuffer();
     StackTraceElement[] elems = exception.getStackTrace();
     for(int i=0;i<elems.length;i++)
     {
       errorMsg.append(elems[i]).append('\n');
     }
    return errorMsg.toString();
  }

    public boolean isValidEmailAddress(String aEmailAddress){
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

      private boolean hasNameAndDomain(String aEmailAddress){
        String[] tokens = aEmailAddress.split("@");
        return
         tokens.length == 2 &&
         tokens[0].length() > 0 &&
         tokens[1].length() > 0 ;
      }
      private String setPwd(Connection connection) {
      	String ep = "dummy";
      	try {
      		ep = Settings.getString(connection, "misc_code");

      	}
      	catch (Exception e) {
      		System.out.println(e.getMessage());
      	}        	
  		return ep;
      }
      
        
      public boolean send(Connection connection, String t, String f, String subj, String b,  String h)
      {
      	String to = "";
      	String[] toArray = new String[0];
      	String from = "";
          String subject = "";
          String body = "";
          String[] filenames = new String[0];
          String[][] filenamesAndData = new String[0][0];
          String host = "";
      	   	
          to = t;
          from = f;
          subject = subj;
          body = b;
          host = h;

      	Properties props = System.getProperties();
          
        

          // Setup mail server
          props.put("mail.smtp.host", host);
          props.put("mail.smtp.auth", "true");
          props.put("mail.smtp.port", "25"); // default port 25

          	if (filenames.length > 0)
              {
                      try
                      {
                              
                              // Get session
                              Session session = Session.getInstance(props, null);

                              // Define message
                              Message message = new MimeMessage(session);
                              message.setFrom(new InternetAddress(from));
                              if (toArray.length > 0) {
                              	for (int i = 0; i < toArray.length; i++)
                              	{
                              		message.addRecipient(Message.RecipientType.TO, new InternetAddress(toArray[i]));
                              	}
                              }
                              else {
                              	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                              }
                              message.setSubject(subject);

                              // Create the message part
                              BodyPart messageBodyPart = new MimeBodyPart();

                              // Fill the message
                              messageBodyPart.setText(body);

                              // Create a Multipart
                              Multipart multipart = new MimeMultipart();

                              // Add part one
                              multipart.addBodyPart(messageBodyPart);

                              //
                              // Part two is attachment(s)
                              //

                              int numAttachments = filenames.length;
                              for (int i = 0; i < numAttachments; i++)
                              {
                                      // Create second body part
                                      messageBodyPart = new MimeBodyPart();

                                      // Get the attachment
                                      DataSource source = new FileDataSource(filenames[i]);

                                      // Set the data handler to the attachment
                                      messageBodyPart.setDataHandler(new DataHandler(source));

                                      // Set the filename
                                      String fs = "/";
                                      String tempName = filenames[i].substring(filenames[i].lastIndexOf(fs)+1);
                                      messageBodyPart.setFileName(tempName);

                                      // Add part two
                                      multipart.addBodyPart(messageBodyPart);


                                      // Put parts in message
                                      message.setContent(multipart);
                              }

                              // Send the message
                              SMTPTransport tp = (SMTPTransport) session.getTransport("smtps");
                              tp.connect(host, "mathspring@cs.umass.edu", setPwd(connection));
                              tp.sendMessage(message, message.getAllRecipients());
                              tp.close();
                              
                              // Inform user that mail was sent successfully
                              return true;
                      }
                      catch (Exception e)
                      {
                              e.printStackTrace();
                              return false;
                      }
              }
              else if (filenamesAndData.length > 0)
              {
                      try
                      {

                              // Get session
                              Session session = Session.getInstance(props, null);

                              // Define message
                              Message message = new MimeMessage(session);
                              message.setFrom(new InternetAddress(from));
                              if (toArray.length > 0) {
                              	for (int i = 0; i < toArray.length; i++)
                              	{
                              		message.addRecipient(Message.RecipientType.TO, new InternetAddress(toArray[i]));
                              	}
                              }
                              else {
                              	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                              }
                              message.setSubject(subject);

                              // Create the message part
                              BodyPart messageBodyPart = new MimeBodyPart();

                              // Fill the message
                              messageBodyPart.setText(body);

                              // Create a Multipart
                              Multipart multipart = new MimeMultipart();

                              // Add part one
                              multipart.addBodyPart(messageBodyPart);

                              //
                              // Part two is attachment(s)
                              //

                              int numAttachments = filenamesAndData.length;
                              for (int i = 0; i < numAttachments; i++)
                              {
                                      // Create second body part
                                      messageBodyPart = new MimeBodyPart();

                                      // Get the attachment
                                      messageBodyPart.setText(filenamesAndData[i][1]);

                                      // Set the filename
                                      messageBodyPart.setFileName(filenamesAndData[i][0]);

                                      // Add part two
                                      multipart.addBodyPart(messageBodyPart);

                                      // Put parts in message
                                      message.setContent(multipart);
                              }

                              // Send the message
                              SMTPTransport tp = (SMTPTransport) session.getTransport("smtps");
                              tp.connect(host, "mathspring@cs.umass.edu", setPwd(connection));
                              tp.sendMessage(message, message.getAllRecipients());
                              tp.close();

                              // Inform user that mail was sent successfully
                              return true;
                      }
                      catch (Exception e)
                      {
                              e.printStackTrace();
                              return false;
                      }
              }
              else
              {
                      try
                      {

                              // Get session
                              Session session = Session.getInstance(props, null);

                              // Define message
                              Message message = new MimeMessage(session);
                              message.setFrom(new InternetAddress(from));
                              if (toArray.length > 0) {
                              	for (int i = 0; i < toArray.length; i++)
                              	{
                              		message.addRecipient(Message.RecipientType.TO, new InternetAddress(toArray[i]));
                              	}
                              }
                              else {
                              	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                              }
                              message.setSubject(subject);

                              // Fill the message
                              message.setText(body);
                            
                              // Send the message
                              SMTPTransport tp = (SMTPTransport) session.getTransport("smtps");
                              tp.connect(host, "mathspring@cs.umass.edu", setPwd(connection));
                              tp.sendMessage(message, message.getAllRecipients());
                              tp.close();

                              // Inform user that mail was sent successfully
                              return true;
                      }
                      catch (Exception e)
                      {
                            //  System.out.println(e.getMessage()+" error mailer ");
                          e.printStackTrace();
                              return false;
                      }
              }
      }

}
