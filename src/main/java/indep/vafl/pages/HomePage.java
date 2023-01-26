package indep.vafl.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomePage {
	
	 @RequestMapping(value="/home", method=RequestMethod.GET)  
	    public String home() {  
	        return "home.html";  
	    }  
	 
	 @RequestMapping(value="/error", method=RequestMethod.GET)  
	    public String err() {  
	        return "error.html";   
	    } 
	 
	 @RequestMapping(value="/", method=RequestMethod.GET)  
	    public String altHome() {  
	        return "home.html";  
	    } 
	 
	 @RequestMapping(value="/about", method=RequestMethod.GET)
	    public String about() {  
	        return "aboutPage.html";  
	    } 
}
