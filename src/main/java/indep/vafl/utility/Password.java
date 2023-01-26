package indep.vafl.utility;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Password implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@NotNull(message="Va rugam introduceti o parola")
	@Size(min=4, max=16, message="Parola trebuie sa fie intre 4 si 16 caractere")
	private String firstPass;
	
	@NotNull(message="Va rugam introduceti parola din nou")
	@Size(min=4, max=16, message="Parola trebuie sa fie intre 4 si 16 caractere")
	private String confirmPass;

	public Password() {
		super();
	}

	public String getFirstPass() {
		return firstPass;
	}

	public void setFirstPass(String firstPass) {
		this.firstPass = firstPass;
	}

	public String getConfirmPass() {
		return confirmPass;
	}

	public void setConfirmPass(String confirmPass) {
		this.confirmPass = confirmPass;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}