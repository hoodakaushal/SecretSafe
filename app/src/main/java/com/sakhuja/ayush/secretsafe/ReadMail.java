package com.sakhuja.ayush.secretsafe;

/**
 * Created by Ayush on 1/1/2015.
 */
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.AndTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;


public class ReadMail {

    public static List<Email> read(String host, String user, String pass, String tag) throws Exception {

        List<Email> emails = new ArrayList<Email>();

        // Create empty properties
        Properties props = new Properties();

        // Get the session
        Session session = Session.getInstance(props, null);

        // Get the store
        Store store = session.getStore("imaps");
        store.connect(host, user, pass);
        System.out.println("Connected");
        // Get folder
        Folder folder = store.getFolder("Inbox");
        folder.open(Folder.READ_WRITE);
        System.out.println("Inbox Opened");
        try {
            // Get directory listing
            //Address from = new Add
            SearchTerm sTerm = new SubjectTerm(tag);
            SearchTerm fTerm = new FromStringTerm(user);
            SearchTerm aTerm = new AndTerm(sTerm,fTerm);
            Message messages[] = folder.search(aTerm);
            System.out.println(messages.length);
            for (int i = 0; i < messages.length; i++) {

                Email email = new Email();

                email.subject = messages[i].getSubject();
                System.out.println(email.subject);
                if (email.subject==null){email.subject="no subject";}
                //Pattern pattern = Pattern.compile("Sample");
                //Matcher matcher = pattern.matcher(email.subject.trim());
                //if (matcher.find()){

                // received date
                if (messages[i].getReceivedDate() != null) {
                    email.received = messages[i].getReceivedDate();
                } else {
                    email.received = new Date();
                }

                // body and attachments
                email.body = "";
                Object content = messages[i].getContent();
                if (content instanceof java.lang.String) {

                    email.body = (String) content;

                } else if (content instanceof Multipart) {

                    Multipart mp = (Multipart) content;

                    for (int j = 0; j < mp.getCount(); j++) {

                        Part part = mp.getBodyPart(j);
                        String disposition = part.getDisposition();

                        if (disposition == null) {

                            MimeBodyPart mbp = (MimeBodyPart) part;
                            if (mbp.isMimeType("text/plain")) {
                                // Plain
                                email.body += (String) mbp.getContent();
                            }

                        } else if ((disposition.equalsIgnoreCase(Part.ATTACHMENT) || disposition .equalsIgnoreCase(Part.INLINE))) {

                            // Check if plain
                            MimeBodyPart mbp = (MimeBodyPart) part;
                            if (mbp.isMimeType("text/plain")) {
                                email.body += (String) mbp.getContent();
                            } else {
                                EmailAttachment attachment = new EmailAttachment();
                                attachment.name = decodeName(part.getFileName());
                                email.attachments.add(attachment);
                            }
                        }
                    } // end of multipart for loop
                } // end messages for loop

                emails.add(email);

            }

            // Close connection
            folder.close(false); // true tells the mail server to expunge deleted messages
            store.close();

        } catch (Exception e) {
            folder.close(false); // true tells the mail server to expunge deleted
            store.close();
            throw e;
        }

        return emails;
    }
    private static String decodeName(String name) throws Exception {
        if (name == null || name.length() == 0) {
            return "unknown";
        }
        String ret = java.net.URLDecoder.decode(name, "UTF-8");

        // also check for a few other things in the string:
        ret = ret.replaceAll("=\\?utf-8\\?q\\?", "");
        ret = ret.replaceAll("\\?=", "");
        ret = ret.replaceAll("=20", " ");

        return ret;
    }

    private static int saveFile(File saveFile, Part part) throws Exception {

        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(saveFile));

        byte[] buff = new byte[2048];
        InputStream is = part.getInputStream();
        int ret = 0, count = 0;
        while ((ret = is.read(buff)) > 0) {
            bos.write(buff, 0, ret);
            count += ret;
        }
        bos.close();
        is.close();
        return count;
    }
}

