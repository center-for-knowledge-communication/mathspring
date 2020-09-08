package edu.umass.ckc.email;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;

import javax.activation.*;

import com.sun.mail.smtp.SMTPTransport;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 * 
 * Frank	05-24-2020 Issue #130 change Transport protocol to smtps
 * Frank	06-03-2020 Issue #141 fix port assignment
 */

public class Mailer extends Object
{
    private static Logger logger = Logger.getLogger(Mailer.class);

	
	private String[] to = new String[0];
        private String from = "";
        private String subject = "";
        private String body = "";
        private String[] filenames = new String[0];
        private String[][] filenamesAndData = new String[0][0];
        private String host = "";

        // use this constructor to send attachments using filenames
        public Mailer(String t, String f, String subj, String b, String[] file, String h)
        {
                String[] temp = {t};
                to = temp;
                from = f;
                subject = subj;
                body = b;
                filenames = file;
                host = h;
        }

        // use this constructor to send attachments when you want to attach a string and specify its filename.
        // [x][0] has filename to send the data as and [x][1] contains the data for that file name
        public Mailer(String t, String f, String subj, String b, String[][] fileData, String h)
        {
                String[] temp = {t};
                to = temp;
                from = f;
                subject = subj;
                body = b;
                filenamesAndData = fileData;
                host = h;
        }

        // use this constructor to send emails w/ no attachments
        public Mailer(String t, String f, String subj, String b, String h)
        {
                String[] temp = {t};
                to = temp;
                from = f;
                subject = subj;
                body = b;
                host = h;
        }

        // use this constructor to send attachments using filenames
        public Mailer(String[] t, String f, String subj, String b, String[] file, String h)
        {
                to = t;
                from = f;
                subject = subj;
                body = b;
                filenames = file;
                host = h;
        }

        // use this constructor to send attachments when you want to attach a string and specify its filename.
        // [x][0] has filename to send the data as and [x][1] contains the data for that file name
        public Mailer(String[] t, String f, String subj, String b, String[][] fileData, String h)
        {
                to = t;
                from = f;
                subject = subj;
                body = b;
                filenamesAndData = fileData;
                host = h;
        }

        // use this constructor to send emails w/ no attachments
        public Mailer(String[] t, String f, String subj, String b, String h)
        {
                to = t;
                from = f;
                subject = subj;
                body = b;
                host = h;
        }

        // sends out the email
        public boolean send()
        {
            Properties props = System.getProperties();
            
            logger.info("sending an email");
            logger.info(host);
            logger.info(to[0]);
            logger.info(from);
            logger.info(subject);
            logger.info(body);
            

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
                                for (int i = 0; i < to.length; i++)
                                {
                                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
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
                                SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
                                t.connect(host, "mathspring@cs.umass.edu", "dummy");
                                t.sendMessage(message, message.getAllRecipients());
                                t.close();
                                
                                // Inform user that mail was sent successfully
                                return true;
                        }
                        catch (Exception e)
                        {
                        		logger.error(e.getMessage());
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
                                for (int i = 0; i < to.length; i++)
                                {
                                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
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
                                SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
                                t.connect(host, "mathspring@cs.umass.edu", "m4thspr1ng!");
                                t.sendMessage(message, message.getAllRecipients());
                                t.close();

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
                                for (int i = 0; i < to.length; i++)
                                {
                                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
                                }
                                message.setSubject(subject);

                                // Fill the message
                                message.setText(body);
                              
                                // Send the message
                                SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
                                t.connect(host, "mathspring@cs.umass.edu", "m4thspr1ng!");
                                t.sendMessage(message, message.getAllRecipients());
                                t.close();

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

        // static method for sending attachments by using file names to a single recepient
        public static boolean send(String t, String f, String subj, String b, String[] file, String h)
        {
                Mailer temp = new Mailer(t, f, subj, b, file, h);
                return temp.send();
        }

        // static method for sending attachments (by using the filenamesAndData 2 dimensional array) to a single recepient
        public static boolean send(String t, String f, String subj, String b, String[][] fileAndData, String h)
        {
                Mailer temp = new Mailer(t, f, subj, b, fileAndData, h);
                return temp.send();
        }

        // static method for attaching and mailing a single file to a single recepient
        public static boolean send(String t, String f, String subj, String b, String file, String h)
        {
                String[] files = {file};
                Mailer temp = new Mailer(t, f, subj, b, files, h);
                return temp.send();
        }

  /**
  *  static method for sending an email w/ no attachments
  *  to a single recepient. Example:
  *  "userName@edlab.cs.umass.edu",
  * "dragon@cs.umass.edu",
  * "Student " + temp.getUserName() + ": Programming Assignment " + hwNumber,
  * "Your results for assignment " + hwNumber + ": \n\n" + temp.getBody(),
  * "localhost"
  *
  * @param t to address
  * @param f from address
  * @param subj subject field
  * @return boolean
  */
public static boolean send(String t, String f, String subj, String b, String h)
        {
                Mailer temp = new Mailer(t, f, subj, b, h);
                return temp.send();
        }

        // static method for sending attachments by using file names to multiple recepients
        public static boolean send(String[] t, String f, String subj, String b, String[] file, String h)
        {
                Mailer temp = new Mailer(t, f, subj, b, file, h);
                return temp.send();
        }

        // static method for sending attachments (by using the filenamesAndData 2 dimensional array) to multiple recepients
        public static boolean send(String[] t, String f, String subj, String b, String[][] fileAndData, String h)
        {
                Mailer temp = new Mailer(t, f, subj, b, fileAndData, h);
                return temp.send();
        }

        // static method for attaching and mailing a single file to multiple recepients
        public static boolean send(String[] t, String f, String subj, String b, String file, String h)
        {
                String[] files = {file};
                Mailer temp = new Mailer(t, f, subj, b, files, h);
                return temp.send();
        }

        // static method for sending an email w/ no attachments to multiple recepients
        public static boolean send(String[] t, String f, String subj, String b, String h)
        {
                Mailer temp = new Mailer(t, f, subj, b, h);
                return temp.send();
        }

        public String[] getTo()
        {
                return to;
        }

        public String getFrom()
        {
                return from;
        }

        public String getSubject()
        {
                return subject;
        }

        public String getBody()
        {
                return body;
        }

        public String[] getFilenames()
        {
                return filenames;
        }

        public String[][] getFilenamesAndData()
        {
                return filenamesAndData;
        }

        public String getHost()
        {
                return host;
        }

        public void setTo(String input)
        {
                String[] temp = {input};
                to = temp;
        }

        public void setTo(String[] input)
        {
                to = input;
        }

        public void setFrom(String input)
        {
                from = input;
        }

        public void setSubject(String input)
        {
                subject = input;
        }

        public void setBody(String input)
        {
                body = input;
        }

        public void setFilenames(String[] input)
        {
                filenames = input;
        }

        public void setFilenamesAndData(String[][] input)
        {
                filenamesAndData = input;
        }

        public void setHost(String input)
        {
                host = input;
        }

    public static void main(String[] args) {
        System.out.println("Trying to run a test of Mailer");
        Mailer m = new Mailer("marshall@cs.umass.edu","marshall@cs.umass.edu","subj","hi there", "loki.cs.umass.edu");
        m.send();
    }
}
