package indep.vafl.pages;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class TrainModelPage {
	
	@Secured("ROLE_ADMIN")
	@GetMapping(value="/trainPage")  
    public String validateBatch(Model model) {  
		
		return "trainModel.html";  
    } 

}
