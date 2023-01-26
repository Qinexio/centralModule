package indep.vafl.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import indep.vafl.datarepo.CredidentialRepository;
import indep.vafl.datarepo.UserRepository;
import indep.vafl.service.EmailService;
import indep.vafl.service.PassService;

@Controller
public class ConfirmationPage {

	@Autowired
	UserRepository userRepository;

	@Autowired
	CredidentialRepository credRepository;

	@Autowired
	EmailService emailService;

	@Autowired
	PassService passService;

	@RequestMapping(value = "/confirmUser/{confirmationString}", method = RequestMethod.GET)
	public String confirmUser(@PathVariable(value = "confirmationString") String confString) {
		if (!userRepository.existsByUserConfirmation(confString)) {
			return "error.html";
		}

		userRepository.findByUserConfirmation(confString).map(user -> {
			user.setUserIsVerified(true);
			return userRepository.save(user);
		});

		return "redirect:/home?success";
	}

	@RequestMapping(value = "/resetPass/{confirmationString}", method = RequestMethod.GET)
	public String resetPass(@PathVariable(value = "confirmationString") String confString) {
		if (!userRepository.existsByUserConfirmation(confString)) {
			return "error.html";
		}

		userRepository.findByUserConfirmation(confString).map(user -> {
			return credRepository.findByCredUserId(user.getId()).map(cred -> {
				String newPass = passService.generatePass();
				if(!user.isUserIsVerified())
					emailService.sendConfirmation(confString, user.getUserMail());
				
				cred.setCredSalt(passService.generateSalt());
				emailService.sendResetPassword(newPass , user.getUserMail());
				cred.setCredPassword(passService.encryptPass(newPass, cred.getCredSalt()));
				return credRepository.save(cred);
			});
		});

		return "redirect:/home?success";
	}

	@RequestMapping(value = "/forgotPass", method = RequestMethod.POST)
	public String forgotPass(@RequestParam(value = "uForgName") String userName) {
		if (!userRepository.findByUserName(userName).isPresent())
			return "redirect:/home?errorpass";

		userRepository.findByUserName(userName).map(user -> {
			emailService.sendPasswordLink(user.getUserConfirmation(), user.getUserMail());
			return null;
		});
		
		return "redirect:/home?success";
	}

}
