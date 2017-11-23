import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailGenerator {
	private Properties mailServerProperties;
	private Session mailSession;
	private MimeMessage mailMessage;
	private Transport transport;
	
	public EmailGenerator(){
		//setup Mail Server Properties
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		System.out.println("Mail Server Properties have been setup successfully..");
 
		//get mail session
		mailSession = Session.getDefaultInstance(mailServerProperties, null);
		
		//Get Session and Send mail
		try {
			transport = mailSession.getTransport("smtp");
			// Enter your correct gmail UserID and Password
			// if you have 2FA enabled then provide App Specific Password
			transport.connect("smtp.gmail.com", "2102.or3@gmail.com", "Lamig2017");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMail(String recipient, String message){
		mailMessage = new MimeMessage(mailSession);
		try {
			//add recipient(s) to email, and create email contents
			mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			mailMessage.setSubject("Temporary Password for your OR3 account.");
			mailMessage.setContent(message, "text/html");
			System.out.println("Mail Session has been created successfully..");
			
			transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
		} catch (MessagingException e) {
			System.out.println("Could not send email. Something went wrong.");
			e.printStackTrace();
		}
	}
	
	public void close() throws MessagingException{
		transport.close();		
	}
	
}
