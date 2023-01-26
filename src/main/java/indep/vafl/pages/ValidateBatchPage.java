package indep.vafl.pages;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import indep.vafl.datarepo.BatchRepository;
import indep.vafl.datarepo.UserRepository;
import indep.vafl.entity.Batch;


@Controller
public class ValidateBatchPage {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BatchRepository batchRepository;
	
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@GetMapping(value="/validateBatch/{batchID}")  
    public String validateBatch(@PathVariable(value = "batchID") Integer batchID, Model model) {  
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Integer userID = userRepository.findByUserName(authentication.getName()).get().getId();
		
		Optional<Batch> toTest = batchRepository.findById(batchID);
		
		if(toTest.isEmpty())
			return "error.html";
		
		if(toTest.get().isBatchComplete())
			return "error.html";

		model.addAttribute("batchID", batchID);
		model.addAttribute("userID", userID);
		
		return "validateBatch.html";  
    } 

}
