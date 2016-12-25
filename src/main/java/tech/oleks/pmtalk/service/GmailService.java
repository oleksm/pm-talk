package tech.oleks.pmtalk.service;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.model.Message;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import tech.oleks.pmtalk.bean.Order;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by alexm on 12/24/16.
 */
@Singleton
public class GmailService extends ManagedService {
    @Inject
    GoogleApiService api;

    //Profile profile;
    String userId = "me";

    @Override
    public void start() throws IOException {
        log.info("GmailService start ...");
        //profile = api.getGmail().users().getProfile("me").execute();
//        log.info("I am " + profile.getEmailAddress());
        log.info("GmailService start DONE");
    }


    /**
     *
     * @param o
     * @throws IOException
     */
    public void sendMessage(Order o) throws IOException {
        String subject = config.getEmailSubjectT().replaceAll("%NAME%", o.getCandidate());
        String bodyText = String.format("" +
                "Candidate: %s\n\n" +
                "Report: %s\n\n" +
                "Resume: %s\n\n" +
                "Meeting: %s\n\n" +
                "Staffing Link: %s\n\n" +
                "Coding Doc: %s\n\n",
                o.getCandidate(), "(!@#@!)", o.getResumeId(), o.getMeetingLink(), o.getStaffingLink(),
                o.getCodingLink()
        );

        try {
            MimeMessage email = createEmail(subject, bodyText);
            sendMessage(email);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param subject  subject of the email
     * @param bodyText body text of the email
     * @return the MimeMessage to be used to send email
     * @throws MessagingException
     */
    private MimeMessage createEmail(
            String subject,
            String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
//        email.setFrom(new InternetAddress(userId));
        for (String recipient : config.getShareWithPeople()) {
            email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(recipient));
        }
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    /**
     * Create a message from an email.
     *
     * @param emailContent Email to be set to raw of message
     * @return a message containing a base64url encoded email
     * @throws IOException
     * @throws MessagingException
     */
    private static Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    /**
     * Send an email from the user's mailbox to its recipient.
     * <p>
     * can be used to indicate the authenticated user.
     *
     * @param emailContent Email to be sent.
     * @return The sent message
     * @throws MessagingException
     * @throws IOException
     */
    private Message sendMessage(
            MimeMessage emailContent)
            throws MessagingException, IOException {
        Message message = createMessageWithEmail(emailContent);
        message = api.getGmail().users().messages().send(userId, message).execute();

        log.info("Message id: " + message.getId());
        log.info("Message body: " + message.toPrettyString());
        return message;
    }
}
