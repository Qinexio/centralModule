package indep.vafl.utility;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class Register implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="Va rugam introduceti un nume de utilizator")
	@Size(min=4, max=32, message="Intre 4 si 32 de caractere")
	private String registerUser;
	
	@NotNull(message="Va rugam introduceti un nume")
	@Size(min=6, max=32, message="Numele trebuie sa fie intre 6 si 32 de caractere")
	private String registerName;
	
	@NotNull(message="Va rugam introduceti o parola")
	@Size(min=4, max=16, message="Parola trebuie sa fie intre 4 si 16 caractere")
	private String registerPass;
	
	@NotNull(message="Va rugam introduceti parola din nou")
	@Size(min=4, max=16, message="Parola trebuie sa fie intre 4 si 16 caractere")
	private String registerConfirmPass;
	
	@NotNull(message="Va rugam introduceti o adresa de email")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message="Introduceti o adresa de email")
	private String registerMail;
	
	public Register()
	{
		
	}

	public String getRegisterUser() {
		return registerUser;
	}
	
	public String getRegisterName() {
		return registerName;
	}

	public void setRegisterName(String registerName) {
		this.registerName = registerName;
	}

	public void setRegisterUser(String registerUser) {
		this.registerUser = registerUser;
	}

	public String getRegisterPass() {
		return registerPass;
	}

	public void setRegisterPass(String registerPass) {
		this.registerPass = registerPass;
	}

	public String getRegisterConfirmPass() {
		return registerConfirmPass;
	}

	public void setRegisterConfirmPass(String registerConfirmPass) {
		this.registerConfirmPass = registerConfirmPass;
	}

	public String getRegisterMail() {
		return registerMail;
	}

	public void setRegisterMail(String registerMail) {
		this.registerMail = registerMail;
	}

}
