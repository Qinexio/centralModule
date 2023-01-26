package indep.vafl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public JavaMailSender mailSender;

	@Value("${admin.domain}")
	private String originLoc;
	
	@Override
	public void sendConfirmation(String userConfirmation, String userMail) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(userMail);
		message.setSubject("Confirmare cont utilizator");
		message.setText("Multumimim pentru inregistrarea in sistemul nostru. "
				+ "Pentru a va valida contul, apasati urmatorul link: " + "http://"+originLoc+"/confirmUser/"
				+ userConfirmation);
		try {
			this.mailSender.send(message);
		} catch (MailException exception) {
			logger.warn(exception.getMessage());
		} 
	}

	@Override
	public void sendPasswordLink(String userConfirmation, String userMail) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(userMail);
		message.setSubject("Resetare parola");
		message.setText(
				"Pentru a primi o parola noua, urmati urmatorul link, daca nu ati facut solicitarea, ignorati mesajul: "
						+ "http://"+originLoc+"/resetPass/" + userConfirmation);
		try {
			this.mailSender.send(message);
		} catch (MailException exception) {
			logger.warn(exception.getMessage());
		}
	}

	public void sendResetPassword(String passWord, String userMail) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(userMail);
		message.setSubject("Parola resetata");
		message.setText("Parola voastra noua: " + passWord);
		try {
			this.mailSender.send(message);
		} catch (MailException exception) {
			logger.warn(exception.getMessage());
		}
	}
}
