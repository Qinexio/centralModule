package indep.vafl.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class PassServiceImpl implements PassService {

	@Override
	public String encryptPass(String toEncrypt, String encryptionSalt) {
		return DigestUtils.sha256Hex(toEncrypt.concat(encryptionSalt));
	}

	@Override
	public String generateSalt() {
		return RandomStringUtils.randomAlphabetic(16);
	}
	
	@Override
	public String generatePass() {
		return RandomStringUtils.randomAlphabetic(32);
	}

}
