package indep.vafl.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import indep.vafl.datarepo.ProfileRepository;
import indep.vafl.entity.Profile;

@Controller
public class UserPage {
	
	@Autowired
	ProfileRepository profileRepository;
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/showUsers")
	public String getUsersAdmin(@PageableDefault(size = 10) Pageable pageable, Model model) {
		
		Page<Profile> page = profileRepository.findAll(pageable);
		model.addAttribute("page", page);
		return "showUserList";
	}


}
