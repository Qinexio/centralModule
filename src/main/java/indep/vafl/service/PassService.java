package indep.vafl.service;

public interface PassService {
	
	public String encryptPass(String toEncrypt, String encrpytionSalt);
	public String generateSalt();
	public String generatePass();

}
