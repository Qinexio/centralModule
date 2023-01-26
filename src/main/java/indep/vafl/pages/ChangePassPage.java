package indep.vafl.pages;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import indep.vafl.datarepo.CredidentialRepository;
import indep.vafl.datarepo.UserRepository;

import indep.vafl.service.PassService;
import indep.vafl.utility.Password;


@Controller
public class ChangePassPage {

	@Autowired
	UserRepository userRepository;

	@Autowired
	CredidentialRepository credRepository;

	@Autowired
	PassService passService;

	@Secured("ROLE_USER")
	@GetMapping(value = "/changePass")
	public String passForm(Model model) {

		model.addAttribute("changePassObj", new Password());

		return "changePass.html";
	}

	@Secured("ROLE_USER")
	@PostMapping(value = "/changePass")
	public String formSubmit(@Valid @ModelAttribute(value = "changePassObj") Password passResult, BindingResult bindingResult,
			Model model) {

		if (!passResult.getFirstPass().equals(passResult.getConfirmPass())) {
			bindingResult.rejectValue("confirmPass", null, "Parolele nu sunt identice");
		}
		if (bindingResult.hasErrors()) {
			return "changePass";
		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Integer userID = userRepository.findByUserName(authentication.getName()).get().getId();
		
		credRepository.findByCredUserId(userID).map(cred -> {
			cred.setCredSalt(passService.generateSalt());
			cred.setCredPassword(passService.encryptPass(passResult.getFirstPass(), cred.getCredSalt()));
			return credRepository.save(cred);
		});

		return "redirect:/home?success";
	}

}
