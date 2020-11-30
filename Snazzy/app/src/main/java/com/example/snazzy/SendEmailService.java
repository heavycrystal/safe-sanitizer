package com.example.snazzy;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;


public class SendEmailService {
    private static com.example.snazzy.SendEmailService instance = null;
    private static Context ctx;

    final String username = "apikey";
    final String password = "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";

    Properties prop;
    Session session;
    static final ExecutorService emailExecutor = Executors.newSingleThreadExecutor();

    private SendEmailService(Context context) {
        ctx = context;

        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);

        prop = new Properties();
        prop.put("mail.smtp.host", "smtp.sendgrid.net");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.sendgrid.net");

        session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    public static synchronized com.example.snazzy.SendEmailService getInstance(Context context) {
        if(instance == null) {
            instance = new com.example.snazzy.SendEmailService(context);
        }
        return instance;
    }

    public void SendOTPEmail(String userEmail, int OTP) {
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("illuminatedllama@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(userEmail.trim())
            );
            message.setSubject("Please verify your e-mail address.");
            message.setText("Your OTP for Tedium is: " + "\n\n" + OTP + "\n\n" + "If you did not initiate this request, " +
                    "please ignore this e-mail." + "\n\n" + "Warm Regards," + "\n" + "The Tedium Team");

            Multipart multipart = new MimeMultipart();

            //text
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "<H1>Welcome to Medium!</H1>";
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);

            Transport.send(message);

        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void SendReminderMail(String userEmail, String userName, String reminderText, Uri filename, Context calling_context, long timestamp) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("illuminatedllama@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(userEmail.trim())
            );
            message.setSubject("A friendly reminder from Tedium.");

            Multipart multipart = new MimeMultipart();

            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "Dear " +
                    userName +
                    ",<br>\n" +
                    "<br>\n" +
                    "You have a reminder " +
                    reminderText +
                    " set to occur now.<br>\n" +
                    "<br>\n" +
                    "Please follow attached the documents you chose to attach with this reminder.<br>\n" +
                    "<br>\n" +
                    "Warm regards,<br>\n" +
                    "The Tedium team";
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);

            if(filename != null) {
                Cursor returnCursor =
                        calling_context.getContentResolver().query(filename, null, null, null, null);
                assert returnCursor != null;
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                String name = returnCursor.getString(nameIndex);
                returnCursor.close();

                MimeBodyPart textBodyPart = new MimeBodyPart();
                ByteArrayDataSource tds = new ByteArrayDataSource(calling_context.getContentResolver().openInputStream(filename),
                        calling_context.getContentResolver().getType(filename));
                textBodyPart.setDataHandler(new DataHandler(tds));
                textBodyPart.setFileName(name);
                multipart.addBodyPart(textBodyPart);
            }

            message.setContent(multipart);
            message.addHeader("X-SMTPAPI", "{" + "\"send_at\": " + timestamp  + "}");
            Log.d("PP3", message.getHeader("X-SMTPAPI")[0]);
            Transport.send(message);
        }
        catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    public void SendGroceryListMail(String userEmail, String userName, ArrayList<String> customBody, Uri filename, Context calling_context, long timestamp) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("illuminatedllama@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(userEmail.trim())
            );
            message.setSubject("Grocery list sent via Tedium.");

            Multipart multipart = new MimeMultipart();

            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "Dear " +
                    userName +
                    ",<br>\n" +
                    "<br>\n" +
                    "Attached is the grocery list you have chosen to generate via Tedium.\n" +
                    "<br>\n";
            for(int i = 0; i < customBody.size(); i++)
            {
                htmlText = htmlText + customBody.get(i) + "\n<br>\n";
            }
            htmlText = htmlText +
                    "<br>\n" +
                    "<br>\n" +
                    "Warm regards,<br>\n" +
                    "The Tedium team";
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);


            if(filename != null) {
                Cursor returnCursor =
                        calling_context.getContentResolver().query(filename, null, null, null, null);
                assert returnCursor != null;
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                String name = returnCursor.getString(nameIndex);
                returnCursor.close();

                MimeBodyPart textBodyPart = new MimeBodyPart();
                ByteArrayDataSource tds = new ByteArrayDataSource(calling_context.getContentResolver().openInputStream(filename),
                        calling_context.getContentResolver().getType(filename));
                textBodyPart.setDataHandler(new DataHandler(tds));
                textBodyPart.setFileName(name);
                multipart.addBodyPart(textBodyPart);
            }

            message.setContent(multipart);
            message.addHeader("X-SMTPAPI", "{" + "\"send_at\": " + timestamp  + "}");
            Log.d("PP3", message.getHeader("X-SMTPAPI")[0]);
            Transport.send(message);
        }
        catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    public void SendItemShareMail(String userEmail, String userName, String itemName, ArrayList<String> customBody, Uri uri, String filename,
                                  Context calling_context, long timestamp) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("illuminatedllama@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(userEmail.trim())
            );
            message.setSubject("Information about " + itemName + ".");

            Multipart multipart = new MimeMultipart();

            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "Dear " +
                    userName +
                    ",<br>\n" +
                    "<br>\n" +
                    "Attached is the details of .\n" +
                    itemName +
                    " that you have chosen to share via Tedium." +
                    "<br>\n";
            for(int i = 0; i < customBody.size(); i++)
            {
                htmlText = htmlText + customBody.get(i) + "\n<br>\n";
            }
            htmlText = htmlText +
                    "<br>\n" +
                    "<br>\n" +
                    "Warm regards,<br>\n" +
                    "The Tedium team";
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);

            /*
            if(filename != null) {
                MimeBodyPart textBodyPart = new MimeBodyPart();
                ByteArrayDataSource tds = new ByteArrayDataSource(calling_context.getContentResolver().openInputStream(uri),
                        calling_context.getContentResolver().getType(uri));
                textBodyPart.setDataHandler(new DataHandler(tds));
                textBodyPart.setFileName(filename);
                multipart.addBodyPart(textBodyPart);
            }
             */

            message.setContent(multipart);
            message.addHeader("X-SMTPAPI", "{" + "\"send_at\": " + timestamp  + "}");
            Log.d("PP3", message.getHeader("X-SMTPAPI")[0]);
            Transport.send(message);
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}