package indep.vafl.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import indep.vafl.datarepo.CredidentialRepository;
import indep.vafl.datarepo.ProfileRepository;
import indep.vafl.datarepo.UserRepository;
import indep.vafl.entity.Credidential;
import indep.vafl.entity.Profile;
import indep.vafl.entity.User;

@Component
public class AuthentificationComponent implements AuthenticationProvider {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CredidentialRepository credRepository;
	
	@Autowired
	private ProfileRepository profRepository;

	@Autowired
	private PassService passService;
	
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		
		String userName = auth.getName();
		String passWord = (String) auth.getCredentials();
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		
		Optional<User> user = userRepository.findByUserName(userName);
		if (!user.isPresent()) {
			throw new BadCredentialsException("User does not exist.");
		}
		
		if (!user.get().isUserIsVerified()) {
			throw new BadCredentialsException("User is not validated.");
		}
		
		Optional<Profile> userProfile = profRepository.findByProfileUserId(user.get().getId());
		
		if (userProfile.get().isProfileIsBanned()) {
			throw new BadCredentialsException("User is banned.");
		}
		
		Optional<Credidential> cred = credRepository.findByCredUserId(user.get().getId());

		passWord = passService.encryptPass(passWord, cred.get().getCredSalt());

		if (!passWord.equals(cred.get().getCredPassword())) {
			throw new BadCredentialsException("Wrong password.");
		}

		grantedAuthorities.add(new SimpleGrantedAuthority(userProfile.get().getProfileRole()));

		return new UsernamePasswordAuthenticationToken(userName, passWord, grantedAuthorities);

	}

	@Override
	public boolean supports(Class<?> aClass) {
		return aClass.equals(UsernamePasswordAuthenticationToken.class);
	}

}
