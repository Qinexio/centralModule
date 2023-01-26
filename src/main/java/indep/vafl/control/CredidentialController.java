package indep.vafl.control;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import indep.vafl.service.PassServiceImpl;
import indep.vafl.utility.EntityNotFoundException;
import indep.vafl.datarepo.CredidentialRepository;
import indep.vafl.datarepo.UserRepository;
import indep.vafl.entity.Credidential;

@RestController
public class CredidentialController {
	@Autowired
	CredidentialRepository credRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PassServiceImpl passService;

	@Secured("ROLE_ADMIN")
	@GetMapping("/user/{userID}/cred")
	public Optional<Credidential> getByUserID(@PathVariable(value = "userID") Integer userID) {
		return credRepository.findByCredUserId(userID);
	}

	@Secured("ROLE_ADMIN")
	// uses SHA-256 encryption
	@PostMapping("/user/{userID}/cred")
	public Credidential createCred(@PathVariable(value = "userID") Integer userID,
			@Valid @RequestBody Credidential cred) {
		return userRepository.findById(userID).map(user -> {
			cred.setCredSalt(passService.generateSalt());
			cred.setCredPassword(passService.encryptPass(cred.getCredPassword(), cred.getCredSalt()));
			cred.setCredUser(user);
			return credRepository.save(cred);
		}).orElseThrow(() -> new EntityNotFoundException("UserID " + userID + " not found"));
	}

	@Secured("ROLE_ADMIN")
	@PutMapping("/user/{userID}/cred/{credID}")
	public Credidential updateCred(@PathVariable(value = "userID") Integer userID,
			@PathVariable(value = "credID") Integer credID, @Valid @RequestBody Credidential credRequest) {
		if (!userRepository.existsById(userID)) {
			throw new EntityNotFoundException("userID " + userID + " not found");
		}

		return credRepository.findById(credID).map(cred -> {
			cred.setCredSalt(passService.generateSalt());
			cred.setCredPassword(passService.encryptPass(cred.getCredPassword(), cred.getCredSalt()));
			return credRepository.save(cred);
		}).orElseThrow(() -> new EntityNotFoundException("credID " + credID + "not found"));
	}

	@Secured("ROLE_ADMIN")
	@DeleteMapping("/user/{userID}/cred/{credID}")
	public ResponseEntity<?> deleteCred(@PathVariable(value = "userID") Integer userID,
			@PathVariable(value = "credID") Integer credID) {
		if (!userRepository.existsById(userID)) {
			throw new EntityNotFoundException("userID " + userID + " not found");
		}

		return credRepository.findById(credID).map(cred -> {
			credRepository.delete(cred);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new EntityNotFoundException("credID " + credID + " not found"));
	}
}
