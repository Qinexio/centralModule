package indep.vafl.service;

public interface EmailService {

	void sendConfirmation(String userConfirmation, String userMail);
	void sendPasswordLink(String userConfirmation, String userMail);
	void sendResetPassword(String passWord, String userMail);
}
