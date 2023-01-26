package indep.vafl.control;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
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

import indep.vafl.service.EmailServiceImpl;
import indep.vafl.utility.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import indep.vafl.datarepo.UserRepository;
import indep.vafl.entity.User;

@RestController
public class UserController {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	EmailServiceImpl emailService;
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/user")
	public Page<User> getAllUsers(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/user")
	public User createUser(@Valid @RequestBody User user) {	
		user.setUserConfirmation(RandomStringUtils.randomAlphabetic(16));
		user.setUserIsVerified(false);
		//emailService.sendConfirmation(user.getUserConfirmation(), user.getUserMail());
		return userRepository.save(user);
	}
	
	@Secured("ROLE_ADMIN")
	//update, might need some changes
	@PutMapping("/user/{userID}")
	public User updateUser(@PathVariable(value = "userID") Integer userID, @Valid @RequestBody User userRequest) {
		return userRepository.findById(userID).map(user -> {
			user.setUserIsVerified(userRequest.isUserIsVerified());
			user.setUserMail(userRequest.getUserMail());
			return userRepository.save(user);
		}).orElseThrow(() -> new EntityNotFoundException("UserID " + userID + " not found"));
	}

	@Secured("ROLE_ADMIN")
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable(value = "userID") Integer userID) {
		return userRepository.findById(userID).map(user -> {
			userRepository.delete(user);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new EntityNotFoundException("UserID " + userID + " not found"));
	}
}
